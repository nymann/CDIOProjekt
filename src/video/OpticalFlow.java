package video;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.Video;

import modeling.AverageFlowVector;
import modeling.FlowVector;

/**
 * 
 * @author Simon
 *
 */
public class OpticalFlow {
	
	Point centerPoint;
	final double NOISE_FACTOR_X = 0.5;
	final double NOISE_FACTOR_Y = 1.5;
	private double avgLength;
	private AverageFlowVector avgVector;
	private ArrayList<FlowVector> flows;
	
	public OpticalFlow() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		centerPoint = new Point(640, 360);
		flows = new ArrayList<FlowVector>();
		avgVector = new AverageFlowVector();
	}
	
	public static void main(String[] args) {
		ImageCapture ic = new ImageCapture();
		OpticalFlow of = new OpticalFlow();
		Mat[] img = ic.run();
		of.findFlows(img[0], img[1]);
	}
	
	public void findFlows(Mat prev, Mat next) {
		Mat grayImagePrev = new Mat();
		Mat grayImageNext = new Mat();
		MatOfByte status = new MatOfByte();
		MatOfFloat err = new MatOfFloat();
		MatOfPoint pointsPrev = new MatOfPoint();
		Imgproc.cvtColor(prev, grayImagePrev, Imgproc.COLOR_BGR2GRAY);
		Imgproc.goodFeaturesToTrack(grayImagePrev, pointsPrev, 1000, 0.01, 1);
		Imgproc.cvtColor(next, grayImageNext, Imgproc.COLOR_BGR2GRAY);
		MatOfPoint2f pointsPrev2f = new MatOfPoint2f(pointsPrev.toArray());
		MatOfPoint2f pointsNext2f = new MatOfPoint2f();
		// Compute Optical Flow
		Video.calcOpticalFlowPyrLK(grayImagePrev, grayImageNext, pointsPrev2f, pointsNext2f, status, err);
		generateFlows(pointsPrev2f, pointsNext2f, status);
		calcAverageVectorLength();
		System.out.println("Average length = "+avgLength);
		System.out.println("Antal vektorer = "+flows.size());
		removeNoise();
		printVectors();
		calcAverageVectorLength();
		computeAverageVector();
		System.out.println("Average length = "+avgLength);
		System.out.println("Average vector length = "+avgVector.getLength());
		System.out.println("Average x = "+avgVector.x+", y = "+avgVector.y);
		System.out.println("Antal vektorer = "+flows.size());
		String filename = "/Users/Simon/Pictures/opticalFlows.png";
		System.out.println("Done. Writing " + filename);
		drawFlowLines(next);
		Imgproc.arrowedLine(next, centerPoint, new Point(centerPoint.x + avgVector.x, centerPoint.y + avgVector.y), new Scalar(0, 255, 255));
		Imgcodecs.imwrite(filename, next);
		System.out.println("Image saved");
		determineMovement();
	}
	
	// Computes the average vector length
	private void calcAverageVectorLength() {
		avgLength = flows.get(0).getLength();
		for (FlowVector v : flows) {
			avgLength += v.getLength();
		}
		avgLength /= flows.size();
	}
	
	// Determines the flow vectors
	private void generateFlows(MatOfPoint2f prev, MatOfPoint2f next, MatOfByte status) {
		double[] cornerPoints1;
		double[] cornerPoints2;
		for (int i = 0; i < status.rows(); i++) {
			int statusInt = (int) status.get(i, 0)[0];
			if (statusInt == 1) {
				cornerPoints1 = prev.get(i, 0);
				cornerPoints2 = next.get(i, 0);
				FlowVector newVector = new FlowVector(new Point(cornerPoints1[0], cornerPoints1[1]), 
						new Point(cornerPoints2[0], cornerPoints2[1]));
				flows.add(newVector);
			}
		}
	}
	
	private void computeAverageVector() {
		for (FlowVector v : flows) {
			avgVector.addVector(v);
		}
		avgVector.computeAverageVector(flows.size());
	}
	
	private void drawFlowLines(Mat next) {
		for (FlowVector v : flows) {
			Imgproc.arrowedLine(next, v.p1, v.p2, new Scalar(255, 100, 0));
		}
	}
	
	private void removeNoise() {
		ArrayList<FlowVector> newFlows = new ArrayList<FlowVector>();
		for (int i = 0; i < flows.size(); i++) {
			if (flows.get(i).getLength() >= avgLength*NOISE_FACTOR_X && flows.get(i).getLength() <= avgLength*NOISE_FACTOR_Y)
//				flows.remove(i);
				newFlows.add(flows.get(i));
		}
		flows = newFlows;
	}
	
	private void printVectors() {
		for (FlowVector v : flows) {
			System.out.println(v.getLength());
		}
	}
	
	private void determineMovement() {
		if (Math.abs(avgVector.getLength() - avgLength) <= avgLength * 0.2) {
			System.out.println("Movement detected!");
			if (isForwardMovement()) {
				System.out.println("Moved forward");
			} else if (isBackwardMovement()) {
				
			} else if (avgVector.x <= avgVector.y) {
				if (avgVector.y > 0) System.out.println("Moved left");
				else System.out.println("Moved right");
			}
		}
	}
	
	private boolean isForwardMovement() {
		int orientation = 0;
		for (FlowVector v : flows) {
			orientation += v.getOrientation(centerPoint);
		}
		return orientation < 0;
	}
	
	private boolean isBackwardMovement() {
		// TODO: Check for backward movement
		return false;
	}

}
