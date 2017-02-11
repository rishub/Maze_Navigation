package com.example.kevinwu.maze_navigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Game extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_game);
//         Maze maze = MazeFactory.getMaze(1); // creates a maze instance
//        // depending on which part of the maze we are in we can create the maze
//        // we can have a 5x5 maze. we can have an enum struct
//        GameView view = new GameView(Game.this, maze);
//        setContentView(view);
    }
}
