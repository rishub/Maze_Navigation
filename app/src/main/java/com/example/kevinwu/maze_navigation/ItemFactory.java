package com.example.kevinwu.maze_navigation;

import java.util.ArrayList;

/**
 * Created by Stan on 2/19/2017.
 */

public class ItemFactory {

    public static ArrayList<Item> getMazeItems() {
        ArrayList<Item> items = new ArrayList<>();

        // Add items throughout the map
        // The parameters are: maze number, x-coordinate, y-coordinate, and item name
        // There are currently two items: key and dynamite
        items.add(new Item(1, 3, 1, "Key"));
        items.add(new Item(1, 5, 6, "Dynamite"));
        items.add(new Item(2, 6, 5, "Key"));
        return items;
    }
}
