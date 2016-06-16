package com.soft.kent.bebluewallpaper.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.Display;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.soft.kent.bebluewallpaper.model.ObjectImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by kentd on 27/05/2016.
 */
public class MyHandler {
    Activity activity;
    Context context;
    final String folderName = "BeblueWallPaper/";
    final String formatType = ".jpg";
    public final int ONLY_DOWNLOAD = 0;
    public final int DOWNLOAD_AND_SET_WALLPAPER = 1;
    public final int DOWNLOAD_AND_SET_CONTACT = 2;


    public MyHandler(Activity activity) {
        this.activity = activity;
    }

    public MyHandler(Context context) {
        this.context = context;
    }

    public void myToast(String msg) {
        Toast.makeText(activity, "" + msg, Toast.LENGTH_LONG).show();
    }

    public Bundle pushDetail(String link, int position, ArrayList<ObjectImage> arrI, String key) {
        Bundle bundle = new Bundle();
        bundle.putString("linkPage", link);
        bundle.putInt("position", position);
        bundle.putString(GetPage.KEY_DETAIL, key);
        bundle.putSerializable(GetDetailImage.LIST_DETAIL, arrI);
        return bundle;
    }

    public static boolean parseIntToBoolean(int num) {
        return (num == 1) ? true : false;
    }

    public static int parseBooleanToInt(boolean isTrue) {
        return (isTrue) ? 1 : 0;
    }

    public int getScreenOrientation(Configuration configuration) {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation = configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
//                orientation = configuration.ORIENTATION_PORTRAIT;
                orientation = 2;
            } else {
//                orientation = configuration.ORIENTATION_LANDSCAPE;
                orientation = 4;
            }
        }
        return orientation;
    }

    public void setWallPaper(Bitmap bmp) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(activity);
        try {
            myWallpaperManager.setBitmap(bmp);
            Toast.makeText(activity, "Set wallpaper success !!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadBitmap(String linkDown, final String wallpaperName, final int numberProgress) {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
        Ion.with(activity).load(linkDown).progress(new ProgressCallback() {
            @Override
            public void onProgress(long downloaded, long total) {
                pDialog.setProgress((int) ((downloaded * 100) / total));
                pDialog.setProgressNumberFormat((downloaded / 1024) + "KB / " + (total / 1024) + "KB");
            }
        }).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                pDialog.dismiss();
                if (numberProgress == 0)
                    saveFile(result, wallpaperName);
                else if (numberProgress == DOWNLOAD_AND_SET_WALLPAPER) {
                    saveFile(result, wallpaperName);
                    setWallPaper(result);
                } else if (numberProgress == DOWNLOAD_AND_SET_CONTACT) {
                    saveFile(result, wallpaperName);
                    activity.startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), DOWNLOAD_AND_SET_CONTACT);
                }
            }
        });
    }

    public void saveFile(Bitmap bmp, String wallpaperName) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + folderName;
        FileOutputStream fOut = null;
        File file = new File(path, wallpaperName + formatType);
        try {
            fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            myToast("Download Success!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitMap(String fileName) {
        try {
            return BitmapFactory.decodeFile(getPath(fileName).toString());
        } catch (Exception e) {
            return null;
        }
    }

    public Uri getPath(String fileName) {
        return Uri.parse(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + folderName + fileName + formatType);
    }
}
