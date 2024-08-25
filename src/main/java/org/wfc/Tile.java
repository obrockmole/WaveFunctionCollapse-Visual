package org.wfc;

public class Tile {
    private String image;
    private int[] north, south, east, west;

    public Tile() {}

    public Tile(String image, int[] north, int[] south, int[] east, int[] west) {
        this.image = "src/main/resources/" + image;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    public String getImage() {
        return image;
    }

    public int[] getNorth() {
        return north;
    }

    public int[] getSouth() {
        return south;
    }

    public int[] getEast() {
        return east;
    }

    public int[] getWest() {
        return west;
    }
}
