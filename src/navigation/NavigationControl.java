package navigation;

import java.awt.image.BufferedImage;

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
	private video.VideoReader vr;
	private IARDrone drone;
	private BufferedImage buffI;

	NavigationControl(ImageListener imgList){
		findPos = new NavFindPosition();
		flyPat = new NavFlyPattern();
		vm = drone.getVideoManager();
		cm = drone.getCommandManager();
		vr = new VideoReader(vm, cm);
		
//		mm = NavFlyPattern(vr, , mm);
		
		runNav();
		presentResults();
	}

	private void runNav(){
		
		double xPos = findPos.getPositionX();
		double yPos = findPos.getPositionY();
		
		try {
			for (int i=0; i<14; i++) {
				flyPat.flyToSpot(xPos, yPos, i);
				if(flyPat.atSpot(xPos, yPos, i)) flyPat.flyLane(i, i+1);
				else flyPat.flyToSpot(xPos, xPos, i);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
	}

	private void presentResults(){
		

	}
}
