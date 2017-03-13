package com.example.kevinwu.maze_navigation.models;

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

        // Map 1
        items.add(new Item(1, 2, 1, "Dynamite"));
        items.add(new Item(1, 3, 1, "Key"));
        items.add(new Item(1, 5, 6, "Dynamite"));

        // Map 2
        items.add(new Item(2, 6, 5, "Key"));

        // Map 3
        items.add(new Item(3, 2, 7, "Key"));
        items.add(new Item(3, 5, 1, "Dynamite"));

        // Map 4
        items.add(new Item(4, 7, 3, "Dynamite"));
        items.add(new Item(4, 7, 5, "Key"));

        //Map 5
        items.add(new Item(5, 0, 7, "Dynamite"));
        items.add(new Item(5, 4, 0, "Key"));
        items.add(new Item(5, 6, 7, "Dynamite"));

        return items;
    }
}
