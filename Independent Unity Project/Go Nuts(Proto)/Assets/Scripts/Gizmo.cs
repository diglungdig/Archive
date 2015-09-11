using UnityEngine;
using System.Collections;

public class Gizmo : MonoBehaviour {
	public float size = 0.75f;

	void OnDrawGizmos()
	{
		Gizmos.color = Color.yellow;
		Gizmos.DrawWireSphere (transform.position, size);

	}
}
