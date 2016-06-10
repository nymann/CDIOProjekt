package QRWallMarks;

/**
 * Created by Nymann on 06-06-2016.
 */
public class QRInfo {
    public String name;
    public String error;
    public int x,y;
    public double angle;
    public Boolean qRCodeFoundInCurrentImage;

    public QRInfo() {
        this.name = "";
        this.error = "";
        this.x = 0;
        this.y = 0;
        this.qRCodeFoundInCurrentImage = false;
    }
}