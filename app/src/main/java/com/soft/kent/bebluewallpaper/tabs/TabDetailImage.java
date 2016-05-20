package com.soft.kent.bebluewallpaper.tabs;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.MyLog;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class TabDetailImage extends Fragment {

    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscapeDetail";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscapeDetail";
    private final String METHOD_NAME = "getLinkImageLandscapeDetail";
    private String link = "http://www.hdwallpapers.in/michelangelo_tmnt_out_of_the_shadows-wallpapers.html";
    ArrayList<ObjectDetailImage> arrrObbjectDetailImage;
    Toolbar toolbar;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;
    String a;
    Handler handler;

    public TabDetailImage(String link) {
        this.a = link;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        handler = new Handler(Looper.getMainLooper());
        return v;
    }

    private void init(View v) {

//        toolbar = (Toolbar) v.findViewById(R.id.toolbar1);
//        getActivity().setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
//        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));

        ivDetail = (ImageView) v.findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) v.findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) v.findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) v.findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) v.findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) v.findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);
        arrrObbjectDetailImage = new ArrayList<>();
        new GetAllDetailImageTask().execute();
//        showImage();
    }

    public void showImage() {
        Picasso.with(getContext()).load("http://www.hdwallpapers.in/walls/michelangelo_tmnt_out_of_the_shadows-wide.jpg")
                .resize(600, 800).into(ivDetail);
//        if (link != null) {
//
//        }

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
        request.addProperty("sUrl", a);
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
            final String ImageDisplay = getLinkImageLandscapeDetailResult.getPropertyAsString("ImageDisplay");
            String ImageResolution = ImageLandScape.getPropertyAsString("ImageResolution");
            String LinkDown = ImageLandScape.getPropertyAsString("LinkDown");

            SoapObject ImageRelate = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("ImageRelate");
            SoapObject ImageLandscapeThumb = (SoapObject)
                    ImageRelate.getProperty("ImageLandscapeThumb");
            String ImageSmall = ImageLandscapeThumb.getPropertyAsString("ImageSmall");
            String LinkDetail = ImageLandscapeThumb.getPropertyAsString("LinkDetail");
            MyLog.e("ImageSmall: " + ImageSmall);
            MyLog.e("LinkDetail: " + LinkDetail);
            MyLog.e("ImageDisplay: " + ImageDisplay);
            MyLog.e("ImageResolution: " + ImageResolution);
            MyLog.e("LinkDown: " + LinkDown);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Picasso.with(getContext()).load(ImageDisplay).resize(600, 800).into(ivDetail);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
