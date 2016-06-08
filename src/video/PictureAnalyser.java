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
public class PictureAnalyser {
	static int counterG = 0 ;
	static int counterR = 0 ;
	static Mat pic ;
    static List<MatOfPoint> contoursGreen = new ArrayList<MatOfPoint>(); 
    static List<MatOfPoint> contoursRed = new ArrayList<MatOfPoint>(); 
    private List<Scalar> color;
    
	public void init() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	public void setColor(List<Point> coloranalyse) {
		this.color.add(new Scalar(coloranalyse.get(0).x,coloranalyse.get(1).x,coloranalyse.get(2).x));
		this.color.add(new Scalar(coloranalyse.get(0).y,coloranalyse.get(1).y,coloranalyse.get(2).y));
	}
	public List<Point> getAnalyse(BufferedImage img) {

		Mat frameMat = new Mat();
		frameMat = bufferedImageToMat(img);
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat frameBlur = new Mat();
		pic = blur(frameMat, 5);
		Mat imgHSV = new Mat();
		Mat imgThresholded = new Mat();
		Imgproc.cvtColor(pic, imgHSV, Imgproc.COLOR_BGR2HSV);
		Scalar high = new Scalar(125, 255, 255);
		Scalar low = new Scalar(100, 40, 40);
		Core.inRange(imgHSV, low, high, imgThresholded);
		int dilation_size = 2;
		Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2 * dilation_size + 1, 2 * dilation_size + 1));
		Imgproc.dilate(imgThresholded, imgThresholded, element1);
		Imgproc.findContours(imgThresholded, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
		MatOfPoint2f approxCurve = new MatOfPoint2f();
		for (int i = 0; i < contours.size(); i++) {
			//Convert contours(i) from MatOfPoint to MatOfPoint2f
			MatOfPoint2f contour2f = new MatOfPoint2f(contours.get(i).toArray());
			//Processing on mMOP2f1 which is in type MatOfPoint2f
			double approxDistance = Imgproc.arcLength(contour2f, true) * 0.02;
			Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);
			//Convert back to MatOfPoint
			MatOfPoint points = new MatOfPoint(approxCurve.toArray());
			// Get bounding rect of contour
			Rect rect = Imgproc.boundingRect(points);
			// draw enclosing rectangle (all same color, but you could use variable i to make them unique)
			Imgproc.rectangle(frameBlur, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 0, 0, 255), 3);
		}
      /*  Scalar highG =new Scalar(68,255,150);
        Scalar lowG =new Scalar(38,125,35);
        
        Scalar highR =new Scalar(20,255,150);
        Scalar lowR =new Scalar(0,125,0);
      
      */
		contoursRed = getConturs(color.get(0),color.get(1), imgHSV); 
		return getblocks(contoursRed);
    //  contoursGreen = getConturs(lowG, highG, imgHSV);  

	//	img = mat2Img(frameBlur);
	//	System.out.println("count Green: "+ counterG+" count Red: " +counterR);
	//	return img;
	}
	   public static List<Point> getblocks ( List<MatOfPoint> contours ){
	        List<Moments> mu = new ArrayList<Moments>(contours.size());
	       // List<Moments> muOld = new ArrayList<Moments>(contours.size());
	        List<Point> cubepoint = new ArrayList<Point>();
	        int counter = 0;
	         for (int i = 0; i < contours.size(); i++) {
	                    mu.add(i, Imgproc.moments(contours.get(i), false));  
	                    Moments p = mu.get(i);
	                    int x = (int) (p.get_m10() / p.get_m00()); 
	                    int y = (int) (p.get_m01() / p.get_m00());
	                  //              int xMid = pic.cols()/2;
	                    //int yMid = pic.rows()/2;
	             //System.out.println("x,y : "+(x-xMid)+" "+(y-yMid));
	             cubepoint.add(new Point((x),(y)));
	        
	             Imgproc.circle(pic, new Point(x, y), 4, new Scalar(255,49,0,255));   
	    }
	    return cubepoint;
	}
	   public static List<MatOfPoint> getConturs ( Scalar low, Scalar high, Mat img  ){
		     
           
	        Mat imgThresholded = new Mat();
	       Core.inRange(img, low, high, imgThresholded);
	       
	       
	       List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	         int dilation_size = 3;
	                   Mat element1 = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new  Size(2*dilation_size + 1, 2*dilation_size+1));
	                  Imgproc.dilate(imgThresholded, imgThresholded, element1);
	                Imgproc.findContours(imgThresholded,contours,new Mat(), Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
	                 MatOfPoint2f         approxCurve = new MatOfPoint2f();
	       
	       
	       for (int i=0; i<contours.size(); i++) {
	                    
	        //Convert contours(i) from MatOfPoint to MatOfPoint2f
	        MatOfPoint2f contour2f = new MatOfPoint2f( contours.get(i).toArray() );
	        //Processing on mMOP2f1 which is in type MatOfPoint2f
	        
	        double approxDistance = Imgproc.arcLength(contour2f, true)*0.02;
	        Imgproc.approxPolyDP(contour2f, approxCurve, approxDistance, true);

	        //Convert back to MatOfPoint
	        MatOfPoint points = new MatOfPoint( approxCurve.toArray() );

	        // Get bounding rect of contour
	        Rect rect = Imgproc.boundingRect(points);
	        int area = (rect.width)*(rect.height);
	                    System.out.println("area = " + area);
	        if(area >500){
	         // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
	       Imgproc.rectangle(pic, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3);
	        
	        }else{
	            contours.remove(i);
	            i--;
	        }
	         //Imgproc.drawContours( blurred, contours, i, color, 2, 8, hierarchy, 0, Point() );
	                }
	       
	       
	    return contours;
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

	public static Image mat2Image(Mat frame) {
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
	}

	public static Mat blur(Mat input, int numberOfTimes) {
		Mat sourceImage = new Mat();
		Mat destImage = input.clone();
		for (int i = 0; i < numberOfTimes; i++) {
			sourceImage = destImage.clone();
			Imgproc.blur(sourceImage, destImage, new Size(5.0, 5.0));
		}
		return destImage;
	}

	public static boolean detect(Mat img) {
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
		//         for (Rect rect : faceDetections.toArray()) {
		//           Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
		//                   + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		//       }

		return true;
	}
	   double Distance_BtwnPoints(Point p, Point q)
	   {
	       double X_Diff = p.x - q.x;
	       double Y_Diff = p.y - q.y;
	       return Math.sqrt((X_Diff * X_Diff) + (Y_Diff * Y_Diff));
	   }

}

