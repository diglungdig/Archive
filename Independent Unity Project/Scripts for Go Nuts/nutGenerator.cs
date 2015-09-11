using UnityEngine;
using System.Collections;

public class nutGenerator : MonoBehaviour
{
	public Transform origin;
	public Rigidbody2D rd;
	public int maxNum;
	public int i;
	private bool done;
	// Use this for initialization
	void Start ()
	{
		i = 0;
		done = true;
	}
	// Update is called once per frame
	void Update ()
	{

		if (i < maxNum && done == true) {
			done = false;
			StartCoroutine ("nutGenerate");
			i++;
		}
	}



	IEnumerator nutGenerate ()
	{
		yield return new WaitForSeconds (1f);
		rd = (Rigidbody2D)Instantiate (rd, transform.position, transform.rotation);
		rd.GetComponent<PolygonCollider2D> ().enabled = true;
		rd.isKinematic = false;
		done = true;
		//yield return new WaitForSeconds (1f);
	}

}
