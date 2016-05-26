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
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.soft.kent.bebluewallpaper.MyLog;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TabDetailImage extends Fragment {

    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscapeDetail";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscapeDetail";
    private final String METHOD_NAME = "getLinkImageLandscapeDetail";
    ArrayList<ObjectDetailImage> arrrObbjectDetailImage;
    Toolbar toolbar;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;
    String link;
    Handler handler;
    CoordinatorLayout LayoutDetailImage;
    String ImageDisplay;
    String WallpaperName;
    String LinkDown;
    Bitmap bmp;
    String filePath;
    public TabDetailImage(String link, int size) {
        this.link = link;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        handler = new Handler(Looper.getMainLooper());
        btnSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPatch();

                if(bmp == null){
                    getContext().registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    downloadFile(LinkDown);
                }else {
                    setWallPaper(bmp);
                }


            }
        });

        btnDownloadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFile(LinkDown);
            }
        });
        return v;
    }

    public void setWallPaper(Bitmap bmp){
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getActivity());
        try {
            myWallpaperManager.setBitmap(bmp);
            Toast.makeText(getActivity(), "Set wallpaper success !!!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Bitmap getPatch(){
        filePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator + "BeblueWallPaper/"+WallpaperName+".jpg";
        bmp = BitmapFactory.decodeFile(filePath);
        return bmp;
    }
    BroadcastReceiver onComplete = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Toast.makeText(getActivity(), "Download complete !!!", Toast.LENGTH_SHORT).show();
            getPatch();
            setWallPaper(bmp);
            getContext().unregisterReceiver(onComplete);
        }
    };

    public void downloadFile(String uRl) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true).setTitle(WallpaperName)
                .setDescription("Downloading ...")
                .setDestinationInExternalPublicDir("/BeblueWallPaper", WallpaperName+".jpg");

        mgr.enqueue(request);

    }



    private void init(View v) {
        LayoutDetailImage = (CoordinatorLayout) v.findViewById(R.id.LayoutDetailImage);
//        toolbar = (Toolbar) v.findViewById(R.id.toolbar1);
//        getActivity().setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
//        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));

        arrrObbjectDetailImage = new ArrayList<>();
        ivDetail = (ImageView) v.findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) v.findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) v.findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) v.findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) v.findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) v.findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);
        arrrObbjectDetailImage = new ArrayList<>();
        LayoutDetailImage.setVisibility(View.GONE);
        new GetAllDetailImageTask().execute();
    }

    private class GetAllDetailImageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getDetailsImage();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

    }

    public void getDetailsImage() {
        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty("sUrl", link);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject getLinkImageLandscapeDetailResult = (SoapObject)
                    response.getProperty("getLinkImageLandscapeDetailResult");
            SoapObject DownloadLinks = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("DownloadLinks");
            SoapObject ImageLandScape = (SoapObject)
                    DownloadLinks.getProperty("ImageLandScape");
            ImageDisplay = getLinkImageLandscapeDetailResult.getPropertyAsString("ImageDisplay");
            String ImageResolution = ImageLandScape.getPropertyAsString("ImageResolution");
            LinkDown = ImageLandScape.getPropertyAsString("LinkDown");
            SoapObject ImageRelate = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("ImageRelate");
            SoapObject ImageLandscapeThumb = (SoapObject)
                    ImageRelate.getProperty("ImageLandscapeThumb");
            String ImageSmall = ImageLandscapeThumb.getPropertyAsString("ImageSmall");
            String LinkDetail = ImageLandscapeThumb.getPropertyAsString("LinkDetail");
            SoapObject Tags = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("Tags");
            String tag1 = Tags.getPropertyAsString(0);
            String tag2 = Tags.getPropertyAsString(1);
            WallpaperName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("WallpaperName");
            String CatetoryName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("CatetoryName");
            final int Download =
                    Integer.parseInt(getLinkImageLandscapeDetailResult.getPropertyAsString("Download"));
            String DateTime =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("DateTime");
//            MyLog.e("ImageSmall: " + ImageSmall);
//            MyLog.e("LinkDetail: " + LinkDetail);
//            MyLog.e("ImageDisplay: " + ImageDisplay);
//            MyLog.e("ImageResolution: " + ImageResolution);
//            MyLog.e("LinkDown: " + LinkDown);
            MyLog.e("CatetoryName: " + CatetoryName);
            MyLog.e("DateTime: " + DateTime);

            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Glide.with(getContext()).load(ImageDisplay)
//                            .thumbnail(0.1f)
//                            .crossFade()
//                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .into(ivDetail);
////
                    Picasso.with(getContext()).load(ImageDisplay).into(ivDetail);
                    LayoutDetailImage.setVisibility(View.VISIBLE);
                    tvTitleDetailImage.setText(WallpaperName);
                    tvCountViewDetailImage.setText(String.valueOf(Download));
//                    MyLog.e("Tab: " + ImageDisplay);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
