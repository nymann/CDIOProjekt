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
    private final double accScalePhys = 100;
	private final double accScaleRaw = 1000;
	

    public Point3D velocity, accelerationPhys, accelerationRaw;
    public long updated;
    
	private Point2D counterVelocity;
	private boolean stableH,stableV;
	
    @Override
    protected void paintComponent(Graphics g) {
		int h = this.getHeight();
		int w = this.getWidth();
		
        int centerX = w / 2;
        int centerY = h / 2;
		
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w, h);
		int shortest = Math.min(w, h);
        if (this.velocity != null) {
            g.setColor(Color.GREEN);
            double factor = shortest / scale;
            g.drawLine(centerX, centerY, centerX + (int) (factor * velocity.getX()), centerY - (int) (factor * velocity.getY()));
        }
        if (this.counterVelocity != null) {
            g.setColor(Color.RED);
            double factor = shortest / counterScale;
            g.drawLine(centerX, centerY, centerX + (int) (factor * counterVelocity.getX()), centerY - (int) (factor * counterVelocity.getY()));
        }

		if (this.accelerationPhys != null) {
            g.setColor(Color.YELLOW);
            double factor = shortest / accScalePhys;
            g.drawLine(centerX, centerY, centerX + (int) (factor * accelerationPhys.getX()), centerY - (int) (factor * accelerationPhys.getY()));
        }

		if (this.accelerationRaw != null) {
            g.setColor(Color.BLUE);
            double factor = shortest / accScaleRaw;
            g.drawLine(centerX, centerY, centerX + (int) (factor * accelerationPhys.getX()), centerY - (int) (factor * accelerationPhys.getY()));
        }

		
		if (this.stableH){
            g.setColor(Color.GREEN);
		} else {
            g.setColor(Color.RED);
		}
		g.drawString("H", 5, h-5);
		
		if (this.stableV){
            g.setColor(Color.GREEN);
		} else {
            g.setColor(Color.RED);
		}
		g.drawString("V", 20, h-5);
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
	
	public void setStabilityH(boolean stable){
		this.stableH = stable;
		this.repaint();
	}

	public void setStabilityV(boolean stable){
		this.stableV = stable;
		this.repaint();
	}
	
	public void setAccelPhys(float[] v){
		this.accelerationPhys = new Point3D(v[1], v[0],v[2]);
	}

	public void setAccelRaw(int[] v){
		this.accelerationRaw = new Point3D(v[1], v[0],v[2]);
	}
	
	
}
