using UnityEngine;
using System.Collections;

public class RotateAround : MonoBehaviour {

	public float rotationSpeed;

	// Use this for initialization
	void Start () {
	
	}
	
	// Update is called once per frame
	void Update () {
		this.transform.RotateAround(new Vector3(0.0f, -4.5f, 0.0f), Vector3.up, rotationSpeed*Time.deltaTime);
	}
}
