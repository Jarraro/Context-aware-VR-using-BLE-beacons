package com.jarrar.beaconreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Created by Jarrar on 16-Nov-16.
 */

public class MyReceiver extends BroadcastReceiver {
    public static int size = 0;
    public static String type = "";

    /**
     * Static variables to hold beacons' data
     * for the VR app to access them
     * TODO: replace w/ beacon object
     */

    public static String beacon1_address = "";
    public static double beacon1_distance = 0;
    public static int beacon1_tx = 0;
    public static int beacon1_rssi = 0;

    public static String beacon2_address = "";
    public static double beacon2_distance = 0;
    public static int beacon2_tx = 0;
    public static int beacon2_rssi = 0;

    public static String beacon3_address = "";
    public static double beacon3_distance = 0;
    public static int beacon3_tx = 0;
    public static int beacon3_rssi = 0;

    public static String beacon4_address = "";
    public static double beacon4_distance = 0;
    public static int beacon4_tx = 0;
    public static int beacon4_rssi = 0;

    public static String beacon5_address = "";
    public static double beacon5_distance = 0;
    public static int beacon5_tx = 0;
    public static int beacon5_rssi = 0;

    public static ArrayList<Beacon> beaconsList;
    private static MyReceiver instance;

    /**
     * static method to create our receiver object
     * it'll be Unity that will create our receiver object
     */
    public static void createInstance() {
        beaconsList = new ArrayList<>();
        if (instance == null) {
            instance = new MyReceiver();
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        type = intent.getStringExtra("com.jarrar.beaconlibrary.type");
        beaconsList = intent.getParcelableArrayListExtra("com.jarrar.beaconlibrary.beaconsList");
        size = intent.getIntExtra("com.jarrar.beaconlibrary.beaconsSize", 0);
        if (beaconsList != null) {
            switch (size) {
                case 0:
                    break;
                case 1:
                    setBeacon1();
                    break;
                case 2:
                    setBeacon1();
                    setBeacon2();
                    break;
                case 3:
                    setBeacon1();
                    setBeacon2();
                    setBeacon3();
                    break;
                case 4:
                    setBeacon1();
                    setBeacon2();
                    setBeacon3();
                    setBeacon4();
                    break;
                case 5:
                    setBeacon1();
                    setBeacon2();
                    setBeacon3();
                    setBeacon4();
                    setBeacon5();
                    break;
            }
        }
    }

    private void setBeacon1() {
        beacon1_address = beaconsList.get(0).address;
        beacon1_distance = beaconsList.get(0).distance;
        beacon1_tx = beaconsList.get(0).txPower;
        beacon1_rssi = beaconsList.get(0).rssi;
    }

    private void setBeacon2() {
        beacon2_address = beaconsList.get(1).address;
        beacon2_distance = beaconsList.get(1).distance;
        beacon2_tx = beaconsList.get(1).txPower;
        beacon2_rssi = beaconsList.get(1).rssi;
    }

    private void setBeacon3() {
        beacon3_address = beaconsList.get(2).address;
        beacon3_distance = beaconsList.get(2).distance;
        beacon3_tx = beaconsList.get(2).txPower;
        beacon3_rssi = beaconsList.get(2).rssi;
    }

    private void setBeacon4() {
        beacon4_address = beaconsList.get(3).address;
        beacon4_distance = beaconsList.get(3).distance;
        beacon4_tx = beaconsList.get(3).txPower;
        beacon4_rssi = beaconsList.get(3).rssi;
    }

    private void setBeacon5() {
        beacon5_address = beaconsList.get(4).address;
        beacon5_distance = beaconsList.get(4).distance;
        beacon5_tx = beaconsList.get(4).txPower;
        beacon5_rssi = beaconsList.get(4).rssi;
    }
}