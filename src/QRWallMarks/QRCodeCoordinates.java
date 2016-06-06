package QRWallMarks;

import com.google.zxing.ResultPoint;

/**
 * Created by Nymann on 06-06-2016.
 */
public class QRCodeCoordinates
{
    public float X1;
    public float X2;
    public float Y1;
    public float Y2;

    public QRCodeCoordinates(ResultPoint[] resultPoints)
    {
        this.X1 = resultPoints[0].getX(); // index 0: bottom left
        this.X2 = resultPoints[2].getX(); // index 2: top right
        this.Y1 = resultPoints[2].getY(); // index 2: top right
        this.Y2 = resultPoints[0].getY(); // index 0: bottom left
    }

    public float getXCenter() {
        return (this.X1 + this.X2)/2;
    }

    public float getYCenter() {
        return (this.Y1 + this.Y2)/2;
    }
}
