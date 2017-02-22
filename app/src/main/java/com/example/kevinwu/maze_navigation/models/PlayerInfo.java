package com.example.kevinwu.maze_navigation.models;

import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kevin on 2/20/2017.
 */

public class PlayerInfo implements Parcelable {

    public PlayerInfo (int x, int y, int mazeNum){
        this.playerX = x;
        this.playerY = y;
        this.playerMazeNum = mazeNum;
    }

    private int playerX, playerY;
    //private Paint playerColor;
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

//    public Paint getPlayerColor() {
//        return playerColor;
//    }
//
//    public void setPlayerColor(Paint playerColor) {
//        this.playerColor = playerColor;
//    }

    public int getPlayerMazeNum() {
        return playerMazeNum;
    }

    public void setPlayerMazeNum(int playerMazeNum) {
        this.playerMazeNum = playerMazeNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(new int[] {this.playerX, this.playerY, this.playerMazeNum});
    }

    public static final Parcelable.Creator<PlayerInfo> CREATOR = new Parcelable.Creator<PlayerInfo>() {
        public PlayerInfo createFromParcel(Parcel in) {
            return new PlayerInfo(in);
        }

        public PlayerInfo[] newArray(int size) {
            return new PlayerInfo[size];
        }
    };

    private PlayerInfo(Parcel in){
        int[] data = new int[3];

        in.readIntArray(data);
        this.playerX = data[0];
        this.playerY = data[1];
        this.playerMazeNum = data[2];
    }
}
