package mx.edu.cicese.alejandro.voicehelper;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by UCI on 4/12/15.
 */
public class BluetoothRunnable implements Runnable {
    public static final int READY_TO_CONN =0;
    public static final int CANCEL_CONN =1;
    public static final int MESSAGE_READ =2;
    UUID[] uuids = new UUID[2];

    private static String LOG_TAG = "Bluetooth";
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    BluetoothAdapter myBt;
    Handler handle;

    public BluetoothRunnable(BluetoothDevice device) {
        Log.e(LOG_TAG, "ConnectThread start....");
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {

            // this seems to work on the note3...
            // you can remove the Insecure if you want to...
            tmp = device.createInsecureRfcommSocketToServiceRecord(uuids[0]);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Danger Will Robinson");
            e.printStackTrace();
        }
        mmSocket = tmp;
    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        myBt.cancelDiscovery();
        Log.e(LOG_TAG, "stopping discovery");

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            Log.e(LOG_TAG, "connecting!");

            mmSocket.connect();
        } catch (IOException connectException) {

            Log.e(LOG_TAG, "failed to connect");

            // Unable to connect; close the socket and get out
            try {
                Log.e(LOG_TAG, "close-ah-da-socket");

                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(LOG_TAG, "failed to close hte socket");

            }
            Log.e(LOG_TAG, "returning..");

            return;
        }

        Log.e(LOG_TAG, "we can now manage our connection!");

        // Do work to manage the connection (in a separate thread)
        manageConnectedSocket(mmSocket);
    }

    /**
     * Will cancel an in-progress connection, and close the socket
     */
    public void cancel() {
        try {
            mmSocket.close();
            Message msg = handle.obtainMessage(READY_TO_CONN);
            handle.sendMessage(msg);

        } catch (IOException e) {
        }
    }

    public void manageConnectedSocket(BluetoothSocket mmSocket) {
        ConnectedThread t = new ConnectedThread(mmSocket);
        t.start();
// manage your socket... I'll probably do a lot of the boiler plate here later
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(LOG_TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(LOG_TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(LOG_TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    //                  byte[] blah = ("System Time:" +System.currentTimeMillis()).getBytes();
                    //                  write(blah);
                    //                  Thread.sleep(1000);
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI Activity
                    handle.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                            .sendToTarget();

                    //                  .sendToTarget();
                } catch (Exception e) {
                    Log.e(LOG_TAG, "disconnected", e);
                    connectionLost();
                    //                  break;
                }
            }
        }

        public void connectionLost() {
            Message msg = handle.obtainMessage(CANCEL_CONN);
            //          Bundle bundle = new Bundle();
            //          bundle.putString("NAMES", devs);
            //          msg.setData(bundle);

            handle.sendMessage(msg);

        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);

                // Share the sent message back to the UI Activity
                //              mHandler.obtainMessage(BluetoothChat.MESSAGE_WRITE, -1, -1, buffer)
                //              .sendToTarget();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "close() of connect socket failed", e);
            }
        }
    }
}

