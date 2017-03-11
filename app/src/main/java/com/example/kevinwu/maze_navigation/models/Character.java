package com.example.kevinwu.maze_navigation.models;

import com.example.kevinwu.maze_navigation.models.Item;

import java.util.ArrayList;

/**
 * Created by Stan on 2/15/2017.
 */

public class Character {

    public Character() {
        this.mostRecentDirection = "None";
        character_Items = new ArrayList<>();
    }

    public void setDirection(String direction) {
        this.mostRecentDirection = direction;
    }

    public String getDirection() {
        return this.mostRecentDirection;
    }

    public void addItemToInventory(String item_id) {
        character_Items.add(new Item(item_id));
    }

    public ArrayList<Item> getInventory() {
        return character_Items;
    }

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return this.username; }

    private String mostRecentDirection;
    private ArrayList<Item> character_Items;
    private String username;
}
