package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by Nymann on 20-06-2016.
 */
public class ResultPanel extends JPanel {
    int cubeWidth = 5;
    int cubeHeight = 5;

    private ArrayList<Point> greenCubes;
    private ArrayList<Point> redCubes;

    public ResultPanel(ArrayList<Point> greenCubes, ArrayList<Point> redCubes) {
        this.greenCubes = greenCubes;
        this.redCubes = redCubes;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, this.getWidth(), this.getHeight()); // Draw the entire frame dark gray.

        for (Point cube: greenCubes) {
            g.setColor(Color.green);
            g.fillRect((int) cube.getX(), (int) cube.getY(), cubeWidth, cubeHeight);
        }

        for (Point cube: redCubes) {
            g.setColor(Color.red);
            g.fillRect((int) cube.getX(), (int) cube.getY(), cubeWidth, cubeHeight);
        }
    }
}
