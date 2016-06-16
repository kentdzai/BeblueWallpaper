package com.soft.kent.bebluewallpaper.model;

import java.io.Serializable;

/**
 * Created by kentd on 02/06/2016.
 */
public class ObjectDownloads implements Serializable {
    public String resolution;
    public String downloadLink;

    public ObjectDownloads(String resolution, String downloadLink) {
        this.resolution = resolution;
        this.downloadLink = downloadLink;
    }
}
