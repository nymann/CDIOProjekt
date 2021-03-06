/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package navigation;

import de.yadrone.base.video.ImageListener;
import java.awt.image.BufferedImage;
import modeling.MainModel;

/**
 *
 * @author Mikkel
 */
public class ImageDataListener implements ImageListener {

	private ImageData data;

	public ImageDataListener() {
	}

	@Override
	public void imageUpdated(BufferedImage bi) {
		this.data = new ImageData(
				bi, 
				MainModel.getDroneAttitude(),
				MainModel.getDronePosition(),
				System.currentTimeMillis()
		);
	}
	
	public ImageData getImageData(){
		return data;
	}

}
