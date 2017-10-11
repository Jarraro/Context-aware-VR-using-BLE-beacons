package com.jarrar.beaconlibrary;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.bkon.phy_sdk.PhyWebBeacon;
import com.bkon.phy_sdk.PhyWebManager;
import com.jarrar.beaconreceiver.Beacon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarrar on 16-Nov-16.
 */

public class BeaconServicePhy extends Service {

    // TODO: Add your own API-Key/APP-ID obtained from your phy.net account
    private static String mApiKey = "";
    private static String TAG = BeaconServicePhy.class.getSimpleName();
    private static ArrayList<PhyWebBeacon> phyWebBeacons;
    private final Handler handler = new Handler();
    private ArrayList<Beacon> mBeaconsList;
    private PhyWebManager mPhyWebManager;
    // the method which will be executed by the handler
    private Runnable sendData = new Runnable() {
        public void run() {
            /*
            sendIntent is the object that will be broadcast outside our app
            SetAction uses a string which is an important name as it identifies the sender of the intent and that we will give to the receiver to know what to listen.
            */
            Intent sendIntent = new Intent();
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            sendIntent.setAction("com.jarrar.beaconlibrary");
            int count = getBeaconsListSize();
            if (count > 0) {
                sendIntent.putExtra("com.jarrar.beaconlibrary.type", "phy");
                sendIntent.putExtra("com.jarrar.beaconlibrary.beaconsSize", count);
                mBeaconsList = getParcelList(count);
                sendIntent.putExtra("com.jarrar.beaconlibrary.beaconsList", mBeaconsList);
                sendBroadcast(sendIntent);
            }

            // In our case we run this method 300ms with postDelayed
            handler.removeCallbacks(this);
            handler.postDelayed(this, 300);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scanner();
        startScanning();
        // start the Handler after 1.5 seconds
        handler.removeCallbacks(sendData);
        handler.postDelayed(sendData, 1500);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void scanner() {
        mPhyWebManager = new PhyWebManager(this, mApiKey);
        phyWebBeacons = new ArrayList<>();
        mBeaconsList = new ArrayList<>();
    }

    public void startScanning() {
        mPhyWebManager.stopScanningForBeacons();
        mPhyWebManager.startScanningForBeacons();
        mPhyWebManager.setPhyWebListener(new PhyWebManager.OnPhyWebListener() {
            @Override
            public void onPhyWebBeaconsFound(List beacons) {
                phyWebBeacons.clear();
                phyWebBeacons.addAll(beacons);
            }

            @Override
            public void onRefreshBeaconData() {
                Log.d(TAG, "onRefreshBeaconData: ");
            }
        });

    }


    public void stopScanning() {
        mPhyWebManager.stopScanningForBeacons();
        Log.d(TAG, "stopScanning");
    }

    public int getBeaconsListSize() {
        if (phyWebBeacons != null)
            return phyWebBeacons.size();
        else return 0;
    }

    @Override
    public void onDestroy() {
        stopScanning();
        super.onDestroy();
    }

    public ArrayList<Beacon> getParcelList(int count) {
        ArrayList<Beacon> beacons = new ArrayList<>();
        Beacon tempBeacon = new Beacon();
        for (int i = 0; i < count; i++) {
            tempBeacon.setUrl(phyWebBeacons.get(i).scanUrl);
            tempBeacon.setAddress(phyWebBeacons.get(i).address);
            tempBeacon.setRssi(phyWebBeacons.get(i).rssi);
            tempBeacon.setTxPower(phyWebBeacons.get(i).txPower);
            tempBeacon.setDistance(phyWebBeacons.get(i).distance);
            beacons.add(tempBeacon);
        }
        return beacons;
    }
}