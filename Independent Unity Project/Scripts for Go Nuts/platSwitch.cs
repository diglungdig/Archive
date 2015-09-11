using UnityEngine;
using System.Collections;

public class platSwitch : MonoBehaviour
{

	private Animator anim;
	private int orderNum;
	private bool ishittingSpace;
	private bool animPlayed;

	public AreaEffector2D area1;
	public int turningAngle;
	
	// Use this for initialization
	void Start ()
	{
		
		anim = GetComponent<Animator> ();
		orderNum = 0;
		animPlayed = false;
	}
	
	// Update is called once per frame
	void Update ()
	{



		if (animPlayed == true) {

			animPlayed = false;

			switch (orderNum) {
			case 1:
				normalDir (-turningAngle);
				break;
			case 2:
				normalDir (turningAngle);
				break;
			case 3:
				normalDir (turningAngle);
				break;
			case 4:
				normalDir (-turningAngle);
				break;
			default:
				break;
			}
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

			if (orderNum != 4) {
				orderNum++;
				Debug.Log ("orderNum is" + orderNum);
			} else {
				orderNum = 0;
			}

			anim.SetInteger ("orderNum", orderNum);
			animPlayed = true;
		}
	}
	
	void normalDir (int dir)
	{
		Debug.Log (dir);
		area1.forceAngle = area1.forceAngle + dir;
	}

}
