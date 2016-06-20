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

    public static void main(String[] args) {
        ArrayList<Point> greenCubes = new ArrayList<>();
        ArrayList<Point> redCubes = new ArrayList<>();
        int upper = 500;
        int lower = 0;

        for (int i = 0; i < 30; i++) {
            greenCubes.add(i, new Point(randomNumber(upper, lower), randomNumber(upper, lower)));
            redCubes.add(i, new Point(randomNumber(upper, lower), randomNumber(upper, lower)));
        }

        resultPanel = new ResultPanel(greenCubes, redCubes);
        Dimension velocityDimension = new Dimension(800, 800);
        resultPanel.setSize(velocityDimension);
        resultPanel.setPreferredSize(velocityDimension);

        JFrame resultFrame = new JFrame();
        resultFrame.getContentPane().setLayout(new FlowLayout());
        resultFrame.getContentPane().add(resultPanel);
        resultFrame.setLocation(0, 0);
        resultFrame.setTitle("Results are in lads.");
        resultFrame.setVisible(true);
        resultFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        resultFrame.pack();
    }

    private static int randomNumber(int upperBound, int lowerBound) {
        return ((int) (Math.random() * (upperBound - lowerBound)) + lowerBound);
    }
}
