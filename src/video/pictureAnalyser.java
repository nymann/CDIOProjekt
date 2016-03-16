/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package video;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

/**
 *
 * @author JoachimØstergaard
 */
public class pictureAnalyser {
    
    public pictureAnalyser(){
   System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        	// get the jpeg image from the internal resource folder
               //Mat image = Imgcodecs.imread(location);
		// convert the image in gray scale
//		     Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
		// write the new image on disk
		//Imgcodecs.imwrite("pic/Poli-tesh.jpg", imgThresholded);
                //Imgcodecs.imwrite("pic/Poli-blur.jpg", blurred);
                //Imgcodecs.imwrite("pic/Poli-hsv.jpg", imgHSV);
                
                //                  frame2.render(blurred);
		//System.out.println("Done!");
               //   capture.release();
        } 
    public BufferedImage getAnalyse(BufferedImage img){
          Mat frameMat = new Mat(); 
        frameMat =bufferedImageToMat(img);
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>(); 
        Mat frameBlur = new Mat();
        frameBlur = blur(frameMat,5);
        Mat imgHSV = new Mat();
        Mat imgThresholded = new Mat();
        Imgproc.cvtColor(frameBlur, imgHSV, Imgproc.COLOR_BGR2HSV); 
        Scalar high =new Scalar(179,255,255);
        Scalar low =new Scalar(160,40,40);        
        Core.inRange(imgHSV, low, high, imgThresholded);
	         int dilation_size = 2;
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
         // draw enclosing rectangle (all same color, but you could use variable i to make them unique)
        Imgproc.rectangle(frameBlur, new Point(rect.x,rect.y), new Point(rect.x+rect.width,rect.y+rect.height), new Scalar(255, 0, 0, 255), 3); 
                }
		
        img =mat2Img(frameBlur);
              
        return img;
    }
    public static BufferedImage mat2Img(Mat in)
    {
        BufferedImage out;
        byte[] data = new byte[320 * 240 * (int)in.elemSize()];
        int type;
        in.get(0, 0, data);

        if(in.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(320, 240, type);

        out.getRaster().setDataElements(0, 0, 320, 240, data);
        return out;
    } 
    //set it to mat
    public static Mat bufferedImageToMat(BufferedImage bi) {
  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
  mat.put(0, 0, data);
  return mat;
}
      	public static Image mat2Image(Mat frame)
	{
		// create a temporary buffer
		MatOfByte buffer = new MatOfByte();
		// encode the frame in the buffer, according to the PNG format
		Imgcodecs.imencode(".png", frame, buffer);
		// build and return an Image created from the image encoded in the
		// buffer
		return new Image(new ByteArrayInputStream(buffer.toArray()));
        }
           public static Mat blur(Mat input, int numberOfTimes){
        Mat sourceImage = new Mat();
        Mat destImage = input.clone();
        for(int i=0;i<numberOfTimes;i++){
            sourceImage = destImage.clone();
            Imgproc.blur(sourceImage, destImage, new Size(5.0, 5.0));
        }
        return destImage;
    }
   public static boolean detect(Mat img){
       CascadeClassifier face_cascade = new CascadeClassifier();
       if(!face_cascade.load("pic/lbpcascade_frontalface.xml"))return false;
       MatOfRect faceDetections = new MatOfRect();
       face_cascade.detectMultiScale( img, faceDetections);
        System.out.println(String.format("Detected %s faces",
                faceDetections.toArray().length));
        if(faceDetections.toArray().length ==0) return false;
 //         for (Rect rect : faceDetections.toArray()) {
 //           Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
 //                   + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
 //       }
        
  
        
       return true;
   }

             
    
}