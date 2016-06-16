
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import javafx.scene.image.Image;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.objdetect.CascadeClassifier;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JoachimØstergaard
 */
public class PictureView {
	static int counterG = 0 ;
	static int counterR = 0 ;
	static Mat pic ;
    private List<Scalar> color;

	public static void init() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public static BufferedImage setSize(BufferedImage img,double x, double y) {
		Mat imgMat;
		imgMat = bufferedImageToMat(img);
		Imgproc.resize(imgMat, imgMat, new Size(x, y));
		img= mat2Img(imgMat);
		return img;
	}


	public static BufferedImage mat2Img(Mat in) {
		BufferedImage out;
		int width = in.cols();
		int height = in.height();
		byte[] data = new byte[width * height * (int) in.elemSize()];
		int type;
		in.get(0, 0, data);

		if (in.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}

		out = new BufferedImage(width, height, type);

		out.getRaster().setDataElements(0, 0, width, height, data);
		return out;
	}
	//set it to mat

	public static Mat bufferedImageToMat(BufferedImage in) {
		Mat out;
		byte[] data;
		int r, g, b;
		int height = in.getHeight();
		int width = in.getWidth();
		if (in.getType() == BufferedImage.TYPE_INT_RGB || in.getType() == BufferedImage.TYPE_INT_ARGB) {
			out = new Mat(height, width, CvType.CV_8UC3);
			data = new byte[height * width * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
			for (int i = 0; i < dataBuff.length; i++) {
				data[i * 3 + 2] = (byte) ((dataBuff[i] >> 16) & 0xFF);
				data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
				data[i * 3] = (byte) ((dataBuff[i]) & 0xFF);
			}
		} else if (in.getType() == BufferedImage.TYPE_3BYTE_BGR) {
			out = new Mat(height, width, CvType.CV_8UC3);
			data = new byte[height * width * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
			for (int i = 0; i < dataBuff.length; i++) {
				data[i * 3 + 2] = (byte) ((dataBuff[i]) & 0xFF);
				data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
				data[i * 3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
			}
		} else {
			out = new Mat(height, width, CvType.CV_8UC1);
			data = new byte[height * width * (int) out.elemSize()];
			int[] dataBuff = in.getRGB(0, 0, width, height, null, 0, width);
			for (int i = 0; i < dataBuff.length; i++) {
				r = (byte) ((dataBuff[i] >> 16) & 0xFF);
				g = (byte) ((dataBuff[i] >> 8) & 0xFF);
				b = (byte) ((dataBuff[i]) & 0xFF);
				data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b)); //luminosity
			}
		}
		out.put(0, 0, data);
		return out;
	}

}
