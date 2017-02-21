package com.example.kevinwu.maze_navigation.models;

import android.graphics.Paint;

/**
 * Created by Kevin on 2/20/2017.
 */

public class PlayerInfo {

    public PlayerInfo (){
    }

    private int playerX, playerY;
    private Paint playerColor;
    private int playerMazeNum;

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public Paint getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Paint playerColor) {
        this.playerColor = playerColor;
    }

    public int getPlayerMazeNum() {
        return playerMazeNum;
    }

    public void setPlayerMazeNum(int playerMazeNum) {
        this.playerMazeNum = playerMazeNum;
    }
}
