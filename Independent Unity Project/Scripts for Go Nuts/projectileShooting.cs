using UnityEngine;
using System.Collections;
using UnityEngine.EventSystems;

public class projectileShooting : MonoBehaviour
{

	public LineRenderer ShootingTrack;
	public Transform origin;
	public Rigidbody2D rd;
	private int maxStack;
	public static bool nutWasShot;

	private GameObject nut;
	public static bool held;
	private Vector3 shoortingOrigin;
	private Vector3 before;
	// Use this for initialization
	private float velocity;
	void Start ()
	{
		nutWasShot = false;
		maxStack = 3;
		held = true;

	}
	
	// Update is called once per frame
	void Update ()
	{

		velocity = GameObject.Find ("progressBar").GetComponent<progessBar> ().getfillAmount () * 15;
		//velocity = 20;
		Vector3 pp = (Input.mousePosition - transform.position).normalized;
		Debug.Log ("final Position:" + pp);

		if (held == false && maxStack > 0) {
			StartCoroutine ("projectileInitiate");
			held = true;

		} else {
			if (Input.GetMouseButtonDown (0) && maxStack > 0 && nutWasShot == false && held == true && !EventSystem.current.IsPointerOverGameObject ()) {

				Debug.Log ("what is wrong");
				rd.isKinematic = false;	
				rd.GetComponent<PolygonCollider2D> ().enabled = true;
				rd.transform.SetParent (GameObject.Find ("Shot").transform);
				rd.velocity = rd.transform.TransformDirection (Vector3.right * velocity);
				//rd.velocity = rd.transform.TransformVector (new Vector3 (1f, 0f, 0f) * velocity);
				held = false;
				nutWasShot = true;
				maxStack--;

			}
		}
	}
	IEnumerator projectileInitiate ()
	{	
		//generate new projectile
		yield return new WaitForSeconds (1f);
		rd = (Rigidbody2D)Instantiate (rd, transform.position, transform.rotation);
		rd.GetComponent<PolygonCollider2D> ().enabled = false;
		rd.transform.parent = transform;
		rd.transform.localPosition = new Vector3 (1.4f, 0f, 0f);
		rd.transform.localScale = new Vector3 (1f, 1f, 1f);
		rd.isKinematic = true;

	}


}
