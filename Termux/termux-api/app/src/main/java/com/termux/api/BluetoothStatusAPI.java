package com.termux.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.util.JsonWriter;
import android.util.Log;

import com.termux.api.util.ResultReturner;

import java.util.Set;

public class BluetoothAPI {
    static void onReceive(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {
            @Override
            public void writeJson(JsonWriter out) throws Exception {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                BluetoothAvailable bluetoothAvailable = new BluetoothAvailable(bluetoothAdapter);
                final String actionExtra = intent.getStringExtra("action");

                Log.d("API", actionExtra);

                if (actionExtra == null) {

                    out.name(bluetoothAvailable.getTag()).value(bluetoothAvailable.getMessage());

                } else {
                    if (bluetoothAvailable.getStatus()) {

                        if (actionExtra == "status") {
                            out.name(bluetoothAvailable.getTag()).value(bluetoothAvailable.getMessage());
                            out.name("BluetoothEnabled").value(bluetoothAvailable.getEnabledMessage());

                            if (bluetoothAvailable.getEnabledStatus()) {
                                out.name("DeviceName").value(bluetoothAvailable.getDeviceName());
                                out.name("DeviceAddress").value(bluetoothAvailable.getDeviceAddress());
                            }

                        } else if (actionExtra == "pair") {

                            if (bluetoothAvailable.getEnabledStatus()) {
                                for (BluetoothDevice device : bluetoothAvailable.getDevices()) {
                                    out.name(device.getName()).value(device.getAddress());
                                }
                            }

                        } else if (actionExtra == "activate") {
                            //TODO
                        } else if (actionExtra == "deactivate") {
                            //TODO
                        } else if (actionExtra == "send") {
                            //TODO
                        } else {
                            out.name("Error").value("Action unknown");
                        }

                    } else {
                        out.name(bluetoothAvailable.getTag()).value(bluetoothAvailable.getMessage());
                    }
                }
                out.endObject();


            }
        });
    }


    private static class BluetoothAvailable {
        private String tag = "BluetoothAvailability";
        private String message = "Device doesn't support Bluetooth";
        private Boolean status = false;
        private Boolean isEnabled = false;
        private String enabledMessage = "Bluetooth is not enabled";
        private String deviceAddress = "";
        private String deviceName = "";
        private Set<BluetoothDevice> devices = null;

        public BluetoothAvailable(BluetoothAdapter bluetooth) {
            if (bluetooth != null) {
                this.message = "Device supports Bluetooth";
                this.status = true;
                this.isEnabled = bluetooth.isEnabled();
            }

            if (bluetooth.isEnabled()) {
                this.enabledMessage = "Bluetooth is enabled";
                this.deviceAddress = bluetooth.getAddress();
                this.deviceName = bluetooth.getName();
                this.devices = bluetooth.getBondedDevices();
            }
        }

        public String getTag() {
            return this.tag;
        }

        public String getMessage() {
            return this.message;
        }

        public Boolean getStatus() {
            return this.status;
        }

        public Boolean getEnabledStatus() {
            return this.isEnabled;
        }

        public String getEnabledMessage() {
            return this.enabledMessage;
        }

        public String getDeviceAddress() {
            return this.deviceAddress;
        }

        public String getDeviceName() {
            return this.deviceName;
        }

        public Set<BluetoothDevice> getDevices() {
            return this.devices;
        }
    }

    private class BluetoothActive {

    }
}
