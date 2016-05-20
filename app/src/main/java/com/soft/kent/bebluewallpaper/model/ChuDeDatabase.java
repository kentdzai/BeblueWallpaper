package com.soft.kent.bebluewallpaper.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soft.kent.bebluewallpaper.R;

import java.util.ArrayList;

/**
 * Created by QuyetChu on 5/19/16.
 */
public class ChuDeDatabase extends SQLiteOpenHelper {
    public SQLiteDatabase db;

    public ChuDeDatabase(Context context) {
        super(context, "dbCategories.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TBL_CHUDE(ID INTEGER PRIMARY KEY, TEN_CHUDE TEXT, LINK_CHUDE TEXT, AVATAR INTEGER, COVER INTEGER)");
        insertDefaultCategories(db);
    }

    public void insertDefaultCategories(SQLiteDatabase db) {
        insertDefaults(db, 1, "3D & Abstract",
                "http://www.hdwallpapers.in/3d__abstract-desktop-wallpapers/page", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 2, "Animals & Birds",
                "http://www.hdwallpapers.in/animals__birds-desktop-wallpapers/page", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 3, "Anime",
                "http://www.hdwallpapers.in/anime-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 4, "Beach",
                "http://www.hdwallpapers.in/beach-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 5, "Bikes",
                "http://www.hdwallpapers.in/bikes__motorcycles-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 6, "Brands & Logos",
                "http://www.hdwallpapers.in/brands__logos-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 7, "Car",
                "http://www.hdwallpapers.in/cars-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 8, "Celebrations",
                "http://www.hdwallpapers.in/celebrations-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 9, "Celebrities",
                "http://www.hdwallpapers.in/celebrities-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 10, "Christmas",
                "http://www.hdwallpapers.in/christmas-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 11, "Creative Graphics",
                "http://www.hdwallpapers.in/creative__graphics-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 12, "Cute",
                "http://www.hdwallpapers.in/cute-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 13, "Digital Universe",
                "http://www.hdwallpapers.in/digital_universe-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 14, "Dreamy & Fantasy",
                "http://www.hdwallpapers.in/dreamy__fantasy-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 15, "Fantasy Girls",
                "http://www.hdwallpapers.in/fantasy_girls-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 16, "Flowers",
                "http://www.hdwallpapers.in/flowers-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 17, "Games",
                "http://www.hdwallpapers.in/games-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 18, "Inspirational",
                "http://www.hdwallpapers.in/inspirational-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 19, "Love",
                "http://www.hdwallpapers.in/love-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 20, "Music",
                "http://www.hdwallpapers.in/music-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 21, "Movies",
                "http://www.hdwallpapers.in/movie-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 22, "Nature",
                "http://www.hdwallpapers.in/nature__landscape-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 23, "Others",
                "http://www.hdwallpapers.in/others-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 24, "Photography",
                "http://www.hdwallpapers.in/photography-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 25, "Planes",
                "http://www.hdwallpapers.in/planes-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 26, "Sports",
                "http://www.hdwallpapers.in/sports-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 27, "Travel & World",
                "http://www.hdwallpapers.in/travel__world-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);
        insertDefaults(db, 28, "Vector & Designs",
                "http://www.hdwallpapers.in/vector__designs-desktop-wallpapers/page/", R.mipmap.bg_fav, R.mipmap.cover);

    }

    public void insertDefaults(SQLiteDatabase db, int id, String name, String link, int avatar, int cover) {
        db.execSQL("INSERT INTO TBL_CHUDE VALUES " +
                "(" + id + ", '" + name + "', '" + link + "',"
                + avatar + "," + cover + ")");
    }

    public ArrayList<ObjectCategories> queryAllChuDe(){
        ArrayList<ObjectCategories> a = new ArrayList<>();

        db = getReadableDatabase();
        Cursor c = db.query("TBL_CHUDE", null, null, null, null, null, null);

        c.moveToFirst();

        while(c.moveToNext()){
            a.add(new ObjectCategories(c.getInt(0),c.getString(1), c.getString(2), c.getInt(3), c.getInt(4)));
        }

        c.close();

        return a;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
