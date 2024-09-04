package org.wfc;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GUIWorker extends SwingWorker<Void, Cell[]> {
    private final WaveFunctionCollapse wfc;
    private final JFrame frame;
    private static final int DELAY = 50; // Delay in milliseconds between each cell collapse

    public GUIWorker(WaveFunctionCollapse wfc, JFrame frame) {
        this.wfc = wfc;
        this.frame = frame;
    }

    @Override
    protected Void doInBackground() {
        while (!wfc.isComplete()) {
            wfc.checkLowestEntropy();
            publish(wfc.getGrid().toArray(new Cell[0]));

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void process(List<Cell[]> chunks) {
        Cell[] latestGrid = chunks.getLast();
        displayGrid(latestGrid);
    }

    @Override
    protected void done() {
        wfc.printGrid();
//        wfc.saveGridText("src/main/generations/");
//        wfc.saveGridImage("src/main/generations/");
    }

    private JPanel displayPossibleTiles(Cell cell) {
        JPanel cellPanel = new JPanel(new GridLayout(3, 3));
        cellPanel.setSize(Main.IMAGE_SIZE, Main.IMAGE_SIZE);
        cellPanel.setLocation(cell.getX() * Main.IMAGE_SIZE, cell.getY() * Main.IMAGE_SIZE);
        cellPanel.setBackground(new Color(200 / cell.getPossibleTiles().length, 15 * cell.getPossibleTiles().length, 0));

        for (Tiles tile : cell.getPossibleTiles()) {
            JLabel tilePanel = new JLabel(new ImageIcon(tile.getTile().image()));
            cellPanel.add(tilePanel);
        }

        return cellPanel;
    }

    private void displayGrid(Cell[] grid) {
        frame.getContentPane().removeAll();

        for (Cell cell : grid) {
            if (cell.isCollapsed()) {
                JLabel cellPanel = new JLabel(new ImageIcon(cell.getPossibleTiles()[0].getTile().image()));
                cellPanel.setSize(Main.IMAGE_SIZE, Main.IMAGE_SIZE);
                cellPanel.setLocation(cell.getX() * Main.IMAGE_SIZE, cell.getY() * Main.IMAGE_SIZE);
                frame.add(cellPanel);

            } else {
                frame.add(displayPossibleTiles(cell));
            }
        }

        frame.revalidate();
        frame.repaint();
    }
}
