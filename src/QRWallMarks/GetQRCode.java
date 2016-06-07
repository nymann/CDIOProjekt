package QRWallMarks;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import de.yadrone.base.video.ImageListener;
import org.opencv.core.Mat;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.Hashtable;

public class GetQRCode extends JPanel {
    BufferedImage image;

    public GetQRCode(BufferedImage img) {
        image = img;
    }

    public GetQRCode() {
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public static QRInfo readQRCode(BufferedImage qrcodeImage) {
        QRInfo qrInfo = new QRInfo();
        Hashtable<DecodeHintType, Object> hintMap = new Hashtable<>();
        hintMap.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(qrcodeImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(bitmap, hintMap);
        } catch (NotFoundException e) {
            qrInfo.error = "QR not found. Might have been partially " +
                    "detected but could not be confirmed.";
        } catch (ChecksumException e) {
            qrInfo.error = "Successfully detected and decoded, but was not " +
                    "returned because its checksum feature failed.";
        } catch (FormatException e) {
            qrInfo.error = "Detected, but some aspect did not conform " +
                    "to the format rules.";
        }
        assert result != null;
        qrInfo.name = result.getText();
        QRCodeCoordinates qrCodeCoordinates = new QRCodeCoordinates(result
                .getResultPoints());
        qrInfo.x = (int)qrCodeCoordinates.getXCenter();
        qrInfo.y = (int)qrCodeCoordinates.getYCenter();
        return qrInfo;
    }
}
