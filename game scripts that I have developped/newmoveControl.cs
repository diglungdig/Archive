using UnityEngine;
//using CrossPlatformInput;

[RequireComponent(typeof(newControlScript))]

public class newmoveControl : MonoBehaviour
{
	private newControlScript character01;
	
	public Rigidbody2D rb;
	
	private void Awake ()
	{
		character01 = GetComponent<newControlScript> ();
		Transform charr = transform;
		Vector3 theScale = charr.localScale;
		
	}

	
	private void FixedUpdate ()
	{
		// Read the inputs.
		//bool crouch = Input.GetKey (KeyCode.LeftControl);

		float h = Input.GetAxis ("Horizontal");
		float v = Input.GetAxis ("Vertical");
		// Pass all parameters to the character control script.
		character01.Move (h, v);
	}
}
