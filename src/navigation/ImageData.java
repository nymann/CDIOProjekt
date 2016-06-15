/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navigation;

import java.awt.image.BufferedImage;
import javafx.geometry.Point3D;
import modeling.Angle3D;

/**
 *
 * @author Mikkel
 */
public class ImageData {
	
	public BufferedImage image;
	public Angle3D attitude;
	public Point3D position;
	public long time;

	ImageData(
			BufferedImage image, 
			Angle3D droneAttitude,
			Point3D position,
			long time
		) {
		this.image = image;
		this.attitude = droneAttitude;
		this.position = position;
		this.time = time;
	}
	
}
