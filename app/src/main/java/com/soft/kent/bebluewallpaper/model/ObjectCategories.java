package com.soft.kent.bebluewallpaper.model;

/**
 * Created by QuyetChu on 5/19/16.
 */
public class ObjectCategories {

    int id;
    String tenChuDe;
    String linkChuDe;
    int anhDaiDien;
    int anhBia;

    public ObjectCategories(int id, String tenChuDe, String linkChuDe, int anhDaiDien, int anhBia) {
        this.id = id;
        this.tenChuDe = tenChuDe;
        this.anhDaiDien = anhDaiDien;
        this.linkChuDe = linkChuDe;
        this.anhBia = anhBia;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnhBia(int anhBia) {
        this.anhBia = anhBia;
    }

    public void setAnhDaiDien(int anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public void setLinkChuDe(String linkChuDe) {
        this.linkChuDe = linkChuDe;
    }

    public void setTenChuDe(String tenChuDe) {
        this.tenChuDe = tenChuDe;
    }

    public int getId() {
        return id;
    }

    public int getAnhBia() {
        return anhBia;
    }

    public int getAnhDaiDien() {
        return anhDaiDien;
    }

    public String getLinkChuDe() {
        return linkChuDe;
    }

    public String getTenChuDe() {
        return tenChuDe;
    }

    @Override
    public String toString() {
        return "ObjectCategories{" +
                "id=" + id +
                ", tenChuDe='" + tenChuDe + '\'' +
                ", linkChuDe='" + linkChuDe + '\'' +
                ", anhDaiDien=" + anhDaiDien +
                ", anhBia=" + anhBia +
                '}';
    }
}
