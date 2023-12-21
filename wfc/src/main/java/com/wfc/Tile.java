package com.wfc;

public class Tile {
    public String image;
    public Tile[] northT, southT, eastT, westT;
    public int[] north, south, east, west;

    public Tile() {}

    public Tile(String image, int[] north, int[] south, int[] east, int[] west) {
        this.image = "wfc/src/main/resources/" + image;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }
}
