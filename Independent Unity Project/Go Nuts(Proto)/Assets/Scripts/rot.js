#pragma strict

var mouse_pos:Vector3;
var target:Transform;
var object_pos:Vector3;
static var mouseAngle:float;


function Start () {

}

function Update () {
	        //follow mouse
        mouse_pos = Input.mousePosition;
        mouse_pos.z = 0; //The distance between the camera and object
        object_pos = Camera.main.WorldToScreenPoint(target.position);
        mouse_pos.x = mouse_pos.x - object_pos.x;
        mouse_pos.y = mouse_pos.y - object_pos.y;
        mouseAngle = Mathf.Atan2(mouse_pos.y, mouse_pos.x) * Mathf.Rad2Deg;
        transform.rotation = Quaternion.Euler(Vector3(0, 0, mouseAngle));
 
 		/*
        transform.position.x=left_hand.position.x;
        transform.position.y=left_hand.position.y;
        transform.position.z=left_hand.position.z;
		*/
}