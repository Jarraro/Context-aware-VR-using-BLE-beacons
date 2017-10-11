package com.jarrar.beaconplugin;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jarrar.beaconlibrary.BeaconServiceNative;
import com.jarrar.beaconlibrary.BeaconServicePhy;

/**
 * Created by Jarrar on 16-Nov-16.
 */

public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        locationPermission();
        enableBT();

        Button buttonScanNative = (Button) findViewById(R.id.buttonScanNative);
        Button buttonScanPhy = (Button) findViewById(R.id.buttonScanPhy);
        Button buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonScanNative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNativeService();
                startVR();
            }
        });

        buttonScanPhy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPHYService();
                startVR();
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, BeaconServicePhy.class));
                stopService(new Intent(MainActivity.this, BeaconServiceNative.class));
            }
        });
    }

    /**
     * Initializes Bluetooth adapter
     * Ensures Bluetooth is available on the device and it is enabled.
     * If not, display a dialog requesting user permission to enable Bluetooth.
     */
    public void enableBT() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            mBluetoothAdapter = bluetoothManager.getAdapter();
        }
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 4341;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Start the scanning service
     * using PHY SDK
     */
    private void startPHYService() {
        stopService(new Intent(this, BeaconServiceNative.class));
        startService(new Intent(this, BeaconServicePhy.class));
        Toast.makeText(this, "PHY service started", Toast.LENGTH_LONG).show();
    }

    private void startNativeService() {
        stopService(new Intent(this, BeaconServicePhy.class));
        startService(new Intent(this, BeaconServiceNative.class));
        Toast.makeText(this, "Native service started", Toast.LENGTH_LONG).show();
    }

    /**
     * Start the sample VR app
     */
    private void startVR() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.Jarrar.VR");
        if (launchIntent != null) {
            startActivity(launchIntent);//null pointer check in case package name was not found
        }
    }

    /**
     * Ask for location permission
     */
    private void locationPermission() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Permission granted to access location", Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(MainActivity.this, "Permission denied to access location", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
