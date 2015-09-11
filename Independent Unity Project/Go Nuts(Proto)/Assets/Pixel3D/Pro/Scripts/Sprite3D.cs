/// <summary>
/// The following monobehavior can be used to generate a "3D sprite" using the pixel information
/// of a 2D sprite object.  The new 3D sprite will be scaled and positioned on top of the 2D object,
/// and will update itself each frame of the sprite's animation.
/// 
/// Created by Matthew Barr
/// ClearWave Interactive, LLC
/// </summary>

using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.Sprites;

namespace Pixel3D{
	public class Sprite3D : MonoBehaviour {

		#region Constants

		private const float minPixelThickness = 0.01f;
		private const float minPixelScale = 0.01f;

		#endregion

		#region Editor Variables
		
		[SerializeField] private GameObject pixelObject;
		[SerializeField] private Material pixelMaterial;
		[SerializeField] private float pixelThickness = 1;
		[SerializeField] private float layerOffset = 0;
		[SerializeField] private float pixelScale = 1;
		[SerializeField] private float colorBlendThreshold = 0.1f;

		#endregion

		#region Member Variables

		private SpriteRenderer spriteRenderer;
		private Transform pixelGroupRef;
		private Sprite currentSprite;
		private Rect origTextureRect;
		private float prevPixelThickness;
		private float prevLayerOffset;
		private float prevPixelScale;
		private float prevBlendThreshold;
		private int nextLayer;
		private bool isValidSprite = true;

		#endregion
		
		#region Collections
		
		private GameObject[ , ] pixelObjects;
		private Dictionary<Color32, int> layersByColor = new Dictionary<Color32, int>();
		private Dictionary<Color32, Material> materialsByColor = new Dictionary<Color32, Material>();
		
		#endregion

		#region Accessors
		
		public GameObject PixelObject{
			get{ return pixelObject; }
			set{
				if(value != null) 
					pixelObject = value;
				else
					pixelObject = Resources.Load<GameObject>("PixelObject");
				
				GameObject.Destroy(pixelGroupRef.gameObject);
				this.pixelGroupRef = null;
				CreatePixelMatrix();
				DrawSprite(currentSprite);
			}
		}
		
		public Material PixelMaterial {
			get{ return pixelMaterial; }
			set{
				if(value != null) 
					pixelMaterial = value;
				else
					pixelMaterial = new Material(Shader.Find ("Standard"));
				
				ModifyMaterials();
			}
		}
		
		public float PixelThickness {
			get{ return pixelThickness; }
			set{ pixelThickness = value; }
		}
		
		public float LayerOffset {
			get{ return layerOffset; }
			set{ layerOffset = value; }
		}
		
		public float PixelScale {
			get{ return pixelScale; }
			set{ pixelScale = value; }
		}
		
		public float ColorBlendThreshold {
			get{ return colorBlendThreshold; }
			set{ colorBlendThreshold = value; }
		}
		
		private bool attributesHaveChanged{
			get{
				return 	(spriteRenderer.sprite != currentSprite) ||
						(pixelScale != prevPixelScale) || 
						(pixelThickness != prevPixelThickness) ||
						(layerOffset != prevLayerOffset) ||
						(colorBlendThreshold != prevBlendThreshold);
			}
		}

		#endregion

		#region Monobehaviors

		void Awake () {

			spriteRenderer = GetComponent<SpriteRenderer>();
			
			if(pixelMaterial == null){
				pixelMaterial = new Material(Shader.Find ("Standard"));
			}

			if(pixelObject == null){
				pixelObject = Resources.Load<GameObject>("PixelObject");
			}

			SetCurrentAttributes();

			CreatePixelMatrix();
		}
		
		void OnEnable () {
			ShowSprite3D();
		}
		
		void Update () {
			ClampAttributes();
			
			if(attributesHaveChanged){
				SetCurrentAttributes();
				InitializeLayers();
				DrawSprite(spriteRenderer.sprite);
			}
		}
		
		void OnDisable () {
			ShowSprite2D();
		}
		
