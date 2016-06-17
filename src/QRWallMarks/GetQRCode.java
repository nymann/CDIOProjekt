package QRWallMarks;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

public class GetQRCode {

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
			return qrInfo;
        } catch (ChecksumException e) {
            qrInfo.error = "Successfully detected and decoded, but was not " +
                    "returned because its checksum feature failed.";
			return qrInfo;
        } catch (FormatException e) {
            qrInfo.error = "Detected, but some aspect did not conform " +
                    "to the format rules.";
			return qrInfo;
        }

		assert result != null;
		
        qrInfo.name = result.getText(); // This line produces an error.
        
        QRCodeCoordinates qrCodeCoordinates = new QRCodeCoordinates(result
                .getResultPoints());
        qrInfo.x = (int) qrCodeCoordinates.getXCenter();
        qrInfo.y = (int) qrCodeCoordinates.getYCenter();
        return qrInfo;
    }
}