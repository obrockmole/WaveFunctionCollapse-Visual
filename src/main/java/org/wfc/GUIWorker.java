package org.wfc;

import javax.swing.*;
import java.util.List;

public class GUIWorker extends SwingWorker<Void, Cell[]> {
    private final WaveFunctionCollapse wfc;
    private final JFrame frame;
    private static final int DELAY = 8; // Delay in milliseconds between each cell collapse

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
        wfc.saveGrid("src/main/generations/");
    }

    private void displayGrid(Cell[] grid) {
        frame.getContentPane().removeAll();

        for (Cell cell : grid) {
            JLabel cellPanel = new JLabel(new ImageIcon(cell.isCollapsed() ? cell.getPossibleTiles()[0].getTile().getImage() : ""));
            cellPanel.setSize(Main.IMAGE_SIZE, Main.IMAGE_SIZE);
            cellPanel.setLocation(cell.getX() * Main.IMAGE_SIZE, cell.getY() * Main.IMAGE_SIZE);
            frame.add(cellPanel);
        }

        frame.revalidate();
        frame.repaint();
    }
}
