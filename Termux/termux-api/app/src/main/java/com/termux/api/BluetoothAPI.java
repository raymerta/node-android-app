package com.termux.api;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.util.JsonWriter;

import com.termux.api.util.ResultReturner;

public class BluetoothAPI {
    static void onReceive(TermuxApiReceiver apiReceiver, final Context context, final Intent intent) {
        ResultReturner.returnData(apiReceiver, intent, new ResultReturner.ResultJsonWriter() {
            @Override
            public void writeJson(JsonWriter out) throws Exception {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (bluetoothAdapter != null) {
                    out.beginObject();
                    out.name("BluetoothStatus").value("It has bluetooth");
                    out.endObject();
                } else {
                    out.beginObject();
                    out.name("BluetoothStatus").value("It doesn't have bluetooth");
                    out.endObject();
                }
            }
        });
    }
}
