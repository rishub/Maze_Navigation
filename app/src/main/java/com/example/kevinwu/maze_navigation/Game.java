package com.example.kevinwu.maze_navigation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andretietz.android.controller.DirectionView;
import com.andretietz.android.controller.InputView;

public class Game extends AppCompatActivity implements InputView.InputEventListener {

    private TextView textAction;
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_game);
        textAction = (TextView) findViewById(R.id.textView);
        DirectionView directionView = (DirectionView) findViewById(R.id.viewDirection);
        Log.d("Kevin", "Here");
        directionView.setOnButtonListener(this);
        gameView = (GameView) findViewById(R.id.mazeInstance);
//         Maze maze = MazeFactory.getMaze(1); // creates a maze instance
//        // depending on which part of the maze we are in we can create the maze
//        // we can have a 5x5 maze. we can have an enum struct
//        GameView view = new GameView(Game.this, maze);
//        setContentView(view);
    }

    @Override public void onInputEvent(View view, int buttons) {
        switch (view.getId()) {
            case R.id.viewDirection:
                Log.d("Kevin", "buttons: " + buttons);
                textAction.setText(String.format("Action: %s", directionButtonsToString(buttons)));
                playerMove(directionButtonsToString(buttons)); // player can go up down left right
                break;
        }
    }

    private String directionButtonsToString(int buttons) {
        String direction = "NONE";
        switch (buttons&0xff) {
            case DirectionView.DIRECTION_DOWN:
                direction = "Down";
                break;
            case DirectionView.DIRECTION_LEFT:
                direction = "Left";
                break;
            case DirectionView.DIRECTION_RIGHT:
                direction = "Right";
                break;
            case DirectionView.DIRECTION_UP:
                direction = "Up";
                break;
            case DirectionView.DIRECTION_DOWN_LEFT:
                direction = "Down Left";
                break;
            case DirectionView.DIRECTION_UP_LEFT:
                direction = "Up Left";
                break;
            case DirectionView.DIRECTION_DOWN_RIGHT:
                direction = "Down Right";
                break;
            case DirectionView.DIRECTION_UP_RIGHT:
                direction = "Up Right";
                break;

        }
        return direction;
    }

    private boolean playerMove(String direction){
        boolean moved = false;
        switch(direction) {
            case "Up":
                moved = GameView.maze.move(Maze.UP);
                break;
            case "Down":
                Log.d("Kevin", "Direction Down");
                moved = GameView.maze.move(Maze.DOWN);
                Log.d("Kevin", "Moved = " + moved);
                break;
            case "Right":
                moved = GameView.maze.move(Maze.RIGHT);
                break;
            case "Left":
                moved = GameView.maze.move(Maze.LEFT);
                break;
        }
        if(moved) {
            gameView.invalidate();
            //the ball was moved so we'll redraw the view
        }
        return true;
    }


}
