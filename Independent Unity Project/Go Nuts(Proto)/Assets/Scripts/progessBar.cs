using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class progessBar : MonoBehaviour
{

	Image image;
	private bool shot;

	// Use this for initialization
	void Start ()
	{
		image = gameObject.GetComponent<Image> ();
		shot = false;
	}
	
	// Update is called once per frame
	void Update ()
	{

		//fillamout Growing should be controllable


		if (Input.GetMouseButton (0)) {
			shot = true;
		}

		if (projectileShooting.held == true) {
			shot = false;
		}

		if (shot == false) {
			if (Input.GetKey (KeyCode.W)) {
				image.fillAmount = Mathf.MoveTowards (image.fillAmount, 1, Time.deltaTime * 0.5f);
			} else if (Input.GetKey (KeyCode.S)) {
				image.fillAmount = Mathf.MoveTowards (image.fillAmount, 0, Time.deltaTime * 0.5f);
			}
		}
	}

	public float getfillAmount ()
	{

		return image.fillAmount;
	}
}
