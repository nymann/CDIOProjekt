package video;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import org.opencv.core.Core;
import org.opencv.core.CvType;
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
public class OpticalFlow implements Runnable {

	Point centerPoint;
	final double NOISE_FACTOR_X = 0.6;
	final double NOISE_FACTOR_Y = 1.4;
	private final int THRESHOLD = 60;
	private double avgLength;
	private AverageFlowVector avgVector;
	private ArrayList<FlowVector> flows;
	private Mat prev, next;

	// GUI-elementer
	private JFrame frame;
	private JLabel label;

	public OpticalFlow() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		centerPoint = new Point(640, 360);
	}

	public static void main(String[] args) {
		new Thread(new OpticalFlow()).start();
	}

	public void findFlows(BufferedImage img) {
		if (prev == null) {
			prev = bufferedImageToMat(img);
			return;
		} else
			next = bufferedImageToMat(img);
		flows = new ArrayList<FlowVector>();
		avgVector = new AverageFlowVector();
		long tid = System.currentTimeMillis();
		Mat grayImagePrev = new Mat();
		Mat grayImageNext = new Mat();
		MatOfByte status = new MatOfByte();
		MatOfFloat err = new MatOfFloat();
		MatOfPoint pointsPrev = new MatOfPoint();
		Imgproc.cvtColor(prev, grayImagePrev, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(grayImagePrev, grayImagePrev, THRESHOLD, THRESHOLD * 2);
		Imgproc.goodFeaturesToTrack(grayImagePrev, pointsPrev, 500, 0.1, 1);
		Imgproc.cvtColor(next, grayImageNext, Imgproc.COLOR_BGR2GRAY);
		Imgproc.Canny(grayImageNext, grayImageNext, THRESHOLD, THRESHOLD * 2);
		MatOfPoint2f pointsPrev2f = new MatOfPoint2f(pointsPrev.toArray());
		MatOfPoint2f pointsNext2f = new MatOfPoint2f();
		// Compute Optical Flow
		Video.calcOpticalFlowPyrLK(grayImagePrev, grayImageNext, pointsPrev2f, pointsNext2f, status, err);
		generateFlows(pointsPrev2f, pointsNext2f, status);
		calcAverageVectorLength();
		removeNoise();
		calcAverageVectorLength();
		computeAverageVector();
		// System.out.println("Average length = "+avgLength);
		// System.out.println("Average vector length = "+avgVector.getLength());
		// System.out.println("Average x = "+avgVector.x+", y = "+avgVector.y);
		// System.out.println("Antal vektorer = "+flows.size());
		// String filename = "/Users/Simon/Pictures/opticalFlows.png";
		// String filenameCanny = "/Users/Simon/Pictures/opticalCanny.png";
		// drawFlowLines(next);
		// Imgproc.arrowedLine(next, centerPoint, new Point(centerPoint.x +
		// avgVector.x, centerPoint.y + avgVector.y), new Scalar(0, 255, 255));
		// Imgcodecs.imwrite(filename, next);
		// Imgcodecs.imwrite(filenameCanny, grayImagePrev);
		determineMovement();
		// System.out.println("Tid = " + (System.currentTimeMillis() - tid));
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
			if (flows.get(i).getLength() >= avgLength * NOISE_FACTOR_X
					&& flows.get(i).getLength() <= avgLength * NOISE_FACTOR_Y)
				newFlows.add(flows.get(i));
		}
		flows = newFlows;
	}

	private void determineMovement() {
		if (Math.abs(avgVector.getLength() - avgLength) <= avgLength * 0.2) {
			System.out.println("Movement detected!");
			if (Math.abs(avgVector.x) > Math.abs(avgVector.y)) {
				if (avgVector.x > 0)
					System.out.println("Moved right");
				else
					System.out.println("Moved left");
			} else if (Math.abs(avgVector.x) <= Math.abs(avgVector.y)) {
				if (avgVector.y > 0)
					System.out.println("Moved backward");
				else
					System.out.println("Moved forward");
			}
		}
	}

	@Override
	public void run() {
		ImageCapture ic = new ImageCapture();
		frame = new JFrame("Optical Flow");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1260, 700));
		label = new JLabel();
//		Mat prevImg = ic.run();
//		Mat nextImg = ic.run();
//		Mat img = findFlows(prevImg, nextImg);
//		label.setIcon(new ImageIcon(matToBufferedImage(img)));
//		frame.add(label);
//		frame.pack();
//		frame.setVisible(true);
	}

	private BufferedImage matToBufferedImage(Mat matrix) {
		BufferedImage bimg = null;
		if (matrix != null) {
			int cols = matrix.cols();
			int rows = matrix.rows();
			int elemSize = (int) matrix.elemSize();
			byte[] data = new byte[cols * rows * elemSize];
			int type;
			matrix.get(0, 0, data);
			switch (matrix.channels()) {
			case 1:
				type = BufferedImage.TYPE_BYTE_GRAY;
				break;
			case 3:
				type = BufferedImage.TYPE_3BYTE_BGR;
				// bgr to rgb
				byte b;
				for (int i = 0; i < data.length; i = i + 3) {
					b = data[i];
					data[i] = data[i + 2];
					data[i + 2] = b;
				}
				break;
			default:
				return null;
			}

			// Reuse existing BufferedImage if possible
			if (bimg == null || bimg.getWidth() != cols || bimg.getHeight() != rows || bimg.getType() != type) {
				bimg = new BufferedImage(cols, rows, type);
			}
			bimg.getRaster().setDataElements(0, 0, cols, rows, data);
		} else { // mat was null
			bimg = null;
		}
		return bimg;
	}

	private Mat bufferedImageToMat(BufferedImage bi) {
		Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}

}