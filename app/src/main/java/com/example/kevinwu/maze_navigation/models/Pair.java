package com.example.kevinwu.maze_navigation.models;

/**
 * Created by Kevin on 2/11/2017.
 */

public class Pair<Point,String> {

    private final Point left;
    private final String right;

    public Pair(Point left, String right) {
        this.left = left;
        this.right = right;
    }

    public Point getPoint() { return left; }
    public String getDirection() { return right; }

}
