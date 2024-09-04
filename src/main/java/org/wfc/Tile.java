package org.wfc;

public record Tile(String image, int[] north, int[] south, int[] east, int[] west) {
    public Tile(String image, int[] north, int[] south, int[] east, int[] west) {
        this.image = "src/main/resources/" + image;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }
}
