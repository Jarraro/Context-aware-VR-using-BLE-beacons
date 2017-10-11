using UnityEngine;
using System.Collections;

public class MoveObject : MonoBehaviour
{

	string type;
	// Use this for initialization
	void Start ()
	{
		type = BroadcastReceiver.getScanServiceType ();
		InvokeRepeating ("moveObject", 3.0f, 1.0f);

	}
	
	// Update is called once per frame
	void Update ()
	{
		
	}

	void moveObject ()
	{
			transform.position = new Vector3(0,1,BroadcastReceiver.getBeaconDistance(1));
	}
}
