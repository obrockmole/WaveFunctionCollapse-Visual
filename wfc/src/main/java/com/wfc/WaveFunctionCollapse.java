package com.wfc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.swing.*;

public class WaveFunctionCollapse {
    public int width, height;
    public ArrayList<Cell> grid;
    public int iterations;

    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    public WaveFunctionCollapse(int width, int height, Tiles[] tiles) {
        this.width = width;
        this.height = height;
        this.grid = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.add(new Cell(x, y, tiles, false));
            }
        }
    }

    public void start() {
        checkLowestEntropy();
    }

    public void checkLowestEntropy() {
        ArrayList<Cell> entropyGrid = new ArrayList<>(grid);

        entropyGrid.removeIf(cell -> cell.collapsed || cell.possibleTiles.length == 1);
        if (entropyGrid.isEmpty()) {
            iterations = width * height;
            return;
        }

        entropyGrid.sort(Comparator.comparingInt(cell -> cell.possibleTiles.length));

        entropyGrid.removeIf(cell -> cell.possibleTiles.length > entropyGrid.get(0).possibleTiles.length);

        collapseCells(entropyGrid);
    }

    public void collapseCells(ArrayList<Cell> entropyGrid) {
        Cell randomCell = entropyGrid.get((int) (Math.random() * entropyGrid.size()));
        randomCell.collapse(randomCell.possibleTiles[(int) (Math.random() * randomCell.possibleTiles.length)]);
        updateEntropy();
    }

    private void updateEntropy() {
        ArrayList<Cell> newGrid = new ArrayList<>(grid);
        for (Cell cell : newGrid) {
            if (!cell.collapsed) {
                ArrayList<Tiles> validTileOptions = new ArrayList<>(Arrays.asList(Tiles.values()));
                for (Direction direction : Direction.values()) {
                    Cell neighborCell = getNeighborCell(cell, direction);
                    if (neighborCell != null && neighborCell.collapsed) {
                        ArrayList<Tiles> viableTiles = getViableTiles(neighborCell, direction);
                        validTileOptions.retainAll(!viableTiles.isEmpty() ? viableTiles : validTileOptions);
                    }
                }
                cell.setPossibleTiles(validTileOptions.toArray(new Tiles[0]));
                if (cell.possibleTiles.length == 1) {
                    cell.collapse(validTileOptions.get(0));
                }
            }
        }
        grid.clear();
        grid.addAll(newGrid);
        iterations++;
    }

    private Cell getNeighborCell(Cell cell, Direction direction) {
        int x = cell.x;
        int y = cell.y;
        switch (direction) {
            case NORTH:
                y--;
                break;
            case SOUTH:
                y++;
                break;
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
        }
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return null;
        }
        return grid.get(y * width + x);
    }

    private ArrayList<Tiles> getViableTiles(Cell cell, Direction direction) {
        int[] viableTilesInt = switch (direction) {
            case NORTH -> cell.possibleTiles[0].tile.south;
            case SOUTH -> cell.possibleTiles[0].tile.north;
            case EAST -> cell.possibleTiles[0].tile.west;
            case WEST -> cell.possibleTiles[0].tile.east;
            default -> throw new IllegalArgumentException("Invalid direction: " + direction);
        };
        return Arrays.stream(viableTilesInt)
                .mapToObj(tile -> Tiles.values()[tile])
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void printGrid() {
        StringBuilder output = new StringBuilder();

        for (Cell cell : grid) {
            output.append(cell.collapsed ? cell.possibleTiles[0].index : "X").append(" ");

            if (cell.x == width - 1)
                output.append("\n");
        }

        System.out.println(output);
    }

    public void displayGrid() {
        JFrame frame = new JFrame("Wave Function Collapse");
        frame.setLayout(new GridLayout(height, width));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        for (Cell cell : grid) {
            JLabel cellPanel = new JLabel(new ImageIcon(cell.possibleTiles[0].tile.image));
            cellPanel.setSize(20, 20);
            cellPanel.setLocation(cell.x * 20, cell.y * 20);
            frame.add(cellPanel);
        }

        frame.pack();
        frame.setSize(new Dimension(width * 20 + frame.getInsets().left + frame.getInsets().right, height * 20 + frame.getInsets().top + frame.getInsets().bottom));
        frame.setVisible(true);
    }
}
