package com.soft.kent.bebluewallpaper.controller;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.soft.kent.bebluewallpaper.DetailImageActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by kentd on 02/06/2016.
 */
public class DownloadImage {
    final String folderName = "BeblueWallPaper/";
    final String formatType = ".jpg";
    public final int ONLY_DOWNLOAD = 0;
    public final int DOWNLOAD_AND_SET_WALLPAPER = 1;
    public final int DOWNLOAD_AND_SET_CONTACT = 2;
    Context context;

    public DownloadImage(Context context) {
        this.context = context;
    }


    public void setWallPaper(Bitmap bmp) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(context);
        try {
            myWallpaperManager.setBitmap(bmp);
            Toast.makeText(context, "Set wallpaper success !!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadBitmap(final Activity activity, String linkDown, final String wallpaperName, final int numberProgress) {
        final ProgressDialog pDialog;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setProgress(0);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(true);
        pDialog.show();
        Ion.with(context).load(linkDown).progressDialog(pDialog).asBitmap().setCallback(new FutureCallback<Bitmap>() {
            @Override
            public void onCompleted(Exception e, Bitmap result) {
                pDialog.dismiss();
                if (numberProgress == 0)
                    saveFile(result, wallpaperName);
                else if (numberProgress == DOWNLOAD_AND_SET_WALLPAPER) {
                    saveFile(result, wallpaperName);
                    setWallPaper(result);
                } else if (numberProgress == DOWNLOAD_AND_SET_CONTACT)
                    activity.startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), DOWNLOAD_AND_SET_CONTACT);

            }
        });
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

    public String renameWalpaper(String wallpaperName) {
        return wallpaperName.replace(" ", "-");
    }
}
