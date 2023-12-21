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

        int lowestEntropy = entropyGrid.get(0).possibleTiles.length;
        entropyGrid.removeIf(cell -> cell.possibleTiles.length > lowestEntropy);

        collapseCells(entropyGrid);
    }

    public void collapseCells(ArrayList<Cell> entropyGrid) {
        Cell randomCell = entropyGrid.get((int) (Math.random() * entropyGrid.size()));
        randomCell.collapse(randomCell.possibleTiles[(int) (Math.random() * randomCell.possibleTiles.length)]);
        updateEntropy();
    }


    /*private void updateEntropy() {
        for (Cell cell : grid) {
            if (cell.collapsed) continue;

            java.util.List<Tiles> validTileOptions = new ArrayList<>(Arrays.asList(Tiles.values()));
            //For some reason possibleTiles length is 0 some times sooooooo idk
            if (cell.y > 0) validTileOptions.retainAll(getViableTiles(grid.get((cell.y - 1) * width + cell.x).possibleTiles[0].tile.south));
            if (cell.y < height - 1) validTileOptions.retainAll(getViableTiles(grid.get((cell.y + 1) * width + cell.x).possibleTiles[0].tile.north));
            if (cell.x < width - 1) validTileOptions.retainAll(getViableTiles(grid.get(cell.y * width + (cell.x + 1)).possibleTiles[0].tile.west));
            if (cell.x > 0) validTileOptions.retainAll(getViableTiles(grid.get(cell.y * width + (cell.x - 1)).possibleTiles[0].tile.east));

            //Dis is why its the length is 0 cuz it likes to be funny and generate so there are no valid tile options
            cell.setPossibleTiles(validTileOptions.toArray(new Tiles[0]));
            if (cell.possibleTiles.length == 1) cell.collapse(validTileOptions.get(0));
        }
        iterations++;
    }

    private List<Tiles> getViableTiles(int[] viableTilesInt) {
        return Arrays.stream(viableTilesInt)
                .mapToObj(tile -> Tiles.values()[tile])
                .collect(Collectors.toList());
    }*/

    public void updateEntropy() {
        ArrayList<Cell> newGrid = new ArrayList<>(grid);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;

                if (newGrid.get(index).possibleTiles.length == 1 && !newGrid.get(index).collapsed) {
                    newGrid.get(index).collapse(newGrid.get(index).possibleTiles[0]);
                    continue;
                } else if (newGrid.get(index).collapsed) {
                    continue;
                }

                ArrayList<Tiles> validTileOptions = new ArrayList<>(Arrays.asList(Tiles.values()));

                if (y > 0) {
                    Cell northCell = newGrid.get((y - 1) * width + x);

                    if (northCell.collapsed) {
                        int[] viableTilesInt = northCell.possibleTiles[0].tile.south;
                        ArrayList<Tiles> viableTiles = Arrays.stream(viableTilesInt)
                                .mapToObj(tile -> Tiles.values()[tile])
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                        validTileOptions.retainAll(!viableTiles.isEmpty() ? viableTiles : validTileOptions);
                    }
                }

                if (y < height - 1) {
                    Cell southCell = newGrid.get((y + 1) * width + x);

                    if (southCell.collapsed) {
                        int[] viableTilesInt = southCell.possibleTiles[0].tile.north;
                        ArrayList<Tiles> viableTiles = Arrays.stream(viableTilesInt)
                                .mapToObj(tile -> Tiles.values()[tile])
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                        validTileOptions.retainAll(!viableTiles.isEmpty() ? viableTiles : validTileOptions);
                    }
                }

                if (x < width - 1) {
                    Cell eastCell = newGrid.get(y * width + (x + 1));

                    if (eastCell.collapsed) {
                        int[] viableTilesInt = eastCell.possibleTiles[0].tile.west;
                        ArrayList<Tiles> viableTiles = Arrays.stream(viableTilesInt)
                                .mapToObj(tile -> Tiles.values()[tile])
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                        validTileOptions.retainAll(!viableTiles.isEmpty() ? viableTiles : validTileOptions);
                    }
                }

                if (x > 0) {
                    Cell westCell = newGrid.get(y * width + (x - 1));

                    if (westCell.collapsed) {
                        int[] viableTilesInt = westCell.possibleTiles[0].tile.east;
                        ArrayList<Tiles> viableTiles = Arrays.stream(viableTilesInt)
                                .mapToObj(tile -> Tiles.values()[tile])
                                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

                        validTileOptions.retainAll(!viableTiles.isEmpty() ? viableTiles : validTileOptions);
                    }
                }

                newGrid.get(index).setPossibleTiles(validTileOptions.toArray(new Tiles[0]));

                if (newGrid.get(index).possibleTiles.length == 1) {
                    newGrid.get(index).collapse(validTileOptions.get(0));
                }
            }
        }

        grid.clear();
        grid.addAll(newGrid);
        iterations++;
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
