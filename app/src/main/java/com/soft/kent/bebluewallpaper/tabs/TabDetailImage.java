package com.soft.kent.bebluewallpaper.tabs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.soft.kent.bebluewallpaper.DetailImageActivity;
import com.soft.kent.bebluewallpaper.controller.MyLog;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.SelectDownLoadActivity;
import com.soft.kent.bebluewallpaper.controller.GetDetailImage;
import com.soft.kent.bebluewallpaper.controller.MyHandler;
import com.soft.kent.bebluewallpaper.model.DatabaseWallpaper;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.soft.kent.bebluewallpaper.model.ObjectDownloads;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.view.ZoomPhotoActivity;

import java.io.File;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TabDetailImage extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    ArrayList<ObjectDetailImage> arrrObbjectDetailImage;

    CoordinatorLayout LayoutDetailImage;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;
    ProgressBar pLoadDetail;
    public CheckBox cbFavorite;
    String link;
    Handler ui_handler;
    MyHandler myHandler;
    DatabaseWallpaper db;
    Context context;
    Activity activity;
    boolean fav;
    public Bitmap bmp;
    public ObjectDetailImage oD;
    public ArrayList<ObjectDownloads> arrD;
    ArrayList<ObjectDownloads> arrR;
    String smallImage;
    RelativeLayout RBannerDetail;


    public TabDetailImage() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        ui_handler = new Handler(Looper.getMainLooper());
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(View v) {
        Bundle bD = getArguments();
        if (bD != null) {
            link = bD.getString("LinkDetail");
            smallImage = bD.getString("ImageSmall");
            db = new DatabaseWallpaper(activity);
        }

        myHandler = new MyHandler(activity);
        arrD = new ArrayList<>();
        LayoutDetailImage = (CoordinatorLayout) v.findViewById(R.id.LayoutDetailImage);
        arrrObbjectDetailImage = new ArrayList<>();
        RBannerDetail = (RelativeLayout) v.findViewById(R.id.RBannerDetail);
        ivDetail = (ImageView) v.findViewById(R.id.ivDetail);
        tvTitleDetailImage = (TextView) v.findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) v.findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) v.findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) v.findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);
        pLoadDetail = (ProgressBar) v.findViewById(R.id.pLoadDetail);
        LayoutDetailImage.setVisibility(View.GONE);
        cbFavorite = (CheckBox) v.findViewById(R.id.cbFavorite);
        fav = db.checkFavorite(link);
        cbFavorite.setChecked(fav);
        showBanner(RBannerDetail);

        new GetAllDetailImageTask().execute(link);
        btnSetWallpaper.setOnClickListener(this);
        btnDownloadImage.setOnClickListener(this);
        cbFavorite.setOnCheckedChangeListener(this);
        ivDetail.setOnClickListener(this);
    }

    AdView adView;

    private void showBanner(RelativeLayout RBannerDetail) {
        adView = new AdView(activity, "293171814358472_293171827691804", AdSize.BANNER_HEIGHT_50);
        RBannerDetail.addView(adView);
        adView.loadAd();
    }

    public String getOriginal(ArrayList<ObjectDownloads> arrD) {
        for (int i = 0; i < arrD.size(); i++) {
            if (arrD.get(i).resolution.equals("Original")) {
                return arrD.get(i).downloadLink;
            }
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        bmp = myHandler.getBitMap(oD.wallpaperName);
        switch (v.getId()) {
            case R.id.ivDetail:
                if (myHandler.getBitMap(oD.wallpaperName + "-Original") == null) {
                    if (arrD != null) {
                        final ProgressDialog pDialog;
                        pDialog = new ProgressDialog(activity);
                        pDialog.setMessage("Downloading file. Please wait...");
                        pDialog.setIndeterminate(false);
                        pDialog.setProgress(0);
                        pDialog.setMax(100);
                        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pDialog.setCancelable(true);
                        pDialog.show();
                        Ion.with(activity).load(getOriginal(arrD)).progressDialog(pDialog).asBitmap().setCallback(new FutureCallback<Bitmap>() {
                            @Override
                            public void onCompleted(Exception e, Bitmap result) {
                                pDialog.dismiss();
                                myHandler.saveFile(result, oD.wallpaperName + "-Original");
                                Intent itPhotoZoom = new Intent(getContext().getApplicationContext(), ZoomPhotoActivity.class);
                                itPhotoZoom.putExtra("WALLPAPER_NAME", oD.wallpaperName + "-Original");
                                startActivity(itPhotoZoom);
                            }
                        });
                    }
                } else {
                    Intent itPhotoZoom = new Intent(getContext().getApplicationContext(), ZoomPhotoActivity.class);
                    itPhotoZoom.putExtra("WALLPAPER_NAME", oD.wallpaperName + "-Original");
                    startActivity(itPhotoZoom);

                }
//                Chuyển sang màn hình full-screen
                break;
            case R.id.btnDownloadImage:
                if (bmp == null) {
                    if (arrD != null) {
                        Intent itDownload = new Intent(getContext().getApplicationContext(), SelectDownLoadActivity.class);
                        itDownload.putExtra("LIST_LINK", arrD);
                        itDownload.putExtra("PROGRESS", 0);
                        itDownload.putExtra("WALLPAPER_NAME", oD.wallpaperName);
                        startActivity(itDownload);
                        for (int i = 0; i < arrD.size(); i++) {
                            if (arrD.get(i).resolution.equals("Original")) {
                                MyLog.e(arrD.get(i).downloadLink);
                            }
                        }
                    } else
                        myHandler.downloadBitmap(oD.linkDownload, oD.wallpaperName + "-Original", myHandler.ONLY_DOWNLOAD);
                }
                break;
            case R.id.btnSetWallpaper:
                Intent itDownloadToSetWallpaper = new Intent(getContext().getApplicationContext(), SelectDownLoadActivity.class);
                itDownloadToSetWallpaper.putExtra("LIST_LINK", arrD);
                itDownloadToSetWallpaper.putExtra("PROGRESS", 2);
                itDownloadToSetWallpaper.putExtra("WALLPAPER_NAME", oD.wallpaperName);
                startActivity(itDownloadToSetWallpaper);
                break;
        }
    }

    public ArrayList<ObjectDownloads> getResolutionFileDownloaded(String fileName) {
        ArrayList<ObjectDownloads> arrD = new ArrayList<>();
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File beblueDir = new File(sdCardRoot, "BeblueWallPaper/");
        for (File f : beblueDir.listFiles()) {
            if (f.isFile()) {
                String fullName = f.getName().trim();
                if (fullName.indexOf("-") != -1) {
                    String firstName = fullName.substring(0, fullName.indexOf("-")).trim();
                    if (firstName.equals(fileName)) {
                        String resolution = fullName.substring(fullName.indexOf("-") + 1, fullName.indexOf(".jpg")).trim();
                        arrD.add(new ObjectDownloads(resolution, ""));
                    }
                }
            }
        }
        return arrD;
    }

    public void insertFav(boolean isChecked) {
        if (isChecked) {
            db.insertFavorite(new ObjectImage(smallImage, link, true));
        } else {
            db.deleteFavorite(link);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (DetailImageActivity.itFav != null)
            if (cbFavorite.isChecked())
                DetailImageActivity.itFav.setTitle(DetailImageActivity.parseBooleanFavToTitle(true));
            else
                DetailImageActivity.itFav.setTitle(DetailImageActivity.parseBooleanFavToTitle(false));
        insertFav(isChecked);
    }

    private class GetAllDetailImageTask extends AsyncTask<String, Void, ObjectDetailImage> {
        @Override
        protected ObjectDetailImage doInBackground(String... params) {
            oD = GetDetailImage.getDetailsImage(params[0], arrD);
            return oD;
        }

        @Override
        protected void onPostExecute(ObjectDetailImage result) {
            if (oD != null) {
                ui_handler.post(new Runnable() {
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
                            pLoadDetail.setProgress(0);
                            pLoadDetail.setMax(100);
                            pLoadDetail.setVisibility(View.VISIBLE);
                            Ion.with(context).load(oD.linkDisplay).progress(new ProgressCallback() {
                                @Override
                                public void onProgress(long downloaded, long total) {
                                    int progress = (int) ((downloaded * 100) / total);
                                    pLoadDetail.setProgress(progress);
                                }
                            }).intoImageView(ivDetail).setCallback(new FutureCallback<ImageView>() {
                                @Override
                                public void onCompleted(Exception e, ImageView result) {
                                    pLoadDetail.setVisibility(View.GONE);
                                }
                            });
                        }
                        tvTitleDetailImage.setText(oD.wallpaperName);
                        tvCountViewDetailImage.setText(String.valueOf(oD.downloadCount));
                        tvAuthorName.setText(oD.authorName);
                        LayoutDetailImage.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

    }
}
