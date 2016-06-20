package gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Nymann on 20-06-2016.
 */
public class ResultPanel extends JPanel {
    int cubeWidth = 7;
    int cubeHeight = 7;

    int drawYOffset = -15;

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

        // Draw room stuff
        g.setColor(Color.WHITE);
        g.drawString("Wall 0", this.getWidth() / 2, 20);
        g.drawString("Wall 2", this.getWidth() / 2, this.getHeight() - 20);
        g.drawString("Wall 3", 0, this.getHeight() / 2);
        g.drawString("Wall 1", this.getWidth() - 50, this.getHeight() / 2);
        //

        for (Point cube : greenCubes) {
            g.setColor(Color.BLACK);
            g.fillRect((int) cube.getX(), (int) cube.getY(), cubeWidth + 1, cubeHeight + 1);
            g.setColor(Color.green);
            g.fillRect((int) cube.getX(), (int) cube.getY(), cubeWidth, cubeHeight);

            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
            g.drawString("(" + (int) cube.getX() + ", " + (int) cube.getY() + ")", (int) cube.getX() + drawYOffset - 1, (int) cube.getY() + 1);
            g.setColor(new Color(148, 235, 148));
            g.drawString("(" + (int) cube.getX() + ", " + (int) cube.getY() + ")", (int) cube.getX() + drawYOffset, (int) cube.getY());
        }

        for (Point cube : redCubes) {
            g.setColor(Color.BLACK);
            g.fillRect((int) cube.getX(), (int) cube.getY(), cubeWidth + 1, cubeHeight + 1);
            g.setColor(Color.red);
            g.fillRect((int) cube.getX(), (int) cube.getY(), cubeWidth, cubeHeight);

            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 10));
            g.drawString("(" + (int) cube.getX() + ", " + (int) cube.getY() + ")", (int) cube.getX() + drawYOffset - 1, (int) cube.getY() + 1);
            g.setColor(new Color(235, 191, 147));
            g.drawString("(" + (int) cube.getX() + ", " + (int) cube.getY() + ")", (int) cube.getX() + drawYOffset, (int) cube.getY());
        }
    }
}
