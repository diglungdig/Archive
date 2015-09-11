using UnityEngine;
using System.Collections;

public class RandomExplosion : MonoBehaviour {
	
	Animator animator;
	
	float startTime;
	
	bool isExploding = false;
	
	// Use this for initialization
	void Start () {
		animator = GetComponent<Animator>();
		transform.localScale = transform.localScale * Random.Range (0.6f, 1.2f);
		startTime = Random.Range(0,1.5f);
	}
	
	// Update is called once per frame
	void Update () {
		if(Time.time > startTime && !isExploding){
			animator.SetTrigger("Explode");
			isExploding = true;
		}
	}
}
