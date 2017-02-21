package com.example.kevinwu.maze_navigation.models;

/**
 * Created by Kevin on 2/11/2017.
 */

public class Point {

    public Point(int x, int y, int link) {
        this.xCoordinate = x;
        this.yCoordinate = y;
        this.mazeLink = link;
    }

    private int xCoordinate;
    private int yCoordinate;

    public int getMazeLink() {
        return mazeLink;
    }

    public void setMazeLink(int mazeLink) {
        this.mazeLink = mazeLink;
    }

    private int mazeLink;

    public int getY() {
        return yCoordinate;
    }

    public void setY(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getX() {
        return xCoordinate;
    }

    public void setX(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

}
