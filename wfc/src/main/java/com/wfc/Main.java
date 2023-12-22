package com.wfc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    static WaveFunctionCollapse wfc;
    static boolean complete = false;
    static int WIDTH = 20;
    static int HEIGHT = 15;

    public static void main(String[] args) {
        wfc = new WaveFunctionCollapse(WIDTH, HEIGHT, Tiles.values());
        wfc.start();

        JFrame frame = createFrame();
        Timer timer = createTimer(frame);
        timer.start();

        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Wave Function Collapse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(new GridLayout(HEIGHT, WIDTH));
        frame.pack();
        frame.setSize(new Dimension(WIDTH * 20 + frame.getInsets().left + frame.getInsets().right, HEIGHT * 20 + frame.getInsets().top + frame.getInsets().bottom));
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
        return new Timer(10, e -> {
            if (!complete) {
                update(frame);
                if (complete) {
                    wfc.printGrid();
                    wfc.saveGrid("wfc/src/main/generations/");

                    frame.getContentPane().removeAll();
                    for (Cell cell : wfc.grid) {
                        JLabel cellPanel = new JLabel(new ImageIcon(cell.possibleTiles[0].tile.image));
                        cellPanel.setSize(20, 20);
                        cellPanel.setLocation(cell.x * 20, cell.y * 20);
                        frame.add(cellPanel);
                    }

                    frame.pack();
                    frame.setSize(new Dimension(WIDTH * 20 + frame.getInsets().left + frame.getInsets().right, HEIGHT * 20 + frame.getInsets().top + frame.getInsets().bottom));
                }
            }
        });
    }

    private static void update(JFrame frame) {
        if (wfc.iterations < wfc.width * wfc.height) {
            wfc.checkLowestEntropy();
        } else {
            complete = true;
        }

        displayGrid(frame);
    }

    private static void displayGrid(JFrame frame) {
        frame.getContentPane().removeAll();

        for (Cell cell : wfc.grid) {
            JLabel cellPanel = new JLabel(new ImageIcon(cell.collapsed ? cell.possibleTiles[0].tile.image : ""));
            cellPanel.setSize(20, 20);
            cellPanel.setLocation(cell.x * 20, cell.y * 20);
            frame.add(cellPanel);
        }

        frame.revalidate();
        frame.repaint();
    }
}