package test;

import gui.ResultPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nymann on 20-06-2016.
 */
public class ResultTest {
    static ResultPanel resultPanel;
    static int roomX = 963; // Wall 2 and Wall 0, is 9.63 meters.
    static int roomY = 1078; // The two Glasswalls are 10.78 meters.

    public static void main(String[] args) {
        ArrayList<Point> greenCubes = new ArrayList<>();
        ArrayList<Point> redCubes = new ArrayList<>();
        int xUpper = 963;
        int xLower = 0;
        int yUpper = 1078;
        int yLower = 0;

        for (int i = 0; i < 30; i++) {
            greenCubes.add(i, new Point(randomNumber(xUpper, xLower), randomNumber(yUpper, yLower)));
            redCubes.add(i, new Point(randomNumber(xUpper, xLower), randomNumber(yUpper, yLower)));
        }

        resultPanel = new ResultPanel(greenCubes, redCubes);
        Dimension velocityDimension = new Dimension(roomX, roomY);
        resultPanel.setSize(velocityDimension);
        resultPanel.setPreferredSize(velocityDimension);

        JFrame resultFrame = new JFrame();
        resultFrame.getContentPane().setLayout(new FlowLayout());
        resultFrame.getContentPane().add(resultPanel);
        resultFrame.setLocation(0, 0);
        resultFrame.setTitle("Cube Room Visualization - Night Fury");
        resultFrame.setUndecorated(true);
        resultFrame.setVisible(true);
        resultFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        resultFrame.pack();
    }

    private static int randomNumber(int upperBound, int lowerBound) {
        return ((int) (Math.random() * (upperBound - lowerBound)) + lowerBound);
    }
}
