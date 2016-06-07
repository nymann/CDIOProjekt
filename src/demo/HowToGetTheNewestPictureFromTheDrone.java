package demo;

import video.VideoReader;

import java.awt.image.BufferedImage;

/**
 * Created by Nymann on 07-06-2016.
 */
public class HowToGetTheNewestPictureFromTheDrone {
    private BufferedImage imageFromDrone;
    private VideoReader vr;

    public HowToGetTheNewestPictureFromTheDrone(VideoReader vr) {
        this.vr = vr;
    }

    private void getLatestImageFromDrone() {
        imageFromDrone = vr.getImage();
    }
}
