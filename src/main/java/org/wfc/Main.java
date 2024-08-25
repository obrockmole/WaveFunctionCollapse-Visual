package org.wfc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    static WaveFunctionCollapse wfc;
    static boolean complete = false;
    static int WIDTH = 20; //Number of tiles in the X direction
    static int HEIGHT = 15; //Number of tiles in the Y direction
    static int IMAGE_SIZE = 20; //Size of the image used for each tile in pixels

    public static void main(String[] args) {
        wfc = new WaveFunctionCollapse(WIDTH, HEIGHT, Tiles.values());
        wfc.start();

        JFrame frame = createFrame();
        frame.setVisible(true);

        Timer timer = createTimer(frame);
        timer.start();

        while (wfc.getIterations() < wfc.getWidth() * wfc.getHeight()) {}
        timer.stop();

        wfc.printGrid();
        wfc.saveGrid("src/main/generations/");
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Wave Function Collapse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(HEIGHT, WIDTH));
        frame.pack();
        frame.setSize(new Dimension(WIDTH * IMAGE_SIZE + frame.getInsets().left + frame.getInsets().right, HEIGHT * IMAGE_SIZE + frame.getInsets().top + frame.getInsets().bottom));
        frame.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == 32) {
                    wfc = new WaveFunctionCollapse(WIDTH, HEIGHT, Tiles.values());
                    wfc.start();
                    complete = false;
                }
            }
        });
        return frame;
    }

    private static Timer createTimer(JFrame frame) {
        return new Timer(15, e -> {
            wfc.checkLowestEntropy();
            displayGrid(frame);
        });
    }

    private static void displayGrid(JFrame frame) {
        frame.getContentPane().removeAll();

        for (Cell cell : wfc.getGrid()) {
            JLabel cellPanel = new JLabel(new ImageIcon(cell.isCollapsed() ? cell.getPossibleTiles()[0].getTile().getImage() : ""));
            cellPanel.setSize(IMAGE_SIZE, IMAGE_SIZE);
            cellPanel.setLocation(cell.getX() * IMAGE_SIZE, cell.getY() * IMAGE_SIZE);
            frame.add(cellPanel);
        }

        frame.revalidate();
        frame.repaint();
    }
}