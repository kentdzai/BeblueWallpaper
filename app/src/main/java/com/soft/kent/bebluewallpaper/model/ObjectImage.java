package com.soft.kent.bebluewallpaper.model;

/**
 * Created by QuyetChu on 5/18/16.
 */
public class ObjectImage {

    private String imageSmall;
    private String linkDetail;


    public void setImageSmall(String imageSmall) {

        this.imageSmall = imageSmall;
    }

    public void setLinkDetail(String linkDetail) {
        this.linkDetail = linkDetail;
    }

    public String getLinkDetail() {
        return linkDetail;
    }

    public String getImageSmall() {
        return imageSmall;
    }

    @Override
    public String toString() {
        return "Anh{" +
                "imageSmall='" + imageSmall + '\'' +
                ", linkDetail='" + linkDetail + '\'' +
                '}';
    }


}

