using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class uiControl : MonoBehaviour
{
	public Vector3[] posArray;
	public Camera mainCam;
	public float cameraMoveSpeed;
	public Button[] buttonArray;

	private GameObject mainMenu;

	private bool start;
	private bool mapselected;
	private bool backhit;
	private bool settinghit;
	private bool gotNutsHit;


	private int i;
	private bool transformFinished;
	private AsyncOperation async;
	
		// Use this for initialization
	void Start ()
	{
	
		mainMenu = GameObject.Find ("Canvas");
		i = 1;
	}
	
	// Update is called once per frame
	void Update ()
	{

		if (i == 1) {
			mainMenu.SetActive (true);
		}

		//player hits start button
		if (start == true) {

			mainMenu.SetActive (false);
			playerHitStart (moveCamera (i + 1));

		} else if (mapselected == true) {
	
			playerHitmap (moveCamera (i + 1));
	
		} else if (backhit == true) {

			if (i == 0) {
				playerHitback (moveCamera (i + 1));
			} else {
				playerHitback (moveCamera (i - 1));
			}
		} else if (settinghit == true) {
			mainMenu.SetActive (false);
			playerHitSetting (moveCamera (i - 1));
		} else if (gotNutsHit == true) {

			//add roading screen
			StartCoroutine(startloading());


		}



	}
	IEnumerator startloading(){
		StartCoroutine(Wait233 ());
		Debug.LogError ("sucker!!!!!!!!");
		async.allowSceneActivation = true;
		yield return null;
	}
	IEnumerator Wait233(){
		GameObject.Find("loadingScreen").GetComponent<Image>().enabled = true;
		GameObject.Find("loadingScreen").GetComponent<Animator>().SetBool("start!", true);
		GameObject.Find("loadingText").GetComponent<Text>().enabled = true;
		async = Application.LoadLevelAsync(1);
		async.allowSceneActivation = false;

		yield return new WaitForSeconds (2);

		Debug.LogError ("sucker");

	}


	//methods that interact with buttons
	public void playerHitStart (bool hitStartOrNot)
	{
		start = hitStartOrNot;
	}
	public void playerHitmap (bool hitMapOrNot)
	{
		mapselected = hitMapOrNot;
	}
	public void playerHitback (bool hitBackOrNot)
	{

		backhit = hitBackOrNot;
	}
	public void playerHitSetting (bool hitSettingOrNot)
	{
		settinghit = hitSettingOrNot;

	}
	public void playerHitGoNuts (bool hitGoNutsOrNot)
	{

		gotNutsHit = hitGoNutsOrNot;
	}




	bool moveCamera (int index)
	{	
		Debug.Log (index);
		if (Vector3.Distance (mainCam.transform.position, posArray [index]) > 0.1) {
			mainCam.transform.position = Vector3.Lerp (mainCam.transform.position, posArray [index], cameraMoveSpeed * Time.deltaTime);
			foreach(Button a in buttonArray){
				a.interactable = false;
			}
			return true;
		} else {
			//camera reach the canvas, update the index
			i = index;
			foreach(Button a in buttonArray){
				a.interactable = true;
			}
			return false;
		}
	}



}
