package com.soft.kent.bebluewallpaper.model;

/**
 * Created by QuyetChu on 5/20/16.
 */
public class ObjectDetailImage {
    String imageDisplay;
    String linkDownload;

    public void setImageDisplay(String imageDisplay) {
        this.imageDisplay = imageDisplay;
    }


    public String getImageDisplay() {
        return imageDisplay;
    }

    public void setLinkDownload(String linkDownload) {
        this.linkDownload = linkDownload;
    }

    public String getLinkDownload() {
        return linkDownload;
    }

    @Override
    public String toString() {
        return "DetailImage{" +
                "imageDisplay='" + imageDisplay + '\'' +
                ", linkDownload='" + linkDownload + '\'' +
                '}';
    }
}
