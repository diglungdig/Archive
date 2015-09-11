using UnityEngine;
using System.Collections;

public class PARALEEXX : MonoBehaviour {

	public Transform[] backgrounds;
	private float [] parallaxScales;
	public float smooth = 1f;

	private Transform cam;
	private Vector3 previousCamPos;

	//is called before Start(),Great for references
	void Awake(){
		cam = Camera.main.transform;

		}

	// Use this for initialization
	void Start () {
		previousCamPos = cam.position;

		parallaxScales = new float[backgrounds.Length];

		for (int i = 0; i<backgrounds.Length; i++) {
			parallaxScales[i] = backgrounds[i].position.z*-1;

				}


	}
	
	// Update is called once per frame
	void Update () {
		for (int i = 0; i<backgrounds.Length; i++) {
			float parallax = (previousCamPos.x - cam.position.x)*parallaxScales[i];

			float backgroundTargetPosX = backgrounds[i].position.x + parallax;

			Vector3 backgroundTargetPos = new Vector3(backgroundTargetPosX, backgrounds[i].position.y, backgrounds[i].position.z);

			backgrounds[i].position = Vector3.Lerp(backgrounds[i].position, backgroundTargetPos,smooth*Time.deltaTime);

		}


		previousCamPos = cam.position;

	
	}
}
