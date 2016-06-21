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

    private final double scale = 500;
    private final double counterScale = 20;
 	private final double accScaleRaw = 500;
	private final double velocityVScale = 500;
	private final double counterVScale = 20;
	

    public Point3D velocity, accelerationPhys, accelerationRaw;
    
	private Point2D counterVelocity;
	private boolean stableH,stableV;
	private float velocityV;
	private int	counterV;
	
    @Override
    protected void paintComponent(Graphics g) {
		int h = this.getHeight();
		int w1 = this.getWidth();
		w1 = w1*10/11;
		int w2 = w1/10;
		
        int centerX = w1 / 2;
        int centerY = h / 2;
		
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, w1+w2, h);
		int shortest = Math.min(w1, h);
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

		if (this.accelerationRaw != null) {
            g.setColor(Color.YELLOW);
            double factor = shortest / accScaleRaw;
            g.drawLine(centerX, centerY, centerX + (int) (factor * accelerationRaw.getX()), centerY - (int) (factor * accelerationRaw.getY()));
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
		
		
		g.setColor(Color.WHITE);
		g.drawLine(w1, centerY, w1+w2, centerY);
		
		g.setColor(Color.GREEN);
		double factor = h / velocityVScale;
        if (velocityV > 0){
			g.fillRect(w1, centerY, w2/2, (int) (factor*velocityV));
		} else {
			g.fillRect(w1, (int) (centerY+velocityV*factor), w2/2, (int) (-velocityV*factor));
		}
		g.setColor(Color.RED);
		factor = h / counterVScale;
		if (counterV > 0){
			g.fillRect(w1+w2/2, centerY, w2/2, (int) (factor*counterV));
		} else {
			g.fillRect(w1+w2/2, (int) (centerY+counterV*factor), w2/2, (int) (-counterV*factor));
		}
    }

    @Override
    public void velocityChanged(float vx, float vy, float vz) {
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
	
	public void setAccelRaw(int[] v){
		this.accelerationRaw = new Point3D(v[1], -v[0],v[2]);
	}

	public void setVelocityV(float velocityV) {
		this.velocityV = velocityV;
	}

	public void setCounterV(int counterV) {
		this.counterV = counterV;
	}
	
	
}
