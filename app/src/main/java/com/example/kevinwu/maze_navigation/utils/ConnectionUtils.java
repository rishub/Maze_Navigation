package com.example.kevinwu.maze_navigation.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.kevinwu.maze_navigation.activities.Connection;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Kevin on 2/20/2017.
 */

public class ConnectionUtils {
    public static void unpairDevice(BluetoothDevice device) {
        try {
            Method m = device.getClass()
                    .getMethod("removeBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Log.e("KEVIN", e.getMessage());
        }
    }

    public static void connectDevice(String address) {
        BluetoothDevice device = Connection.BA.getRemoteDevice(address);
        BluetoothSocket tmp = null;
        UUID MY_UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB");
        // Get a BluetoothSocket for a connection with the
        // given BluetoothDevice
        try {
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
            tmp = (BluetoothSocket) m.invoke(device, 1);
        } catch (IOException e) {
            Log.d("KEVIN", "create() failed", e);
        } catch (Exception e) {
            Log.d("KEVIN", "failed to do reflection");
        }
        Connection.deviceConnection = tmp;
        try {
            Connection.deviceConnection.connect();
        } catch(Exception e) {
            Log.d("KEVIN", "failed to make connection with device");
        }
    }
}
