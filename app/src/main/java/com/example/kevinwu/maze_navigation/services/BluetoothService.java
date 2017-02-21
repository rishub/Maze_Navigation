package com.example.kevinwu.maze_navigation.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.kevinwu.maze_navigation.models.PlayerInfo;
import com.example.kevinwu.maze_navigation.activities.Game;
import com.example.kevinwu.maze_navigation.views.GameView;

/**
 * Created by Kevin on 2/20/2017.
 */

public class BluetoothService extends Service {

    private PlayerInfo currPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
}
