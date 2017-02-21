package com.example.kevinwu.maze_navigation.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import com.example.kevinwu.maze_navigation.services.BluetoothService;
import com.example.kevinwu.maze_navigation.views.GameView;
import com.example.kevinwu.maze_navigation.models.Maze;
import com.example.kevinwu.maze_navigation.models.MazeFactory;
import com.example.kevinwu.maze_navigation.R;

public class Game extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_game);
        Maze maze = MazeFactory.getMaze(1); // creates a maze instance
        ArrayList<Item> item = ItemFactory.getMazeItems(); // creates the list of items in the maze
        // the items are long-lasting, they are not recreated when the game view is recreated
        Character character = new Character();
        // depending on which part of the maze we are in we can create the maze
        // we can have a 5x5 maze. we can have an enum struct
        GameView view = new GameView(Game.this, maze, item, character);
        setContentView(view);
        startService(new Intent(getBaseContext(), BluetoothService.class));
    }
}