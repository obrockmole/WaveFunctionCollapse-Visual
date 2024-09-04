package org.wfc;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

public class WaveFunctionCollapse {
    private final int width, height;
    private int iterations;
    private final ArrayList<Cell> grid;

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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIterations() {
        return iterations;
    }

    public ArrayList<Cell> getGrid() {
        return grid;
    }

    public void start() {
        checkLowestEntropy();
    }

    public void checkLowestEntropy() {
        ArrayList<Cell> entropyGrid = new ArrayList<>(grid);

        entropyGrid.removeIf(cell -> cell.isCollapsed() || cell.getPossibleTiles().length == 1);
        if (entropyGrid.isEmpty()) {
            iterations = width * height;
            return;
        }

        entropyGrid.sort(Comparator.comparingInt(cell -> cell.getPossibleTiles().length));
        entropyGrid.removeIf(cell -> cell.getPossibleTiles().length > entropyGrid.getFirst().getPossibleTiles().length);

        collapseCells(entropyGrid);
    }

    public void collapseCells(ArrayList<Cell> entropyGrid) {
        Cell randomCell = entropyGrid.get((int) (Math.random() * entropyGrid.size()));
        randomCell.collapse(randomCell.getPossibleTiles()[(int) (Math.random() * randomCell.getPossibleTiles().length)]);
        updateEntropy();
    }

    private void updateEntropy() {
        ArrayList<Cell> newGrid = new ArrayList<>(grid);
        for (Cell cell : newGrid) {
            if (!cell.isCollapsed()) {
                ArrayList<Tiles> validTileOptions = new ArrayList<>(Arrays.asList(Tiles.values()));
                for (Direction direction : Direction.values()) {
                    Cell neighborCell = getNeighborCell(cell, direction);
                    if (neighborCell != null && neighborCell.isCollapsed()) {
                        ArrayList<Tiles> viableTiles = getViableTiles(neighborCell, direction);
                        validTileOptions.retainAll(!viableTiles.isEmpty() ? viableTiles : validTileOptions);
                    }
                }

                cell.setPossibleTiles(validTileOptions.toArray(new Tiles[0]));
                if (cell.getPossibleTiles().length == 1) {
                    cell.collapse(validTileOptions.getFirst());
                    updateEntropy();
                }
            }
        }

        grid.clear();
        grid.addAll(newGrid);
        iterations++;
    }

    private Cell getNeighborCell(Cell cell, Direction direction) {
        int x = cell.getX();
        int y = cell.getY();
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
            case NORTH -> cell.getPossibleTiles()[0].getTile().south();
            case SOUTH -> cell.getPossibleTiles()[0].getTile().north();
            case EAST -> cell.getPossibleTiles()[0].getTile().west();
            case WEST -> cell.getPossibleTiles()[0].getTile().east();
        };

        return Arrays.stream(viableTilesInt)
                .mapToObj(tile -> Tiles.values()[tile])
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean isComplete() {
        for (Cell cell : grid) {
            if (!cell.isCollapsed()) {
                return false;
            }
        }
        return true;
    }

    public void printGrid() {
        StringBuilder output = new StringBuilder();

        for (Cell cell : grid) {
            output.append(cell.isCollapsed() ? cell.getPossibleTiles()[0].getIndex() : "X").append(" ");

            if (cell.getX() == width - 1)
                output.append("\n");
        }

        System.out.println(output);
    }

    public void saveGridText(String directory) {
        directory += LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM_dd_yyyy_HH-mm")) + "/";

        if (new File(directory).mkdir()) {
            String fileName = directory + "wfc_" + width + "x_" + height + "y";

            try (FileWriter writer = new FileWriter(fileName + ".txt")) {
                for (Cell cell : grid) {
                    writer.write(String.valueOf(cell.isCollapsed() ? cell.getPossibleTiles()[0].getIndex() : "X"));
                    writer.write(" ");
                    if (cell.getX() == width - 1)
                        writer.write("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Failed to save grid text, directory could not be created.");
        }
    }

    public void saveGridImage(String directory) {
        directory += LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM_dd_yyyy_HH-mm")) + "/";
        if (new File(directory).mkdirs()) {
            String fileName = directory + "wfc_" + width + "x_" + height + "y";

            BufferedImage image = new BufferedImage(width * Main.IMAGE_SIZE, height * Main.IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            for (Cell cell : grid) {
                try {
                    BufferedImage tileImage = ImageIO.read(new File(cell.getPossibleTiles()[0].getTile().image()));
                    g.drawImage(tileImage, cell.getX() * Main.IMAGE_SIZE, cell.getY() * Main.IMAGE_SIZE, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            g.dispose();

            try {
                ImageIO.write(image, "png", new File(fileName + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("Failed to save grid image, directory could not be created.");
        }
    }
}
