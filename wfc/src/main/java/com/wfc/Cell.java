package com.wfc;

public class Cell {
    public int x, y;
    public boolean collapsed;
    public Tiles[] possibleTiles;

    public Cell(int x, int y, Tiles[] possibleTiles, boolean collapsed) {
        this.x = x;
        this.y = y;
        this.possibleTiles = possibleTiles;
        this.collapsed = collapsed;
    }

    public void setPossibleTiles(Tiles[] possibleTiles) {
        this.possibleTiles = possibleTiles;
    }

    public void collapse(Tiles tile) {
        possibleTiles = new Tiles[]{tile};
        collapsed = true;
    }
}
