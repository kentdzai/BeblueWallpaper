package com.soft.kent.bebluewallpaper.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.controller.MyHandler;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ZoomPhotoActivity extends AppCompatActivity {
    ImageView ivZoomPhoto;
    PhotoViewAttacher photoViewAttacher;
    MyHandler myHandler;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_photo);
        init(getIntent());
    }

    private void init(Intent it) {
        ivZoomPhoto = (ImageView) findViewById(R.id.ivZoomPhoto);

        if (it != null) {
            myHandler = new MyHandler(this);
            String wallpaperName = it.getStringExtra("WALLPAPER_NAME");
            bmp = myHandler.getBitMap(wallpaperName);
            if (bmp != null) {
                ivZoomPhoto.setImageBitmap(bmp);
                photoViewAttacher = new PhotoViewAttacher(ivZoomPhoto);
            }
        }

    }
}
