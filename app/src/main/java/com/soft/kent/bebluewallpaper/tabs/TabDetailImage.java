package com.soft.kent.bebluewallpaper.tabs;

import android.annotation.SuppressLint;
import android.media.Image;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.koushikdutta.ion.Ion;
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
    SimpleDraweeView fDetail;

    public TabDetailImage(String link, int size) {
        this.link = link;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(this.getContext().getApplicationContext());
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        handler = new Handler(Looper.getMainLooper());
        return v;
    }

    private void init(View v) {
        LayoutDetailImage = (CoordinatorLayout) v.findViewById(R.id.LayoutDetailImage);
//        toolbar = (Toolbar) v.findViewById(R.id.toolbar1);
//        (toolbar);
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

        fDetail = (SimpleDraweeView) v.findViewById(R.id.fDetail);


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
            final String ImageDisplay = getLinkImageLandscapeDetailResult.getPropertyAsString("ImageDisplay");
            String ImageResolution = ImageLandScape.getPropertyAsString("ImageResolution");
            String LinkDown = ImageLandScape.getPropertyAsString("LinkDown");
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
            final String WallpaperName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("WallpaperName");
            String CatetoryName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("CatetoryName");
            final int Download =
                    Integer.parseInt(getLinkImageLandscapeDetailResult.getPropertyAsString("Download"));
            String DateTime =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("DateTime");
            final String AuthorName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("AuthorName");
            MyLog.e("WallpaperName " + WallpaperName);
            MyLog.e("LinkDetail: " + LinkDetail);
            MyLog.e("ImageDisplay: " + ImageDisplay);
            MyLog.e("CatetoryName: " + CatetoryName);
            MyLog.e("ImageResolution: " + ImageResolution);
            MyLog.e("ImageSmall: " + ImageSmall);
//            MyLog.e("LinkDown: " + LinkDown);
//            MyLog.e("DateTime: " + DateTime);
//            MyLog.e("AuthorName: " + AuthorName);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if (getContext() != null) {
//                    Picasso.with(getContext()).load(ImageDisplay).into(ivDetail);
//                    Glide.with(getContext()).load(ImageDisplay).into(ivDetail);
                        Ion.with(getContext()).load(ImageDisplay).intoImageView(ivDetail);
                    }

//                    fDetail.setImageURI(Uri.parse(ImageDisplay));
                    LayoutDetailImage.setVisibility(View.VISIBLE);
                    tvTitleDetailImage.setText(WallpaperName);
                    tvCountViewDetailImage.setText(String.valueOf(Download));
                    tvAuthorName.setText(AuthorName);
//                    MyLog.e("Tab: " + ImageDisplay);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
