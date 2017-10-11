package com.jarrar.beaconlibrary;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jarrar.beaconreceiver.Beacon;

import java.util.ArrayList;

/**
 * Created by Jarrar on 20-Nov-16.
 */

public class BeaconServiceNative extends Service {
    public String TAG = this.getClass().getSimpleName();

    /**
     * N is a variable used in calculating distance
     * see {@link #calculateDistance_1(int rssi, int tx)}
     * range: 2-4
     * 2-2.5 is the most common
     */
    public final int N = 2;

    /**
     * Depending on the beacon's config, TX_default value is:
     * 4 dbm = -16
     * 0 dbm = -21
     * -04 dbm = -31
     * -08 dbm = -35
     * -12 dbm = -39
     * -16 dbm = -43
     * -20 dbm = -47
     * -30 dbm = -51
     * or connect to GATT server
     */
    int temp_tx = -16;

    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler = new Handler();
    private ArrayList<Beacon> beacons = new ArrayList<>();
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    if (scanRecord[7] == 0x02 && scanRecord[8] == 0x15) { // iBeacon indicator
                        byte tx = scanRecord[29];
                        temp_tx = (int) tx;
                    }
                    updateBeacon(device, rssi, temp_tx);
                    // sendIntent is the object that will be broadcast outside our app
                    Intent sendIntent = new Intent();
                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_FROM_BACKGROUND | Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
                    sendIntent.setAction("com.jarrar.beaconlibrary");
                    sendIntent.putExtra("com.jarrar.beaconlibrary.type", "native");
                    sendIntent.putExtra("com.jarrar.beaconlibrary.beaconsSize", beacons.size());
                    sendIntent.putExtra("com.jarrar.beaconlibrary.beaconsList", beacons);
                    sendBroadcast(sendIntent);
                }
            };

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            buffer.append(String.format("%02x", bytes[i]));
        }
        return buffer.toString();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        scanLeDevice(true);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    private void updateBeacon(BluetoothDevice device, int rssi, int tx) {
        boolean exists = false;
        for (Beacon beacon :
                beacons) {
            // update rssi, tx and distance
            if (beacon.address.equals(device.getAddress())) {
                beacon.rssi = rssi;
                beacon.txPower = tx;
                beacon.distance = calculateDistance_2(rssi, tx);
                Log.d("Native distance", String.valueOf(beacon.distance));
                exists = true;
            }
        }
        // add if doesn't exist
        if (!exists) {
            Beacon tempBeacon = new Beacon();
            tempBeacon.setAddress(device.getAddress());
            tempBeacon.setRssi(rssi);
            tempBeacon.setDistance(calculateDistance_2(rssi, tx));
            //tempBeacon.setUrl(" ");
            tempBeacon.setTxPower(tx);
            beacons.add(tempBeacon);
        }

    }

    public double calculateDistance_1(int rssi, int tx) {
        return (10 ^ (tx - rssi / (10 * N)));
    }

    public double calculateDistance_2(int rssi, int tx) {
        double ratio = rssi * (1.0 / tx);
        return 0.89976 * (Math.pow(ratio, 7.7095)) + 0.111;
    }

    @Override
    public void onDestroy() {
        scanLeDevice(false);
        super.onDestroy();
    }
}