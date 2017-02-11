package com.example.kevinwu.maze_navigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    public void startGame (View view) {
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }

    public void exitGame (View view) {
        finish();
    }
}


