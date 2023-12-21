package com.wfc;

import java.awt.*;

public class Main {
    static WaveFunctionCollapse wfc;

    enum Tiles {
        WATER(new Tile(new Color(0, 191, 255), "\u001B[34m", new int[]{0, 1}, new int[]{0, 1}, new int[]{0, 1}, new int[]{0, 1}), 0), //Index 0
        SAND(new Tile(new Color(240, 200, 140), "\u001B[33m", new int[]{0, 1, 2}, new int[]{0, 1, 2}, new int[]{0, 1, 2}, new int[]{0, 1, 2}), 1), //Index 1
        GRASS(new Tile(Color.GREEN, "\u001B[32m", new int[]{1, 2, 3}, new int[]{1, 2, 3}, new int[]{1, 2, 3}, new int[]{1, 2, 3}), 2), //Index 2
        FOREST(new Tile(new Color(139, 69, 19), "\u001B[38;2;139;69;19m", new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}, new int[]{2, 3}), 3); //Index 3

        //light blue rgb color
        //0, 191, 255
        //tan rgb color
        //210, 180, 140
        //brown rgb color
        //139, 69, 19
        public final Tile tile;
        public final int index;

        Tiles(Tile tile, int index) {
            this.tile = tile;
            this.index = index;
        }
    }

    public static void main(String[] args) {
        wfc = new WaveFunctionCollapse(50, 30, new Tiles[]{Tiles.WATER, Tiles.SAND, Tiles.GRASS, Tiles.FOREST});

        wfc.start();
        wfc.removeSingles();

        wfc.printGrid();
        wfc.displayGrid();

        wfc.removeSinglesTwo();
        wfc.printGrid();
        wfc.displayGrid();
    }
}