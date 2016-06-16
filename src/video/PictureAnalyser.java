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

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Joachim Knudsen
 */
public class PictureAnalyser {
	static Mat pic;
	public List<Scalar> color;

	public PictureAnalyser() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		this.color = new ArrayList<>();
		this.color.add(new Scalar(0,0,0));
		this.color.add(new Scalar(179,255,255));
	}

	public void setColor(List<Point> coloranalyse) {
		this.color.add(new Scalar(coloranalyse.get(0).x, coloranalyse.get(1).x,
				coloranalyse.get(2).x));
		this.color.add(new Scalar(coloranalyse.get(0).y, coloranalyse.get(1).y,
				coloranalyse.get(2).y));
	}


	public List<Point> getAnalyse(BufferedImage img) {
	//public BufferedImage getAnalyse(BufferedImage img) {

		Mat frameMat = new Mat();
		frameMat = PictureView.bufferedImageToMat(img);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat frameBlur = new Mat();
		pic = blur(frameMat, 5);
		Mat imgHSV = new Mat();
		Imgproc.cvtColor(pic, imgHSV, Imgproc.COLOR_RGB2HSV);
		contours = getConturs(color.get(0), color.get(1), imgHSV);
		
		return getblocks(contours);
		/*Imgproc.cvtColor(imgHSV, pic, Imgproc.COLOR_HSV2RGB);
		Imgproc.circle(pic, new Point(pic.width() / 2,
				pic.height() / 2), 4, new Scalar(255, 49, 255, 255));
		 img = PictureView.mat2Img(pic);
		 return img;
	*/}

	public  List<Point> getblocks(List<MatOfPoint> contours) {
		List<Moments> mu = new ArrayList<Moments>(contours.size());
		List<Point> cubepoint = new ArrayList<Point>();
		int counter = 0;
		for (int i = 0; i < contours.size(); i++) {
			mu.add(i, Imgproc.moments(contours.get(i), false));
			Moments p = mu.get(i);
			int x = (int) (p.get_m10() / p.get_m00());
			int y = (int) (p.get_m01() / p.get_m00());
			cubepoint.add(new Point((x), (y)));

			Imgproc.circle(pic, new Point(x, y), 4, new Scalar(255, 49, 0, 255));
		}
		return cubepoint;
	}

	public List<MatOfPoint> getConturs(Scalar low, Scalar high, Mat img) {

		Mat imgThresholded = new Mat();
		Mat imgThresholded2 = new Mat();
		Core.inRange(img, low, high, imgThresholded);
		if (low.val[0] < 0) {
			low.val[0] = 180 - low.val[0];
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

			// Convert contours(i) from MatOfPoint to MatOfPoint2f
			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
			// Processing on mMOP2f1 which is in type MatOfPoint2f

			double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
			Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

			// Convert back to MatOfPoint
			MatOfPoint points = new MatOfPoint(approxCurve.toArray());

			// Get bounding rect of contour
			Rect rect = Imgproc.boundingRect(points);
			int area = (rect.width) * (rect.height);
			//System.out.println("area = " + area);
			if (area > 500) {
				// draw enclosing rectangle (all same color, but you could use
				// variable i to make them unique)
				Imgproc.rectangle(pic, new Point(rect.x, rect.y), new Point(
						rect.x + rect.width, rect.y + rect.height), new Scalar(
						255, 0, 0, 255), 3);
			} else {
				contours.remove(i);
				i--;
			}
		}

		return contours;
	}

	public Mat blur(Mat input, int numberOfTimes) {
		Mat sourceImage = new Mat();
		Mat destImage = input.clone();
		for (int i = 0; i < numberOfTimes; i++) {
			sourceImage = destImage.clone();
			Imgproc.blur(sourceImage, destImage, new Size(5.0, 5.0));
		}
		return destImage;
	}

	public  boolean detect(Mat img) {
		CascadeClassifier face_cascade = new CascadeClassifier();
		if (!face_cascade.load("pic/lbpcascade_frontalface.xml")) {
			return false;
		}
		MatOfRect faceDetections = new MatOfRect();
		face_cascade.detectMultiScale(img, faceDetections);
		System.out.println(String.format("Detected %s faces",
				faceDetections.toArray().length));
		if (faceDetections.toArray().length == 0) {
			return false;
		}
		// for (Rect rect : faceDetections.toArray()) {
		// Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
		// + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		// }

		return true;
	}

	 public void Calibrate(BufferedImage img){
	//public static BufferedImage Calibrate(BufferedImage img) {

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
				if (testColor[0] > 174)
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

		Scalar high = new Scalar(hue + 5, saturation + 40, value + 40);
		Scalar low = new Scalar(hue - 5, saturation - 40, value - 40);
		List<Point> color = new ArrayList<>();
		getConturs(low, high, imgHSV);
		Imgproc.rectangle(pic, new Point(imgHSV.width() / 2 - width / 2,
				imgHSV.height() / 2 - height / 2), new Point(imgHSV.width() / 2
				+ width / 2, imgHSV.height() / 2 + height / 2), new Scalar(hue,
				saturation, value));
		Imgproc.circle(pic, new Point(imgHSV.width() / 2,
				imgHSV.height() / 2), 4, new Scalar(255, 49, 255, 255));
		this.color.get(0).val[0]=hue - 5;
		this.color.get(0).val[1]=saturation - 40;
		this.color.get(0).val[2]=value - 40;
		this.color.get(1).val[0]=hue + 5;
		this.color.get(1).val[1]=saturation + 40;
		this.color.get(1).val[2]=value + 40;
		System.out.println("test the system:" + hue + " " + saturation + " "
				+ value);
		
	}

}



