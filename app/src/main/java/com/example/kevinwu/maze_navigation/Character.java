package com.example.kevinwu.maze_navigation;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stan on 2/15/2017.
 */

public class Character {

    public Character() {
        this.mostRecentDirection = "None";
        this.isInventoryOpened = false;
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

    public boolean inventoryOpened(){
        return isInventoryOpened;
    }

    public void setInventory(String input){
        if (input.equals("Open"))
            isInventoryOpened = true;
        else
            isInventoryOpened = false;
    }

    public ArrayList<Item> getInventory() {
        return character_Items;
    }

    private String mostRecentDirection;
    private boolean isInventoryOpened;
    private ArrayList<Item> character_Items;
}
