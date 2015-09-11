using UnityEngine;
using System.Collections;

public class Rot : MonoBehaviour
{


	// Update is called once per frame
	void Update ()
	{
	
		//CHANGE THIS SO THAT THE NUT SHOT WILL BE OUT OF THIS CONTROL

		Vector3 mousePos = Input.mousePosition;
		mousePos.z = transform.position.z - Camera.main.transform.position.z;
		Vector3 poss = Camera.main.WorldToScreenPoint (transform.position);
		Vector3 dir = mousePos - poss;
		float angle = Mathf.Atan2 (dir.y, dir.x) * Mathf.Rad2Deg;
		//transform.rotation = Quaternion.AngleAxis (angle, Vector3.forward);
		transform.rotation = Quaternion.Euler (new Vector3 (0, 0, angle));


	}
}
