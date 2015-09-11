using UnityEngine;
using System.Collections;

public class finishCheck : MonoBehaviour
{


	// Update is called once per frame
	void Update ()
	{
		if (gameObject.transform.childCount > 0) {
			foreach (Transform child in transform) {
				if (child.tag != "FinishedNut" && child.GetComponent<Rigidbody2D> ().velocity == Vector2.zero) {
					Debug.Log ("why the heck!");
					child.tag = "FinishedNut";
					projectileShooting.nutWasShot = false;
				}
			}
		}
	}
}
