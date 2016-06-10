package navigation;

import java.awt.image.BufferedImage;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.video.ImageListener;
import de.yadrone.base.video.VideoManager;
import video.VideoReader;

/**
 *
 * @author Kim
 */

public class NavigationControl {
 
	private VideoManager vm;
	private CommandManager cm;
	private NavFlyPattern flyPat;
	private NavFindPosition findPos;
	private video.VideoReader vr;
	private IARDrone drone;
	private BufferedImage buffI;

	NavigationControl(ImageListener imgList){
		findPos = new NavFindPosition();
		flyPat = new NavFlyPattern();
		vm = drone.getVideoManager();
		cm = drone.getCommandManager();
		vr = new VideoReader(vm, cm);
		
		runNav();
		presentResults();
	}

	private void runNav(){
		double xPos = findPos.getPositionX();
		double yPos = findPos.getPositionY();

		flyPat.flyToSpot(xPos, yPos, 0);

		if(flyPat.atSpot(0)) flyPat.flyLane(0, 1);
		else flyPat.flyToSpot(xPos, xPos, 0);
		
		
		
	}

	private void presentResults(){
		

	}
}
