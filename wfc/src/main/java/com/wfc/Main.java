package com.wfc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {
    static WaveFunctionCollapse wfc;
    static boolean complete = false;

    public static void main(String[] args) {
        wfc = new WaveFunctionCollapse(30, 20, Tiles.values());
        wfc.start();

        JFrame frame = createFrame();
        Timer timer = createTimer(frame);
        timer.start();

        frame.setVisible(true);
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Wave Function Collapse");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setSize(new Dimension(wfc.width * 20 + frame.getInsets().left + frame.getInsets().right, wfc.height * 20 + frame.getInsets().top + frame.getInsets().bottom));
        return frame;
    }

    private static Timer createTimer(JFrame frame) {
        return new Timer(10, e -> {
            if (!complete) {
                update(frame);
                if (complete) {
                    frame.dispose();
                    wfc.printGrid();
                    wfc.displayGrid();
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