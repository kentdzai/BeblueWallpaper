package com.soft.kent.bebluewallpaper.model;

import java.io.Serializable;

public class ObjectImage implements Serializable {

    public String imageSmall;
    public String linkDetail;
    public boolean favorite;
    public boolean advertisement;

    public ObjectImage(String imageSmall, String linkDetai, boolean favorite) {
        this.imageSmall = imageSmall;
        this.linkDetail = linkDetai;
        this.favorite = favorite;
    }

    public ObjectImage(String imageSmall, String linkDetai, boolean favorite, boolean advertisement) {
        this.imageSmall = imageSmall;
        this.linkDetail = linkDetai;
        this.favorite = favorite;
        this.advertisement = advertisement;
    }
}

