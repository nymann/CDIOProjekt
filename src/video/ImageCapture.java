package video;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class ImageCapture {

	public Mat[] run() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		System.out.println("Starting...");
		Mat[] r = new Mat[2];
		VideoCapture camera = new VideoCapture();
		camera.open(0);

		while (!camera.isOpened()) {
		}

		if (!camera.isOpened())
			System.out.println("Camera error!");
		else
			System.out.println("Camera OK!");

		Mat imagePrev = new Mat();
		camera.read(imagePrev);
		r[0] = imagePrev;
		try { Thread.sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
		Mat imageNext = new Mat();
		camera.read(imageNext);
		r[1] = imageNext;
		return r;
	}
}