		void OnDestroy () {
			ShowSprite2D();
		}

		#endregion

		#region Sprite3D Methods

		void ShowSprite2D () {
			if(this.pixelGroupRef != null) this.pixelGroupRef.gameObject.SetActive(false);
			if(!spriteRenderer.enabled) spriteRenderer.enabled = true;
		}
		
		void ShowSprite3D () {
			if (!isValidSprite) return;
			if (spriteRenderer.enabled) spriteRenderer.enabled = false;
			if (this.pixelGroupRef != null) this.pixelGroupRef.gameObject.SetActive(true);
			InitializeLayers();
			DrawSprite(spriteRenderer.sprite);
		}
		
		void CreatePixelMatrix () {
			if(pixelGroupRef != null){
				Debug.LogError("CreatePixelMatrix must be cleared before building a new pixel matrix!");
				return;
			}

			var textureRect = currentSprite.textureRect;
			var rectX = (int)textureRect.x;
			var rectY = (int)textureRect.y;
			var rectWidth = (int)textureRect.width;
			var rectHeight = (int)textureRect.height;
			
			if((rectWidth > 128) || (rectHeight > 128)){
				Debug.LogError("Sprite sizes of > 128x128 pixels not currently supported.");
				isValidSprite = false;
				return;
			}

			Transform pixelGroup = Resources.Load<Transform>("PixelGroup");
			
			this.origTextureRect = textureRect;
			this.pixelObjects = new GameObject[rectWidth, rectHeight];
			this.pixelGroupRef = GameObject.Instantiate<Transform>(pixelGroup);
			this.pixelGroupRef.parent = this.transform;
			
			// Scale the resulting sprite down to be the size of the 2D sprite
			var ppu = currentSprite.pixelsPerUnit;
			var pivot = (spriteRenderer.bounds.center - transform.position) / (rectWidth / ppu);
			var rectOffset = new Vector3(-rectWidth/2, -rectHeight/2, 0);
			var pixelOffset = new Vector3(0.5f, 0.5f, 0);
			var pivotOffset = new Vector3(rectWidth * pivot.x / transform.localScale.x, 
			                              rectHeight * pivot.y / transform.localScale.y, 
			                              0);

			this.pixelGroupRef.localScale = Vector3.one / ppu;
			this.pixelGroupRef.localPosition = (rectOffset + pixelOffset + pivotOffset) / ppu;

			GameObject go;
			Transform tf;
			
			for(var y = rectY; y < rectY + rectHeight; y++){
				for(var x = rectX; x < rectX + rectWidth; x++){
					go = GameObject.Instantiate<GameObject>(pixelObject);
					pixelObjects[x - rectX, y - rectY] = go;
					
					tf = go.transform;
					tf.parent = this.pixelGroupRef;
					tf.localPosition = new Vector3(x - rectX, y - rectY, 0);
					tf.localScale = new Vector3(pixelScale, pixelScale, pixelThickness);
					
					go.SetActive(false);
				}
			}
		}

		void DrawSprite(Sprite sprite){
			var texture = sprite.texture;
			var textureWidth = texture.width;
			var textureRect = sprite.textureRect;

			if(textureRect.width != origTextureRect.width || textureRect.height != origTextureRect.height){
				Debug.LogError(	"Sprize changes in animation are not supported. Aborting DrawSprite method.\n" +
								"spriteSize = " + origTextureRect + "\n" +
								"textureRect = " + textureRect);
				ShowSprite2D();
				return;
			}

			var pixels = texture.GetPixels32();
			var rectX = (int)textureRect.x;
			var rectY = (int)textureRect.y;
			var rectWidth = (int)textureRect.width;
			var rectHeight = (int)textureRect.height;
			
			// loop variables
			int pixelIndex;
			Color32 pixel;
			float zPos;
			float zScale;
			int layer;
			
			GameObject go;
			Transform tf;
			Renderer rd;
			
			for(int y = rectY; y < rectY + rectHeight; y++){
				for(int x = rectX; x < rectX + rectWidth; x++){
					pixelIndex = y * textureWidth + x;
					pixel = pixels[pixelIndex];

					go = pixelObjects[x - rectX, y - rectY];
					tf = go.transform;
					rd = go.GetComponent<Renderer>();
					
					// Filter pixels to avoid drawing transparent pixels
					if(pixel.a > 0){
						if(!go.activeSelf) go.SetActive(true);
						
						layer = GetLayerByColor(pixel);
						
						// clamp the position of each pixel so that the base of the pixels are justified
						zScale = pixelThickness + layerOffset * (layer - 1);
						zPos = -(zScale - pixelThickness) / 2;
						
						tf.parent = this.pixelGroupRef;
						tf.localPosition = new Vector3(x - rectX, y - rectY, zPos);
						tf.localScale = new Vector3(pixelScale, pixelScale, zScale);

						rd.material = GetMaterialByColor(pixel);
					}
					else{
						if(go.activeSelf) go.SetActive(false);
					}
				}
			}
		}

