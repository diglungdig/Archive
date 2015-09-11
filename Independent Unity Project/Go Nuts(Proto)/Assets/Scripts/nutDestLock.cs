using UnityEngine;
using System.Collections;

public class nutDestLock : MonoBehaviour
{

	public Animator anim;
	public GameObject youwin;

	void OnTriggerEnter2D (Collider2D other)
	{
		other.attachedRigidbody.velocity = Vector2.zero;
		other.attachedRigidbody.gravityScale = 0;
		other.attachedRigidbody.AddTorque (10);
		anim.SetBool ("Lock!", true);

		Camera.main.GetComponent<CameraFollow> ().enabled = false;
		youwin.SetActive (true);

	}
}
