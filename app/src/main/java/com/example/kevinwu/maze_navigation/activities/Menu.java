package com.example.kevinwu.maze_navigation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.kevinwu.maze_navigation.R;
import com.example.kevinwu.maze_navigation.activities.Connection;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void connect (View view) {
        Intent intent = new Intent(this, Connection.class);
        startActivity(intent);
    }

    public void exitGame (View view) {
        finish();
    }
}


