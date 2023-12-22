package com.wfc;

enum Tiles {
    //No connections
    BLANK(new Tile("Blank.png", new int[]{0, 3, 5, 6}, new int[]{0, 1, 2, 3}, new int[]{0, 2, 4, 6}, new int[]{0, 1, 4, 5}), 0), //Index 0
    //Connection on south, west sides
//        DOWN_LEFT(new Tile("DownLeft.png", new int[]{0, 4, 6, 7}, new int[]{1, 5, 6, 7}, new int[]{0, 3, 5, 7}, new int[]{1, 3, 4, 7}), 2), //Index 2
    DOWN_LEFT(new Tile("DownLeft.png", new int[]{0, 3, 5, 6}, new int[]{4, 5, 6}, new int[]{0, 2, 4, 6}, new int[]{2, 3, 6}), 1), //Index 2
    //Connection on south, east sides
//        DOWN_RIGHT(new Tile("DownRight.png", new int[]{0, 4, 6, 7}, new int[]{1, 5, 6, 7}, new int[]{1, 2, 4, 6}, new int[]{0, 2, 5, 6}), 3), //Index 3
    DOWN_RIGHT(new Tile("DownRight.png", new int[]{0, 3, 5, 6}, new int[]{4, 5, 6}, new int[]{1, 3, 5}, new int[]{0, 1, 4, 5}), 2), //Index 3
    //Connection on east, west sides
//        LEFT_RIGHT(new Tile("LeftRight.png", new int[]{0, 4, 6, 7}, new int[]{0, 2, 3, 4}, new int[]{1, 2, 4, 6}, new int[]{1, 3, 4, 7}), 4), //Index 4
    LEFT_RIGHT(new Tile("LeftRight.png", new int[]{0, 3, 5, 6}, new int[]{0, 1, 2, 3}, new int[]{1, 3, 5}, new int[]{2, 3, 6}), 3), //Index 4
    //Connection on north, south sides
//        UP_DOWN(new Tile("UpDown.png", new int[]{1, 2, 3, 5}, new int[]{1, 5, 6, 7}, new int[]{0, 3, 5, 7}, new int[]{0, 2, 5, 6}), 5), //Index 5
    UP_DOWN(new Tile("UpDown.png", new int[]{1, 2, 4}, new int[]{4, 5, 6}, new int[]{0, 2, 4, 6}, new int[]{0, 1, 4, 5}), 4), //Index 5
    //Connection on north, west sides
//        UP_LEFT(new Tile("UpLeft.png", new int[]{1, 2, 3, 5}, new int[]{0, 2, 3, 4}, new int[]{0, 3, 5, 7}, new int[]{1, 3, 4, 7}), 6), //Index 6
    UP_LEFT(new Tile("UpLeft.png", new int[]{1, 2, 4}, new int[]{0, 1, 2, 3}, new int[]{0, 2, 4, 6}, new int[]{2, 3, 6}), 5), //Index 6
    //Connection on north, east sides
//        UP_RIGHT(new Tile("UpRight.png", new int[]{1, 2, 3, 5}, new int[]{0, 2, 3, 4}, new int[]{1, 2, 4, 6}, new int[]{0, 2, 5, 6}), 7); //Index 7
    UP_RIGHT(new Tile("UpRight.png", new int[]{1, 2, 4}, new int[]{0, 1, 2, 3}, new int[]{1, 3, 5}, new int[]{0, 1, 4, 5}), 6); //Index 7

    public final Tile tile;
    public final int index;

    Tiles(Tile tile, int index) {
        this.tile = tile;
        this.index = index;
    }
}