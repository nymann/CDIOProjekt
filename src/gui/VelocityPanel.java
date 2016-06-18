/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.yadrone.base.navdata.VelocityListener;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikkel
 */
public class VelocityPanel extends JPanel implements VelocityListener {

    private final double scale = 5000;
    private final double counterScale = 20;

    public Point3D velocity;
    public long updated;
    private Point2D counterVelocity;

    @Override
    protected void paintComponent(Graphics g) {
        int centerX = this.getWidth() / 2;
        int centerY = this.getHeight() / 2;

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        if (this.velocity != null) {
            g.setColor(Color.GREEN);
            int shortest = Math.min(this.getWidth(), this.getHeight());
            double factor = shortest / scale;
            g.drawLine(centerX, centerY, centerX + (int) (factor * velocity.getX()), centerY - (int) (factor * velocity.getY()));
        }
        if (this.counterVelocity != null) {
            g.setColor(Color.RED);
            int shortest = Math.min(this.getWidth(), this.getHeight());
            double factor = shortest / counterScale;
            g.drawLine(centerX, centerY, centerX + (int) (factor * counterVelocity.getX()), centerY - (int) (factor * counterVelocity.getY()));
        }
    }

    @Override
    public void velocityChanged(float vy, float vx, float vz) {
        this.updated = System.currentTimeMillis();
        velocity = new Point3D(vx, vy, vz);
        this.repaint();
    }

    public void setCounterVelocity(Point2D counter) {
        this.counterVelocity = counter;
        this.repaint();
    }
}
