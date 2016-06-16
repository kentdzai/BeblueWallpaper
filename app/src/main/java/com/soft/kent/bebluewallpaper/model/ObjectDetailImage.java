package com.soft.kent.bebluewallpaper.model;

/**
 * Created by QuyetChu on 5/20/16.
 */
public class ObjectDetailImage {
    public String wallpaperName;
    public String linkDisplay;
    public String linkDownload;
    public String imageSmall;
    public int downloadCount;
    public String authorName;


    public ObjectDetailImage(String wallpaperName, String linkDisplay, String linkDownload, String imageSmall, int downloadCount, String authorName) {
        this.wallpaperName = wallpaperName;
        this.linkDisplay = linkDisplay;
        this.linkDownload = linkDownload;
        this.imageSmall = imageSmall;
        this.downloadCount = downloadCount;
        this.authorName = authorName;
    }

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
