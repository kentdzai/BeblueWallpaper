package com.soft.kent.bebluewallpaper.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.soft.kent.bebluewallpaper.R;

/**
 * Created by QuyetChu on 5/19/16.
 */
public class ChuDeDatabase extends SQLiteOpenHelper{
    public SQLiteDatabase db;

    public ChuDeDatabase(Context context) {
        super(context, "dbCategories.db", null, 0);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TBL_CHUDE(ID INTEGER PRIMARY KEY, TEN_CHUDE TEXT, LINK_CHUDE TEXT, AVATAR INTEGER, COVER INTEGER)");

    }
    public void insertDefaultCategories(SQLiteDatabase db){
       db.execSQL("INSERT INTO TBL_CHUDE VALUES (1, '3D & Abstract', 'http://www.hdwallpapers.in/3d__abstract-desktop-wallpapers/page/',"+ R.mipmap.ic_backarrow+","+ R.mipmap.ic_backarrow+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
