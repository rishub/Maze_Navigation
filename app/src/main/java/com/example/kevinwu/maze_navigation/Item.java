package com.example.kevinwu.maze_navigation;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Stan on 2/19/2017.
 */

public class Item implements Serializable {

    public Item(int map, int x, int y, String drawable) {
        this.item_map = map;
        this.item_x = x;
        this.item_y = y;
        this.item_string = drawable;
        this.picked_up = false;
        this.is_used = false;
    }

    public Item(String drawable) {
        this.item_map = -1;
        this.item_x = -1;
        this.item_y = -1;
        this.item_string = drawable;
        this.picked_up = false;
        this.is_used = false;
    }

    public int[] getItemPosition() {
        int[] position = new int[3];
        position[0] = this.item_map;
        position[1] = this.item_x;
        position[2] = this.item_y;

        return position;
    }
    public boolean getPickedUp() {
        return this.picked_up;
    }

    public void pickUp() {
        this.picked_up = true;
    }

    public boolean isUsed() {
        return this.is_used;
    }

    public void useItem() {
        this.is_used = true;
    }

    public String getItemID() {
        return this.item_string;
    }

    private int item_map;
    private int item_x;
    private int item_y;
    private String item_string;
    private boolean picked_up;
    private boolean is_used;
}
