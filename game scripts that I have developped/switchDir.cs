using UnityEngine;
using System.Collections;

public class switchDir : MonoBehaviour
{

	private Animator anim;
	private bool backRot;
	private bool ishittingSpace;

	// Use this for initialization
	void Start ()
	{
	
		anim = GetComponent<Animator> ();

	}
	
	// Update is called once per frame
	void Update ()
	{
		backRot = anim.GetBool ("backRot");

		if (Input.GetKeyDown (KeyCode.Space) == true) {

			ishittingSpace = true;
		} else {
			ishittingSpace = false;
		}

	}

	void OnTriggerStay2D (Collider2D other)
	{

		if (other.name == "Character01" && ishittingSpace == true) {
			Debug.LogError ("@#!#@!#@!");
			anim.SetBool ("backRot", !backRot);
		}

	}
}
