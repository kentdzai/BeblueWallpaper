package com.soft.kent.bebluewallpaper.model;

/**
 * Created by QuyetChu on 5/20/16.
 */
public class ImageLandScape {
    String imageResolution;
    String linkDown;

    public void setImageResolution(String imageResolution) {
        this.imageResolution = imageResolution;
    }

    public String getImageResolution() {
        return imageResolution;
    }

    public void setLinkDown(String linkDown) {
        this.linkDown = linkDown;
    }

    public String getLinkDown() {
        return linkDown;
    }

    @Override
    public String toString() {
        return "ImageLandScape{" +
                "imageResolution='" + imageResolution + '\'' +
                ", linkDown='" + linkDown + '\'' +
                '}';
    }
}

