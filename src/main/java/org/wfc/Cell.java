package org.wfc;

public class Cell {
    private final int x, y;
    private Tiles[] possibleTiles;
    private boolean collapsed;

    public Cell(int x, int y, Tiles[] possibleTiles, boolean collapsed) {
        this.x = x;
        this.y = y;
        this.possibleTiles = possibleTiles;
        this.collapsed = collapsed;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Tiles[] getPossibleTiles() {
        return possibleTiles;
    }

    public void setPossibleTiles(Tiles[] possibleTiles) {
        this.possibleTiles = possibleTiles;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void collapse(Tiles tile) {
        possibleTiles = new Tiles[]{tile};
        collapsed = true;
    }
}
