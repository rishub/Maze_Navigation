package com.example.kevinwu.maze_navigation.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.kevinwu.maze_navigation.activities.Connection;
import com.example.kevinwu.maze_navigation.models.AcceptThread;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Kevin on 2/20/2017.
 */

public class ConnectionUtils {

    // standard UUID for bluetooth connections
    public static UUID STANDARD_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("KEVIN", e.getMessage());
        }
    }

    // creates and connects a socket
    // also returns socket
    public static BluetoothSocket connectDevice(String address, Context context) {
        BluetoothDevice device = Connection.BA.getRemoteDevice(address);

        // Accepts a connection
        AcceptThread acceptThread = new AcceptThread(STANDARD_UUID);
        acceptThread.start();

        // Client code
        BluetoothSocket clientSocket = null;
        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            // must be secured (must be paired before connecting) otherwise it won't work
            clientSocket = device.createRfcommSocketToServiceRecord(STANDARD_UUID);
        } catch (IOException e) {
            Log.d("KEVIN", "create() failed", e);
        } catch (Exception e) {
            Log.d("KEVIN", "failed to do reflection");
        }

        // establish a connection
        try {
            clientSocket.connect();
            Toast.makeText(context, "Connected with " + device.getName(), Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            Log.d("KEVIN", "failed to make connection with device " + e.toString());
            Toast.makeText(context, "Failed to connect with " + device.getName(), Toast.LENGTH_SHORT).show();
        }

        Log.d("Kevin", "Bluetooth socket: " + clientSocket);

        return clientSocket;
    }
}
