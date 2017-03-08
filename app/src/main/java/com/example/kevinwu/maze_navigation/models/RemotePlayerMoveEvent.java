package com.example.kevinwu.maze_navigation.models;

/**
 * Created by Kevin on 3/7/2017.
 */

public class RemotePlayerMoveEvent {
    private String message;

    public RemotePlayerMoveEvent(String message) {
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
