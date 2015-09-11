using UnityEngine;
//using CrossPlatformInput;
	
[RequireComponent(typeof(CharacterPropertiesFunc))]
public class movecontrol : MonoBehaviour
{
	private CharacterPropertiesFunc character;
	private bool jump;
		
	public Rigidbody2D rb;
	
	private void Awake ()
	{
		character = GetComponent<CharacterPropertiesFunc> ();
		Transform charr = transform;
		Vector3 theScale = charr.localScale;
		//theScale.x *= -1;
		charr.localScale = theScale;
			
	}
	
	private void Update ()
	{
		if (!jump)
				// Read the jump input in Update so button presses aren't missed.
			jump = Input.GetKeyDown (KeyCode.Space);

	}
		
	private void FixedUpdate ()
	{
		// Read the inputs.
		bool crouch = Input.GetKey (KeyCode.LeftControl);
		float h = Input.GetAxis ("Horizontal");
		// Pass all parameters to the character control script.
		character.Move (h, crouch, jump);
		jump = false;
	}
}
