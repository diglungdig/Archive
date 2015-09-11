using UnityEngine;
using System.Collections;

[RequireComponent (typeof(SpriteRenderer))]

public class tining : MonoBehaviour {

	public int offset = 2;

	public bool hasARightBuddy = false;
	public bool hasALeftBuddy = false;

	public bool reverseScale = false;

	private float spriteWidth = 0f;
	private Camera cam;
	private Transform myTransform;
	
	void Awake(){
		cam = Camera.main;
		myTransform = transform;

		}


	// Use this for initialization
	void Start () {
		SpriteRenderer sRenderer = GetComponent<SpriteRenderer> ();
		spriteWidth = sRenderer.sprite.bounds.size.x;

	}
	
	// Update is called once per frame
	void Update () {
		if(hasALeftBuddy == false || hasARightBuddy == false){

			float camHoriExtend = cam.orthographicSize * Screen.width/Screen.height;

			float edgeRight = (myTransform.position.x+ spriteWidth/2) - camHoriExtend;
			float edgeLeft =   (myTransform.position.x - spriteWidth/2) + camHoriExtend;

			if(cam.transform.position.x >= edgeRight - offset && hasARightBuddy == false)
			{
				MakeNewBuddy(1);
				hasARightBuddy = true;

			}
			else if(cam.transform.position.x <= edgeLeft + offset && hasALeftBuddy == false)
			{
				MakeNewBuddy(-1);
				hasALeftBuddy = true;

		
			}
	}
}

 void MakeNewBuddy (int rightOrLeft){

		Vector3 newPos = new Vector3 (myTransform.position.x + spriteWidth *rightOrLeft, myTransform.position.y, myTransform.position.z);
		Transform newbuddy = Instantiate (myTransform, newPos, myTransform.rotation) as Transform;
		
		if (reverseScale == true) {
			newbuddy.localScale = new Vector3(newbuddy.localScale.x*-1,newbuddy.localScale.y,newbuddy.localScale.z);
				}
		newbuddy.parent = myTransform.parent;
		if (rightOrLeft > 0) {
						newbuddy.GetComponent<tining> ().hasALeftBuddy = true;
				}
		else{
			     newbuddy.GetComponent<tining> ().hasARightBuddy = true;
				}
}

}