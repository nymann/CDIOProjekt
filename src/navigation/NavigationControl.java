package navigation;

import de.yadrone.base.IARDrone;
import de.yadrone.base.command.CommandManager;
import de.yadrone.base.video.VideoManager;
import gui.ResultPanel;
import javafx.geometry.Point3D;
import javassist.expr.NewArray;
import modeling.MainModel;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
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
    private ArrayList<Point> redCubes, greenCubes;
    private ResultPanel rp;

    public NavigationControl(ImageDataListener idl) {
        vm = drone.getVideoManager();
        cm = drone.getCommandManager();
        cm.setControlAck(true);
        findPos = new NavFindPosition(idl, drone);
        flyPat = new NavFlyPattern(idl, drone);
        
        runNav();
        presentResults();
    }

    private void runNav() {
        pos3d = MainModel.getDronePosition();

        double xPos = pos3d.getX();
        double yPos = pos3d.getY();

        try {
            for (int i = 0; i < 14; i++) {
                flyPat.flyToSpot(i);
                if (flyPat.atSpot(i)) flyPat.flyLane(i, i + 1);
                else flyPat.flyToSpot(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void presentResults() {
        /*
         * Tager listerne med red og green cubes, som fundet i NavFlyPattern,
         * og sender dem til resultPanel.
         * Opretter så to filer med resultaterne
		 */
    	greenCubes = new ArrayList<>();
	    redCubes = new ArrayList<>();
	    
    	rp = new ResultPanel(greenCubes, redCubes);
    	
    	FileOutputStream fosGreen = new FileOutputStream("greenCubes.txt");
        ObjectOutputStream oosGreen = new ObjectOutputStream(fosGreen);   
        oosGreen.writeObject(greenCubes);
        oosGreen.close(); 
        
    	FileOutputStream fosRed = new FileOutputStream("RedCubes.txt");
        ObjectOutputStream oosRed = new ObjectOutputStream(fosRed);   
        oosRed.writeObject(redCubes);
        oosRed.close(); 
    }
}
