package com.soft.kent.bebluewallpaper.tabs;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.ion.Ion;
import com.soft.kent.bebluewallpaper.MyLog;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.model.GetDetailImage;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TabDetailImage extends Fragment implements View.OnClickListener {
    ArrayList<ObjectDetailImage> arrrObbjectDetailImage;

    CoordinatorLayout LayoutDetailImage;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;

    String link;
    Handler handler;

    Context context;

    public Bitmap bmp;
    String filePath;
    SimpleDraweeView fDetail;
    public ObjectDetailImage oD;

    final String folderName = "BeblueWallPaper/";
    final String formatName = ".jpg";
    int position;

    public TabDetailImage(String link, int position) {
        this.link = link;
        this.position = position;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(context.getApplicationContext());
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        handler = new Handler(Looper.getMainLooper());
        btnSetWallpaper.setOnClickListener(this);
        btnDownloadImage.setOnClickListener(this);
        return v;
    }

    public void setWallPaper(Bitmap bmp) {
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getActivity());
        try {
            myWallpaperManager.setBitmap(bmp);
            Toast.makeText(getActivity(), "Set wallpaper success !!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPatch() {
        filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + folderName + oD.wallpaperName + formatName;
        bmp = BitmapFactory.decodeFile(filePath);
        return bmp;
    }

    public Uri getFile(String fileName) {
        return Uri.parse(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + folderName + fileName + formatName);
    }

    public BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(getActivity(), "Download complete !!!", Toast.LENGTH_SHORT).show();
            getPatch();
            setWallPaper(bmp);
            context.unregisterReceiver(onComplete);
        }
    };

    public void downloadFile(String uRl) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(uRl));
        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true).setTitle(oD.wallpaperName)
                .setDescription("Downloading ...")
                .setDestinationInExternalPublicDir("/BeblueWallPaper", oD.wallpaperName + ".jpg");
        mgr.enqueue(request);
    }

    private void init(View v) {
        LayoutDetailImage = (CoordinatorLayout) v.findViewById(R.id.LayoutDetailImage);
        arrrObbjectDetailImage = new ArrayList<>();
        ivDetail = (ImageView) v.findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) v.findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) v.findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) v.findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) v.findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) v.findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);

        fDetail = (SimpleDraweeView) v.findViewById(R.id.fDetail);

        LayoutDetailImage.setVisibility(View.GONE);

        new GetAllDetailImageTask().execute(link);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSetWallpaper:
                getPatch();
                if (bmp == null) {
                    context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    downloadFile(oD.linkDownload);
                } else {
                    setWallPaper(bmp);
                }
                break;
            case R.id.btnDownloadImage:
                if (getPatch() == null)
                    downloadFile(oD.linkDownload);
                else
                    myToast("Download rồi mà!");
                break;
        }
    }

    private void myToast(String s) {
        Toast.makeText(TabDetailImage.this.getContext(), s, Toast.LENGTH_LONG).show();
    }

    private class GetAllDetailImageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            MyLog.e("DETAIL IMAGE: " + position);
            oD = GetDetailImage.getDetailsImage(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (oD != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (context != null) {
//                            Glide.with(context).load(ImageDisplay)
//                            .thumbnail(0.1f)
//                            .crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(ivDetail);

//                            Picasso.with(context).load(oD.linkDisplay).into(ivDetail);
//                            Glide.with(context).load(oD.linkDisplay).into(ivDetail);
//                            fDetail.setImageURI(Uri.parse(ImageDisplay));
                            Ion.with(context).load(oD.linkDisplay).intoImageView(ivDetail);
                        }
                        tvTitleDetailImage.setText(oD.wallpaperName);
                        tvCountViewDetailImage.setText(String.valueOf(oD.downloadCount));
                        tvAuthorName.setText(oD.authorName);
                        LayoutDetailImage.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
