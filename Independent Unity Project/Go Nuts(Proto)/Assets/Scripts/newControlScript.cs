using UnityEngine;
using System.Collections;

public class newControlScript : MonoBehaviour
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

	//Animator
	private Animator anim; // Reference to the player's animator component.
	
	
	private void Awake ()
	{
		// Setting up references.
		
		theSquirrel = transform;
		theScale = theSquirrel.localScale;
		test02 = GetComponent<BoxCollider2D> ();
		anim = GetComponent<Animator> ();
	}
	
	
	private void FixedUpdate ()
	{
		// The player is grounded if a circlecast to the groundcheck position hits anything designated as ground
		grounded = true;



	}
	
	
	public void Move (float move, float vertical)
	{
		
		
		// Reduce the speed if crouching by the crouchSpeed multiplier
		//move = (crouch ? move * crouchSpeed : move);
		
		
		// Move the character
		GetComponent<Rigidbody2D> ().velocity = new Vector2 (move * maxSpeed, vertical * maxSpeed);

		if (GetComponent<Rigidbody2D> ().velocity == Vector2.zero) {
			
			anim.SetBool ("idling", true);
		} else {
			anim.SetBool ("idling", false);
		}

	}
}
