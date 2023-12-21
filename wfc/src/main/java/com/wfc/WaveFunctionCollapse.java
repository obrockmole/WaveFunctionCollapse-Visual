package com.wfc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import com.wfc.Main.Tiles;

import javax.swing.*;

public class WaveFunctionCollapse {
    public int width, height;
    public Tiles[] tiles;
    public ArrayList<Cell> grid;

    int iterations;

    double startTime, endTime;

    public WaveFunctionCollapse(int width, int height, Tiles[] tiles) {
        this.width = width;
        this.height = height;
        this.tiles = tiles;

        grid = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid.add(new Cell(x, y, tiles, false));
            }
        }
    }

    public void start() {
        startTime = System.currentTimeMillis();
        
        checkLowestEntropy();
    }

    public void checkLowestEntropy() {
        ArrayList<Cell> entropyGrid = new ArrayList<>();
        for (Cell cell : grid) {
            entropyGrid.add(cell);
        }

        entropyGrid.removeIf(cell -> cell.collapsed || cell.possibleTiles.length == 1);
        if (entropyGrid.size() == 0) return;
        entropyGrid.sort((cell1, cell2) -> cell1.possibleTiles.length - cell2.possibleTiles.length);

        int lowestEntropy = entropyGrid.get(0).possibleTiles.length;
        entropyGrid.removeIf(cell -> cell.possibleTiles.length > lowestEntropy);

        collapseCells(entropyGrid);
    }

    public void collapseCells(ArrayList<Cell> entropyGrid) {
        int randomCellIndex = (int)(Math.random() * entropyGrid.size());
        Cell randomCell = entropyGrid.get(randomCellIndex);
        Tiles randomTile = randomCell.possibleTiles[(int)(Math.random() * randomCell.possibleTiles.length)];
        grid.get(randomCell.y * width + randomCell.x).collapse(randomTile);
        updateEntropy();
    }

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

        if (iterations < width * height) {
            checkLowestEntropy();
        }

        endTime = System.currentTimeMillis();
    }

    public void removeSingles() {
        ArrayList<Cell> newGrid = new ArrayList<>(grid);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                Cell currentCell = newGrid.get(index);
                Cell northCell = null, southCell = null, eastCell = null, westCell = null;

                if (y > 0) northCell = newGrid.get((y - 1) * width + x);
                if (y < height - 1) southCell = newGrid.get((y + 1) * width + x);
                if (x > 0) westCell = newGrid.get(y * width + (x - 1));
                if (x < width - 1) eastCell = newGrid.get(y * width + (x + 1));

                if (northCell != null && southCell != null && eastCell != null && westCell != null) {
                    Tiles commonTile = northCell.possibleTiles[0];
                    if (commonTile == southCell.possibleTiles[0] && commonTile == eastCell.possibleTiles[0] && commonTile == westCell.possibleTiles[0]) {
                        currentCell.collapse(commonTile);
                    }
                } else {
                    Cell[] cells = {northCell, southCell, eastCell, westCell};
                    Tiles commonTile = null;
                    boolean allSame = true;

                    for (Cell cell : cells) {
                        if (cell != null) {
                            if (commonTile == null) {
                                commonTile = cell.possibleTiles[0];
                            } else if (commonTile != cell.possibleTiles[0]) {
                                allSame = false;
                                break;
                            }
                        }
                    }

                    if (allSame && commonTile != null) {
                        currentCell.collapse(commonTile);
                    }
                }
            }
        }

        grid.clear();
        grid.addAll(newGrid);
    }

    public void smooth(int amount) {
        //Function to smooth out the generation, create bigger pockets, and remove small pockets

    }

    public void printGrid() {
        String output = "";

        for (int i = 0; i < grid.size(); i++) {
            if (i % width == 0) {
                output += "\n";
            }

            if (grid.get(i).collapsed)
                output += grid.get(i).possibleTiles[0].tile.textColor + "█" + "\u001B[0m";
            else
                output += "\u001B[30m█\u001B[0m";
        }

        System.out.println(output);
        System.out.println("Time taken: " + (endTime - startTime) + "ms");
    }

    public void displayGrid() {
        JFrame frame = new JFrame("Wave Function Collapse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        for (Cell cell : grid) {
            JPanel cellPanel = new JPanel();
            cellPanel.setSize(20, 20);
            cellPanel.setLocation(cell.x * 20, cell.y * 20);
            cellPanel.setBackground(cell.possibleTiles[0].tile.color);
            frame.add(cellPanel);
        }

        frame.pack();
        Insets insets = frame.getInsets();
        frame.setSize(new Dimension(width * 20 + insets.left + insets.right, height * 20 + insets.top + insets.bottom));

        frame.revalidate();
        frame.setVisible(true);
    }
}
