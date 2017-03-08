package com.example.kevinwu.maze_navigation.services;

import android.app.Service;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.kevinwu.maze_navigation.activities.Connection;
import com.example.kevinwu.maze_navigation.models.AcceptThread;
import com.example.kevinwu.maze_navigation.models.ConnectedThread;
import com.example.kevinwu.maze_navigation.models.PlayerInfo;
import com.example.kevinwu.maze_navigation.utils.ConnectionUtils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kevin on 2/20/2017.
 */

public class BluetoothService extends Service {

    private PlayerInfo currPlayer;
    private ConnectedThread connectionThread;
    private ConnectedThread serverThread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("KEVIN", "Service is instantiated");

        currPlayer = null;

        // before connecting always cancel discovery to speed up things
        Connection.BA.cancelDiscovery();

        // check if the socket is connected, if not connect now
        try {
            if (!Connection.serverSocket.isConnected()) {
                Connection.serverSocket.connect();
            }
            // send the message through the socket
            // create a new thread to do this
            serverThread = new ConnectedThread(Connection.serverSocket);
            // write will write out the message to the output stream
            serverThread.start();
        } catch (IOException e){
            Log.d("Kevin", "Server Socket cannot connect");
        }

        try {
            if (!Connection.clientSocket.isConnected()) {
                Connection.clientSocket.connect();
            }
            // send the message through the socket
            // create a new thread to do this
            connectionThread = new ConnectedThread(Connection.clientSocket);
            // write will write out the message to the output stream
            connectionThread.start();
        } catch (IOException e){
            Log.d("Kevin", "Client Socket cannot connect");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // fill the message from the data received from GameView.
        String message = "";
        String direction = "";

        Bundle data = intent.getExtras();
        if (data != null) {
            String tmp = data.getString("Direction");
            if (tmp != null) {
                direction = tmp;
            }

            currPlayer = data.getParcelable("PlayerInfo");
            if (currPlayer != null) {
                message += currPlayer.getPlayerMazeNum() + "," + currPlayer.getPlayerX() + "," +
                        currPlayer.getPlayerY() + "," + direction + ",";
            }
        }

        if (connectionThread != null) {
            connectionThread.write(message.getBytes());
        }

        if (serverThread != null) {
            serverThread.write(message.getBytes());
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        //stop the threads
        connectionThread.cancel();
        serverThread.cancel();
        super.onDestroy();
    }

}
