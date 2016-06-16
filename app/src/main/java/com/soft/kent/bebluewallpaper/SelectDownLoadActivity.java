package com.soft.kent.bebluewallpaper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.controller.MyHandler;
import com.soft.kent.bebluewallpaper.model.ObjectDownloads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SelectDownLoadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvDownload;
    ArrayList<ObjectDownloads> arrD;
    AdapterDownload adapter;
    MyHandler myHandler;
    String wallpaperName;
    Bitmap bmp;
    final int PROGRESS_DOWNLOAD = 0;
    final int PROGRESS_SETWALLPAPER = 1;
    final int PROGRESS_DOWNLOAD_TO_SETWALLPAPER = 2;
    final int PROGRESS_VIEW_PHOTO = 3;
    int PROGRESS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_down_load);
        init(getIntent());
    }

    private void init(Intent it) {
        myHandler = new MyHandler(this);
        lvDownload = (ListView) findViewById(R.id.lvDownload);
        if (it != null) {
            arrD = (ArrayList<ObjectDownloads>) it.getSerializableExtra("LIST_LINK");
            wallpaperName = it.getStringExtra("WALLPAPER_NAME");
            PROGRESS = it.getIntExtra("PROGRESS", 0);
        }
        if (arrD != null) {
            adapter = new AdapterDownload(getApplicationContext(), R.layout.adapter_downloads, arrD);
            lvDownload.setAdapter(adapter);
            lvDownload.setOnItemClickListener(this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ObjectDownloads oD = arrD.get(position);
        String nameSave = wallpaperName + "-" + oD.resolution;
        bmp = myHandler.getBitMap(nameSave);
        if (PROGRESS == PROGRESS_DOWNLOAD) {
            if (bmp == null) {
                myHandler.downloadBitmap(oD.downloadLink,
                        nameSave,
                        myHandler.ONLY_DOWNLOAD);
            } else
                myHandler.myToast("Download rồi mà!");
        } else if (PROGRESS == PROGRESS_SETWALLPAPER) {
            if (bmp != null) {
                myHandler.setWallPaper(bmp);
            }
        } else if (PROGRESS == PROGRESS_DOWNLOAD_TO_SETWALLPAPER) {
            if (bmp == null)
                myHandler.downloadBitmap(oD.downloadLink,
                        nameSave,
                        myHandler.DOWNLOAD_AND_SET_WALLPAPER);
            else
                myHandler.setWallPaper(bmp);

        } else if (PROGRESS == PROGRESS_VIEW_PHOTO) {
            if (bmp == null) {

            }
        }
    }

    class AdapterDownload extends ArrayAdapter<ObjectDownloads> {
        Context context;
        int idLayout;
        ArrayList<ObjectDownloads> arrDA;
        TextView tvAdapterDownLoads;

        public AdapterDownload(Context context, int idLayout, ArrayList<ObjectDownloads> arrDA) {
            super(context, idLayout, arrDA);
            this.context = context;
            this.idLayout = idLayout;
            this.arrDA = arrDA;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().from(context).inflate(idLayout, null);
            initA(v, position);
            return v;
        }

        private void initA(View v, int position) {
            tvAdapterDownLoads = (TextView) v.findViewById(R.id.tvAdapterDownLoads);
            tvAdapterDownLoads.setText(arrDA.get(position).resolution);
        }
    }
}
