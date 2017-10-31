package com.example.bluetoothapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVERABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button connect_btn = (Button) findViewById(R.id.connect_btn);
        final Button discoverable_btn = (Button) findViewById(R.id.discoverable_btn);
        final Button off_btn = (Button) findViewById(R.id.off_btn);
        final Button paired_devices_btn = (Button) findViewById(R.id.paired_devices_btn);
        paired_devices_btn.setEnabled(false);

        final TextView info_tv = (TextView) findViewById(R.id.info_tv);

        final BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();

        if (bluetooth == null) {
            Toast.makeText(this, "Device doesn't support Bluetooth", Toast.LENGTH_LONG).show();
            info_tv.append("\nDevice doesn't support Bluetooth");
        } else {
            if(bluetooth.isEnabled()) {
                connect_btn.setText("GET INFO");
                paired_devices_btn.setEnabled(true);
            }
        }

        connect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bluetooth.isEnabled()) {
                    Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
                    info_tv.append("\nEnable Bluetooth");
                } else {

                    //bluetooth name
                    String myDeviceAddress = bluetooth.getAddress();
                    String myDeviceName = bluetooth.getName();
                    info_tv.append("\nDevice name : " + myDeviceName);
                    info_tv.append("\nDevice address : " + myDeviceAddress);

                    //get list of available devices
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mReceiver, filter);

                }
            }
        });

        discoverable_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!bluetooth.isDiscovering()) {
                    Toast.makeText(getApplicationContext(), "Making device discoverable", Toast.LENGTH_LONG).show();
                    info_tv.append("\nMaking device discoverable");

                    Intent discoverBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(discoverBluetooth, REQUEST_DISCOVERABLE_BT);
                }
            }
        });

        off_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetooth.disable();
                Toast.makeText(getApplicationContext(), "Turn off bluetooth", Toast.LENGTH_LONG).show();
                connect_btn.setText("TURN ON");
                info_tv.append("\nTurn off bluetooth");
                paired_devices_btn.setEnabled(false);
            }
        });

        paired_devices_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_tv.append("\n\nList of paired devices : ");
                Set<BluetoothDevice> devices = bluetooth.getBondedDevices();
                if (devices.size() > 0) {
                    for (BluetoothDevice device : devices) {
                        info_tv.append("\nDevice : " + device.getName() + " - " + device);
                    }
                } else {
                    info_tv.append("\nNo Paired Device");
                }
            }
        });

//        if (bluetooth != null) {
//            if (bluetooth.isEnabled()) {
//                String myDeviceAddress = bluetooth.getAddress();
//                String myDeviceName = bluetooth.getName();
//                status = myDeviceName + " : " + myDeviceAddress;
//            } else {
//                status = "Bluetooth is not enabled";
//            }
//        } else {
//            status = "This device doesn't support bluetooth";
//        }
//
//        Toast.makeText(this,status, Toast.LENGTH_LONG).show();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                String status = deviceName + " : " + deviceHardwareAddress;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
