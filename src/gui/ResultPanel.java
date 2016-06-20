package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Nymann on 20-06-2016.
 */
public class ResultPanel extends JPanel {
    int height = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    int width = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    int cubeWidth = 5;
    int cubeHeight = 5;

    private ArrayList<Point2D> greenCubes;
    private ArrayList<Point2D> redCubes;

    public ResultPanel(ArrayList<Point2D> greenCubes, ArrayList<Point2D> redCubes) {
        this.greenCubes = greenCubes;
        this.redCubes = redCubes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, width, height); // Draw the entire frame dark gray.

        for (Point2D cube: greenCubes) {
            g.setColor(Color.green);
            g.drawRect((int) cube.getX(), (int) cube.getY(), cubeWidth, cubeHeight);
        }

        for (Point2D cube: redCubes) {
            g.setColor(Color.red);
            g.drawRect((int) cube.getX(), (int) cube.getY(), cubeWidth, cubeHeight);
        }
    }
}
