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
import com.example.kevinwu.maze_navigation.models.PlayerInfo;
import com.example.kevinwu.maze_navigation.activities.Game;
import com.example.kevinwu.maze_navigation.utils.ConnectionUtils;
import com.example.kevinwu.maze_navigation.views.GameView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kevin on 2/20/2017.
 */

public class BluetoothService extends Service {

    private PlayerInfo currPlayer;
    private ArrayList<BluetoothSocket> connectedSockets;
    private PlayerInfo mockPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connectedSockets = new ArrayList<>();
        Log.d("KEVIN", "Service is instantiated");
        for(int i = 0; i < Connection.connectedDevices.size(); i++)
        {
           Log.d("Kevin", "Connected Device " + Connection.connectedDevices.get(i));
            connectedSockets.add(Connection.connectedDevices.get(i));
        }
        currPlayer = null;
        mockPlayer = new PlayerInfo(0, 0, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle data = intent.getExtras();
        // currPlayer data
        if(data != null) {
            currPlayer = data.getParcelable("PlayerInfo");
            if(currPlayer != null) {
                Log.d("Kevin", "Maze: " + currPlayer.getPlayerMazeNum() +
                        " Player X " + currPlayer.getPlayerX() +
                        " Player Y " + currPlayer.getPlayerY());
            }
        }

        //send currPlayer data through socket
        try {
            for(int i = 0; i < connectedSockets.size(); i++){
                connectedSockets.get(i).connect();
            }
        }catch(Exception e) {
            Log.d("Kevin", "Socket connection problem");
        }

        //get otherPlayer data from socket

        //send otherPlayer info back to maze
        Intent intent1 = new Intent("MockPlayer");
        intent1.putExtra("MockPlayerInfo", mockPlayer);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent1);
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
