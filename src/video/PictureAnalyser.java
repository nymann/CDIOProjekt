/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joachim Knudsen
 */
public class PictureAnalyser {

	//tænk som man kunne hive billede ud så man kunne se hvad der er blevet analyseret
	static Mat pic;
	//sætte farverne til analysen
	public List<Scalar> color;

	//sætter oprettelse af Picture analyse
	public PictureAnalyser() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.color = new ArrayList<>();
		this.color.add(new Scalar(0,0,0));
		this.color.add(new Scalar(179,255,255));
	}
	// funktion der ikke bruges men kan sætte farven på analysen

	public void setColor(List<Point> coloranalyse) {
		this.color.add(new Scalar(coloranalyse.get(0).x, coloranalyse.get(1).x,
				coloranalyse.get(2).x));
		this.color.add(new Scalar(coloranalyse.get(0).y, coloranalyse.get(1).y,
				coloranalyse.get(2).y));
	}

	// returnere klodser i et billede

	public List<Point> getAnalyse(BufferedImage img) {
		Mat frameMat = new Mat();
		frameMat = PictureView.bufferedImageToMat(img);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat frameBlur = new Mat();
		pic = blur(frameMat, 5);
		Mat imgHSV = new Mat();
		Imgproc.cvtColor(pic, imgHSV, Imgproc.COLOR_RGB2HSV);
		contours = getConturs(color.get(0), color.get(1), imgHSV);
		
		return getblocks(contours);
	}

	// returnere klodser ud fra contours og bestemmer deres midpunkt
	private  List<Point> getblocks(List<MatOfPoint> contours) {
		List<Moments> mu = new ArrayList<Moments>(contours.size());
		List<Point> cubepoint = new ArrayList<Point>();
		for (int i = 0; i < contours.size(); i++) {
			mu.add(i, Imgproc.moments(contours.get(i), false));
			Moments p = mu.get(i);
			int x = (int) (p.get_m10() / p.get_m00());
			int y = (int) (p.get_m01() / p.get_m00());
			cubepoint.add(new Point((x), (y)));
		}
		return cubepoint;
	}
	// finder contours i billedet ud fra en maske 

	public List<MatOfPoint> getConturs(Scalar low, Scalar high, Mat img) {

		Mat imgThresholded = new Mat();
		Mat imgThresholded2 = new Mat();
		Core.inRange(img, low, high, imgThresholded);
		if (low.val[0] < 0) {
			low.val[0] = 180 + low.val[0];
			high.val[0] = 179;

			Core.inRange(img, low, high, imgThresholded2);
			Core.bitwise_or(imgThresholded, imgThresholded2, imgThresholded);

		}
		if (high.val[0] > 179) {
			low.val[0] = 0;
			high.val[0] = high.val[0] - 180;

			Core.inRange(img, low, high, imgThresholded2);
			Core.bitwise_or(imgThresholded, imgThresholded2, imgThresholded);
		}

		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		int dilation_size = 3;
		Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,
				new Size(2 * dilation_size + 1, 2 * dilation_size + 1));
		Imgproc.dilate(imgThresholded, imgThresholded, element1);
		Imgproc.findContours(imgThresholded, contours, new Mat(),
				Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		MatOfPoint2f approxCurve = new MatOfPoint2f();

		for (int i = 0; i < contours.size(); i++) {
			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
			double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
			Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
			MatOfPoint points = new MatOfPoint(approxCurve.toArray());
			Rect rect = Imgproc.boundingRect(points);
			int area = (rect.width) * (rect.height);
			//tester og arealet er for småt
			if (area > 500) {
			} else {
				contours.remove(i);
				i--;
			}
		}

		return contours;
	}
	// sætter et blur filter på et billede
	public Mat blur(Mat input, int numberOfTimes) {
		Mat sourceImage = new Mat();
		Mat destImage = input.clone();
		for (int i = 0; i < numberOfTimes; i++) {
			sourceImage = destImage.clone();
			Imgproc.blur(sourceImage, destImage, new Size(5.0, 5.0));
		}
		return destImage;
	}
	// sætter farven på analyseren
	 public void Calibrate(BufferedImage img){
		Mat frameMat = new Mat();
		frameMat = PictureView.bufferedImageToMat(img);
		pic = blur(frameMat, 3);
		Mat imgHSV = new Mat();
		Imgproc.cvtColor(pic, imgHSV, Imgproc.COLOR_RGB2HSV);
		int height = 10;
		int width = 10;
		int hue = 0;
		int saturation = 0;
		int value = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				double[] testColor = imgHSV.get((imgHSV.height() / 2)
						- (width / 2) + i, (imgHSV.width() / 2) - (height / 2)
						+ j);
				if (testColor[0] > 140)
					testColor[0] = testColor[0] - 180;
				hue = hue + (int) testColor[0];
				saturation = saturation + (int) testColor[1];
				value = value + (int) testColor[2];

			}
		}
		/*
		 * Green values hue=55; saturation =240; value= 70;
		 */
		hue = hue / (height * width);
		saturation = saturation / (height * width);
		value = value / (height * width);
		this.color.get(0).val[0]=hue - 5;
		this.color.get(0).val[1]=saturation - 40;
		this.color.get(0).val[2]=value - 40;
		this.color.get(1).val[0]=hue + 5;
		this.color.get(1).val[1]=saturation + 40;
		this.color.get(1).val[2]=value + 40;

		
	}

}