		#endregion

		#region Utility Methods
		
		void ClampAttributes(){
			pixelThickness = pixelThickness >= minPixelThickness ? pixelThickness : minPixelThickness;
			pixelScale = pixelScale >= minPixelScale ? pixelScale : minPixelScale;
			colorBlendThreshold = Mathf.Clamp01(colorBlendThreshold);
		}
		
		void SetCurrentAttributes(){
			currentSprite = spriteRenderer.sprite;
			prevPixelScale = pixelScale;
			prevPixelThickness = pixelThickness;
			prevLayerOffset = layerOffset;
			prevBlendThreshold = colorBlendThreshold;
		}
		
		void InitializeLayers(){
			nextLayer = 1;
			layersByColor = new Dictionary<Color32, int>();
		}
		
		Material GetMaterialByColor(Color32 pixelColor){
			Material mat;
			
			// If pixel's color doesn't exist in the dictionary, create it
			if(!materialsByColor.TryGetValue(pixelColor, out mat)){
				mat = new Material(pixelMaterial);
				mat.color = pixelColor;
				materialsByColor[pixelColor] = mat;
			}
			
			return mat;
		}
		
		void ModifyMaterials(){
			var materials = new List<Material>(materialsByColor.Values);
			Material material;
			Color color;
			
			for(int lcv = 0; lcv < materials.Count; lcv++){
				material = materials[lcv];
				color = material.color;
				material.shader = PixelMaterial.shader;
				material.CopyPropertiesFromMaterial(PixelMaterial);
				material.color = color;
			}
		}
		
		int GetLayerByColor(Color32 pixelColor){
			int layer;

			// If pixel's color doesn't exist in the dictionary...
			if(!layersByColor.TryGetValue(pixelColor, out layer)){
				// Check to see if a similar color has a layer (based on colorBlendThreshold)
				layer = GetBlendedLayer(pixelColor);

				// GetBlendedLayer returns nextLayer if there are no similar colors in layersByColor
				if(layer == nextLayer){
					nextLayer++;
				}
				
				layersByColor[pixelColor] = layer;
			}
			
			return layer;
		}

		// Check to see if there are any colors within colorBlendThreshold in layersByColor
		int GetBlendedLayer(Color32 pixel){
			bool redMatches;
			bool greenMatches;
			bool blueMatches;
			bool alphaMatches;
			Color32 color;
			
			foreach(KeyValuePair<Color32, int> layerByColor in layersByColor){
				color = layerByColor.Key;
				
				redMatches = ChannelsMatch(pixel.r, color.r);
				greenMatches = ChannelsMatch(pixel.g, color.g);
				blueMatches = ChannelsMatch(pixel.b, color.b);
				alphaMatches = ChannelsMatch(pixel.a, color.a);
				
				if(redMatches && greenMatches && blueMatches && alphaMatches){
					return layerByColor.Value; 
				}
			}
			
			return nextLayer;
		}

		bool ChannelsMatch(float pixelChannel, float layerChannel){
			return Mathf.Abs(pixelChannel - layerChannel) < colorBlendThreshold * 255;
		}

		#endregion

	}
}