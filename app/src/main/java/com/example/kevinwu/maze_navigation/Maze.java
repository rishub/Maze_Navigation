package com.example.kevinwu.maze_navigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Maze extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maze);

        // Maze maze = MazeCreator.getMaze(beginning); // creates a maze instance
        // depending on which part of the maze we are in we can create the maze
        // we can have a 5x5 maze. we can have an enum struct

        // GameView view = new GameView(this, maze);
        // setContentView(view);

    }
}
