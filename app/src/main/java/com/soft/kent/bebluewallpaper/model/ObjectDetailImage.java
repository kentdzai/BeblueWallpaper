package com.soft.kent.bebluewallpaper.model;

/**
 * Created by QuyetChu on 5/20/16.
 */
public class ObjectDetailImage {
    String linkDisplay;
    String linkDownload;

    public ObjectDetailImage(String linkDisplay, String linkDownload) {
        this.linkDisplay = linkDisplay;
        this.linkDownload = linkDownload;
    }

    public String getLinkDisplay() {
        return linkDisplay;
    }

    public String getLinkDownload() {
        return linkDownload;
    }
}
