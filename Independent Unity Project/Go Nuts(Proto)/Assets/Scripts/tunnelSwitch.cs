using UnityEngine;
using System.Collections;

public class tunnelSwitch : MonoBehaviour
{

	private Animator anim;
	private bool switchIt;
	private bool ishittingSpace;

	public AreaEffector2D area1;
	public AreaEffector2D area2;
	public int turningAngle;
	
	// Use this for initialization
	void Start ()
	{
		
		anim = GetComponent<Animator> ();

	}
	
	// Update is called once per frame
	void Update ()
	{
		switchIt = anim.GetBool ("switch!");

		if (switchIt == true && area1.forceAngle == 270) {
			normalDir (-turningAngle);
		} else if (switchIt == false && area1.forceAngle == 180) {
			normalDir (turningAngle);
		}


		if (Input.GetKeyDown (KeyCode.Space) == true) {
			
			ishittingSpace = true;
		} else {
			ishittingSpace = false;
		}
		
	}
	
	void OnTriggerStay2D (Collider2D other)
	{
		
		if (other.name == "Character01" && ishittingSpace == true) {
			anim.SetBool ("switch!", !switchIt);
		}
		
	}

	void normalDir (int dir)
	{
		area1.forceAngle = area1.forceAngle + dir;
		area2.forceAngle = area2.forceAngle + dir;
	}
}
