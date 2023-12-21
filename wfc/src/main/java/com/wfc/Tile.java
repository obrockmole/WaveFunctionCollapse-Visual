package com.wfc;

import java.awt.*;

public class Tile {
    public Color color;
    public String textColor;
    public Tile[] northT, southT, eastT, westT;
    public int[] north, south, east, west;

    public Tile() {}

    public Tile(Color color, String textColor, Tile[] northT, Tile[] southT, Tile[] eastT, Tile[] westT) {
        this.color = color;
        this.textColor = textColor;
        this.northT = northT;
        this.southT = southT;
        this.eastT = eastT;
        this.westT = westT;
    }

    public Tile(Color color, String textColor, int[] north, int[] south, int[] east, int[] west) {
        this.color = color;
        this.textColor = textColor;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }
}
