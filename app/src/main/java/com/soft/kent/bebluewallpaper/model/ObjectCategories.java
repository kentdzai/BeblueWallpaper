package com.soft.kent.bebluewallpaper.model;

public class ObjectCategories {
    public int id;
    public String tenChuDe;
    public String linkChuDe;
    public int anhDaiDien;
    public int anhBia;

    public ObjectCategories(int id, String tenChuDe, String linkChuDe, int anhDaiDien, int anhBia) {
        this.id = id;
        this.tenChuDe = tenChuDe;
        this.anhDaiDien = anhDaiDien;
        this.linkChuDe = linkChuDe;
        this.anhBia = anhBia;
    }
}
