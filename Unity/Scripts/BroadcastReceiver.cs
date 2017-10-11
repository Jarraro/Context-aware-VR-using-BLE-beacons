using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class BroadcastReceiver : MonoBehaviour
{

	public static AndroidJavaClass jc;
	// Use this for initialization
	void Start ()
	{
		// Access the android receiver
		jc = new AndroidJavaClass ("com.jarrar.beaconreceiver.MyReceiver");
		// Call java function to create MyReceiver java object
		jc.CallStatic ("createInstance");
		// get data to test
		InvokeRepeating("getSampleData", 2.0f, 2.0f);
	}

	void getSampleData() {
		Debug.Log (getBeaconsCount ().ToString () + " Beacons in range.");
		Debug.Log (getScanServiceType() + " service is active.");
		Debug.Log (getBeaconAddress(1) + " is " + getBeaconDistance(1) + " away.");
	}

	public static int getBeaconsCount ()
	{
		// to get the beacons count
		return jc.GetStatic<int> ("size");
	}

	public static string getScanServiceType ()
	{
		// returns service type "phy/native"
		return jc.GetStatic<string> ("type");
	}

	public static string getBeaconAddress (int n)
	{
		return jc.GetStatic<string> ("beacon" + n.ToString () + "_address");
	}

	public static int getBeaconRssi (int n)
	{
		return jc.GetStatic<int> ("beacon" + n.ToString () + "_rssi");
	}

	public static int getBeaconTx (int n)
	{
		return jc.GetStatic<int> ("beacon" + n.ToString () + "_tx");
	}

	public static float getBeaconDistance (int n)
	{
		return (float)jc.GetStatic<double> ("beacon" + n.ToString () + "_distance");
	}
}