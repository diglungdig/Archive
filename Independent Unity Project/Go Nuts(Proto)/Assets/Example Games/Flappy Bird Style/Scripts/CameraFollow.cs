using UnityEngine;
using System.Collections;

public class CameraFollow : MonoBehaviour
{
	private GameObject shot;
	private GameObject character;
	private GameObject character01;

	private bool transformNeeded;
	private float speed;
	private int whichToFollow;

	void Start ()
	{
		shot = GameObject.Find ("Shot");
		character = GameObject.Find ("Character");
		character01 = GameObject.Find ("Character01");
		transformNeeded = false;
		speed = 12.0f;
		whichToFollow = 0;
	}


	void Update ()
	{
		int index = getIndexFromShot ();
		if (shot.transform.childCount > 0 && index != -1) {

			GameObject.Find ("projectile").GetComponent<Rot> ().enabled = false;
			transform.SetParent (null);
			Vector3 poss = shot.transform.GetChild (index).position;
			transform.position = new Vector3 (poss.x, poss.y, transform.position.z);
			transformNeeded = true;

		} else {

			if (transformNeeded == true) {
				transformNeeded = transactionFromNut ();
			} else {

				if (whichToFollow == 0) {
					character.GetComponent<movecontrol> ().enabled = true;
					character.GetComponent<CharacterPropertiesFunc> ().enabled = true;
					character.GetComponentInChildren<projectileShooting> ().enabled = true;
					character.GetComponentInChildren<TrajectoryPredictor> ().enabled = true;

					character01.GetComponent<newmoveControl> ().enabled = false;
					character01.GetComponent<newControlScript> ().enabled = false;
					character01.GetComponent<Rigidbody2D> ().isKinematic = true;

					GameObject.Find("tunnel").GetComponent<tunnelSwitch>().enabled = false;
					GameObject.Find ("newBouncePlat").GetComponent<platSwitch>().enabled = false;
					GameObject.Find("newBouncePlat 1").GetComponent<platSwitch>().enabled = false;

					GameObject.Find ("projectile").GetComponent<Rot> ().enabled = true;
					transform.position = new Vector3 (character.transform.position.x, character.transform.position.y + 5f, transform.position.z);
				} else if (whichToFollow == 1) {

					character.GetComponent<movecontrol> ().enabled = false;
					character.GetComponent<CharacterPropertiesFunc> ().enabled = false;
					character.GetComponentInChildren<projectileShooting> ().enabled = false;
					character.GetComponentInChildren<TrajectoryPredictor> ().enabled = false;

					GameObject.Find("tunnel").GetComponent<tunnelSwitch>().enabled = true;
					GameObject.Find ("newBouncePlat").GetComponent<platSwitch>().enabled = true;
					GameObject.Find("newBouncePlat 1").GetComponent<platSwitch>().enabled = true;


					character01.GetComponent<newmoveControl> ().enabled = true;
					character01.GetComponent<newControlScript> ().enabled = true;
					character01.GetComponent<Rigidbody2D> ().isKinematic = false;
					transform.position = new Vector3 (character01.transform.position.x, character01.transform.position.y, transform.position.z);
				}
			}
		}
	}

	int getIndexFromShot ()
	{
		for (int i = 0; i < shot.transform.childCount; i++) {
			if (shot.transform.GetChild (i).tag != "FinishedNut") {
				return i;
			}
		}
		return -1;
	}
	bool transactionFromNut ()
	{
		//gradually move camera toward chareracter until it reaches, then return false;
		Vector3 posOfChareracter = character.transform.position;
		posOfChareracter = new Vector3 (posOfChareracter.x, posOfChareracter.y + 5f, transform.position.z);

		if (Vector3.Distance (transform.position, posOfChareracter) > 0.1) {

			Vector3 dir = (posOfChareracter - transform.position);
			dir = dir.normalized;
			transform.Translate (dir * speed * Time.deltaTime, Space.World);
			return true;

		} else {
			return false;
		}

	}

	public void setWhichToFollow (int i)
	{
		whichToFollow = i;
		return;
	}


}
