package org.wfc;

import javax.swing.*;
import java.awt.*;

public class Main {
    static WaveFunctionCollapse wfc;
    static int WIDTH = 20; //Number of tiles in the X direction
    static int HEIGHT = 15; //Number of tiles in the Y direction
    static int IMAGE_SIZE = 20; //Size of the image used for each tile in pixels

    public static void main(String[] args) {
        wfc = new WaveFunctionCollapse(WIDTH, HEIGHT, Tiles.values());
        wfc.start();

        JFrame frame = createFrame();
        frame.setVisible(true);

        GUIWorker worker = new GUIWorker(wfc, frame);
        worker.execute();
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Wave Function Collapse");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(HEIGHT, WIDTH));
        frame.setResizable(false);
        frame.setSize(new Dimension(WIDTH * IMAGE_SIZE + frame.getInsets().left + frame.getInsets().right, HEIGHT * IMAGE_SIZE + frame.getInsets().top + frame.getInsets().bottom));

        return frame;
    }
}