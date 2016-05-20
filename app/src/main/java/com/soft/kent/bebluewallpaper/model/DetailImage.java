package com.soft.kent.bebluewallpaper.model;

import java.util.List;

/**
 * Created by QuyetChu on 5/20/16.
 */
public class DetailImage {
    String imageDisplay;
    List<ImageLandScape> list;

    public DetailImage() {
    }

    public void setImageDisplay(String imageDisplay) {
        this.imageDisplay = imageDisplay;
    }


    public String getImageDisplay() {
        return imageDisplay;
    }

    public void setList(List<ImageLandScape> list) {
        this.list = list;
    }

    public List<ImageLandScape> getList() {
        return list;
    }

    @Override
    public String toString() {
        return "DetailImage{" +
                "imageDisplay='" + imageDisplay + '\'' +
                ", list=" + list +
                '}';
    }
}
