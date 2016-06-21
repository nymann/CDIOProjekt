/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import de.yadrone.base.video.ImageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author Mikkel
 */
public class VideoPanel extends JPanel implements ImageListener {

    BufferedImage image = null;

    public VideoPanel() {
    }

    @Override
    public void imageUpdated(BufferedImage bi) {
        this.image = bi;
        //this.setSize(bi.getWidth(), bi.getHeight());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
        }
		int cx = this.getWidth()/2;
		int cy = this.getHeight()/2;
		
		int size = 10;
		g.setColor(Color.GREEN);
		g.drawLine(cx, cy-size, cx, cy+size);
		g.drawLine(cx-size, cy, cx+size, cy);
    }
}
