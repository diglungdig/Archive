using UnityEngine;
using System.Collections;

public class CharacterPropertiesFunc : MonoBehaviour
{
	
	//Basic Properties
	[SerializeField]
	private float
		maxSpeed = 10f; // The fastest the player can travel in the x axis.
	[SerializeField]
	private float
		jumpForce = 400f; // Amount of force added when the player jumps.
	[SerializeField]
	private bool
		airControl = false; // Whether or not a player can steer while jumping;
	[Range(0, 1)]
	[SerializeField]
	private float
		crouchSpeed = .36f;
	
	
	private Transform theSquirrel;
	private	Vector3 theScale;
	
	
	//Variables need by jump
	private bool grounded = false; // Whether or not the player is grounded.
	
	
	private BoxCollider2D test02;
	
	//Used for double jump. Set to 1 when only jump once. Set to 2 when the second jump is pressed, which will disable the jump.
	private int jumpNum = 1;
	
	//Masks for all the ground sprites
	public LayerMask groundCheck;
	
	//Animator
	//private Animator anim; // Reference to the player's animator component.
	
	
	private void Awake ()
	{
		// Setting up references.
		
		theSquirrel = transform;
		theScale = theSquirrel.localScale;
		test02 = GetComponent<BoxCollider2D> ();
	}
	
	
	private void FixedUpdate ()
	{
		// The player is grounded if a circlecast to the groundcheck position hits anything designated as ground
		grounded = true;
	}
	
	
	public void Move (float move, bool crouch, bool jump)
	{
		
		
		// Reduce the speed if crouching by the crouchSpeed multiplier
		move = (crouch ? move * crouchSpeed : move);
		
		
		// Move the character
		GetComponent<Rigidbody2D> ().velocity = new Vector2 (move * maxSpeed, GetComponent<Rigidbody2D> ().velocity.y);
		
		
		//Jump happens when player hits the space button and the object is currently grounded
		if (grounded && jump) {
			
			// Add a vertical force to the player.
			grounded = false;
			GetComponent<Rigidbody2D> ().AddForce (new Vector2 (0f, jumpForce), ForceMode2D.Force);
			jumpNum = 1;
		} else if (jump && jumpNum == 1) {
			//Check for double jump
			grounded = false;
			jumpNum = 2;
			Vector3 temp;
			temp = new Vector2 (GetComponent<Rigidbody2D> ().velocity.x, GetComponent<Rigidbody> ().velocity.y * 0.1f);
			GetComponent<Rigidbody2D> ().velocity = temp;
			GetComponent<Rigidbody2D> ().AddForce (new Vector2 (0f, jumpForce * 0.8f), ForceMode2D.Force);
		}
	}
	
	
}

