package com.termux.api;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.JsonWriter;
import android.util.Log;

import com.termux.api.util.BluetoothChatService;
import com.termux.api.util.Constants;
import com.termux.api.util.ResultReturner;
import com.termux.api.util.TermuxApiLogger;

import java.io.PrintWriter;

/**
 * Created by raditaliem on 26/11/2017.
 */

public class BluetoothConnectionAPI {

    private static String message = "";
    private static String recipient = "";
    private static TermuxApiReceiver termuxBluetoothApiReceiver = null;
    private static Intent termuxBluetoothIntent = null;

    /**
     * Local Bluetooth adapter
     */
    private static BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    /**
     * String buffer for outgoing messages
     */
    private static StringBuffer mOutStringBuffer;

    /**
     * Member object for the chat services
     */
    private static BluetoothChatService mChatService = null;

    public static void onReceive(TermuxApiReceiver apiReceiver, Context context, Intent intent) {

        termuxBluetoothApiReceiver = apiReceiver;
        termuxBluetoothIntent = intent;

        // Get local Bluetooth adapter

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(context, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

        Log.d("BluetoothSend","inititate chat service");
        String[] recipients = intent.getStringArrayExtra("recipients");

        if (recipients == null) {
            // Used by old versions of termux-send-sms.
            recipient = intent.getStringExtra("recipient");
            if (recipient != null) recipients = new String[]{recipient};
        }

        if (recipients == null || recipients.length == 0) {
            TermuxApiLogger.error("No recipient given");
        } else {
            recipient = recipients[0];
            Log.d("Recipient", recipient);

            connectDevice(recipient, true);
        }

    }

    public static void onSend(TermuxApiReceiver apiReceiver, Context context, Intent intent) {
        termuxBluetoothApiReceiver = apiReceiver;
        termuxBluetoothIntent = intent;

        // Get local Bluetooth adapter

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothChatService(context, mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");

        Log.d("BluetoothSend","inititate chat service");
        String[] recipients = intent.getStringArrayExtra("recipients");

        if (recipients == null) {
            // Used by old versions of termux-send-sms.
            recipient = intent.getStringExtra("recipient");
            if (recipient != null) recipients = new String[]{recipient};
        }

        if (recipients == null || recipients.length == 0) {
            TermuxApiLogger.error("No recipient given");
        } else {
            recipient = recipients[0];
            Log.d("Recipient", recipient);

            connectDevice(recipient, true);
            ResultReturner.returnData(apiReceiver, intent, new ResultReturner.WithStringInput() {
                @Override
                public void writeResult(PrintWriter out) throws Exception {
                    Log.d("inputString", inputString);
                    message = inputString;
                }
            });
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private static void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            //Toast.makeText(getActivity(), R.string.not_connected, Toast.LENGTH_SHORT).show();
            Log.d("getState", "not connected");
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * Establish connection with other device
     */
    private static void connectDevice(String address, boolean secure) {
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private static final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("mHandler","Something is passing through me");
            Log.d("msg.what", Integer.toString(msg.what));

            //FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    Log.d("msg.arg1", Integer.toString(msg.arg1));
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            BluetoothConnectionAPI.sendMessage(message);
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            //setStatus(R.string.title_not_connected);
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    final String writeMessage = new String(writeBuf);
                    //mConversationArrayAdapter.add("Me:  " + writeMessage);

                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    final String readMessage = new String(readBuf, 0, msg.arg1);
                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);

                    ResultReturner.returnData(termuxBluetoothApiReceiver, termuxBluetoothIntent, new ResultReturner.ResultJsonWriter() {
                        @Override
                        public void writeJson(JsonWriter out) throws Exception {
                            out.beginObject();
                            out.name(recipient).value(readMessage);
                            out.endObject();
                        }
                    });

                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    //mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    if (null != activity) {
//                        Toast.makeText(activity, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    }
                    break;
                case Constants.MESSAGE_TOAST:
//                    if (null != activity) {
//                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    }
                    break;
            }
        }
    };


}
