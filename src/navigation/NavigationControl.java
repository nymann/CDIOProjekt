package navigation;

import java.awt.image.BufferedImage;

import javafx.geometry.Point3D;
import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;
import modeling.MainModel;
import video.VideoReader;

/**
 *
 * @author Kim
 */

public class NavigationControl {
 
	private VideoManager vm;
	private CommandManager cm;
	private MainModel mm;
	private NavFlyPattern flyPat;
	private NavFindPosition findPos;
	private ImageDataListener idl;
	private IARDrone drone;
	private BufferedImage buffI;
	private Point3D pos3d;

	NavigationControl(ImageDataListener idl){
		vm = drone.getVideoManager();
		cm = drone.getCommandManager();
		cm.setControlAck(true);
		findPos = new NavFindPosition(idl, drone);
		flyPat = new NavFlyPattern(idl, drone);
		
		runNav();
		presentResults();
	}

	private void runNav(){
		
		pos3d = mm.getDronePosition();
		
		double xPos = pos3d.getX();
		double yPos = pos3d.getY();

		try {
			for (int i=0; i<14; i++) {
				/*flyPat.flyToSpot(xPos, yPos, i);
				if(flyPat.atSpot(i)) flyPat.flyLane(i, i+1);
				else flyPat.flyToSpot(xPos, xPos, i);*/
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
	}

	private void presentResults(){
		/*
		 * Ska gemme resultaterne vdr cubes i en fil
		 */

	}
}
