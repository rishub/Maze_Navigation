package com.example.kevinwu.maze_navigation.models;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.kevinwu.maze_navigation.activities.Connection;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Kevin on 3/7/2017.
 */

public class AcceptThread extends Thread {
    private final BluetoothServerSocket mmServerSocket;

    final private String TAG = "Accept Thread";
    final private String NAME = "Server";

    public AcceptThread(UUID uuid) {
        // Use a temporary object that is later assigned to mmServerSocket
        // because mmServerSocket is final.
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID is the app's UUID string, also used by the client code.
            tmp = Connection.BA.listenUsingRfcommWithServiceRecord(NAME, uuid);
        } catch (IOException e) {
            Log.e(TAG, "Socket's listen() method failed", e);
        }
        mmServerSocket = tmp;
    }

    public void run() {
        BluetoothSocket serverSocket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                serverSocket = mmServerSocket.accept();
                Log.d(TAG, "Listening");
            } catch (IOException e) {
                Log.d(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (serverSocket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.

                // stores the server socket for later use.
                //Connection.serverSockets.add(serverSocket);
                Connection.serverSocket = serverSocket;
                Log.d(TAG, "Connection accepted");
                try {
                    mmServerSocket.close();
                } catch (IOException e) {
                    Log.e(TAG, "Socket close method failed", e);
                }
                break;
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            mmServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }
}
