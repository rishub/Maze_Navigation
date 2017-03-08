package com.example.kevinwu.maze_navigation.models;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.kevinwu.maze_navigation.activities.Connection;
import com.example.kevinwu.maze_navigation.services.BluetoothService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import de.greenrobot.event.EventBus;

/**
 * Created by Kevin on 3/6/2017.
 */

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer; // mmBuffer store for the stream

    public ConnectedThread(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // check if the socket is connected, if not connect now
        try {
            if (!mmSocket.isConnected()) {
                if (!Connection.BA.isEnabled()) {
                    Connection.BA.enable();
                }
                mmSocket.connect();
            }
        } catch (IOException e) {
            Log.d("Kevin", "Error in trying to connect with the socket");
        }

        // Get the input and output streams; using temp objects because
        // member streams are final.
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {
            Log.d("Kevin", "Error occurred when creating input stream", e);
        }
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.d("Kevin", "Error occurred when creating output stream", e);
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        mmBuffer = new byte[1024];
        int numBytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs.
        while (true) {
            try {
                // Read from the InputStream.
                int available = mmInStream.available();
                if (available > 0) {
                    numBytes = mmInStream.read(mmBuffer);

                    String readMessage = new String(mmBuffer, 0, numBytes);
                    // send message through the event bus default object
                    EventBus.getDefault().post(new RemotePlayerMoveEvent(readMessage));

                }
            } catch (IOException e) {
                Log.d("Kevin", "Input stream was disconnected", e);
                break;
            }
        }
    }

    // Call this from the main activity to send data to the remote device.
    public void write(byte[] bytes) {
        try {
            mmOutStream.write(bytes);
        } catch (IOException e) {
            Log.e("Kevin", "Error occurred when sending data", e);
        }
    }

    // Call this method from the main activity to shut down the connection.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e("Kevin", "Could not close the connect socket", e);
        }
    }
}
