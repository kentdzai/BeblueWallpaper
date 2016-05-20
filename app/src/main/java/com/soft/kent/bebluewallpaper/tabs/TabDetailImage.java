package com.soft.kent.bebluewallpaper.tabs;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

@SuppressLint("ValidFragment")
public class TabDetailImage extends Fragment {

    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscapeDetail";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscapeDetail";
    private final String METHOD_NAME = "getLinkImageLandscapeDetail";
    private String link = "http://www.hdwallpapers.in/assetto_corsa-wallpapers.html";
    ObjectDetailImage objectDetailImage;
    Toolbar toolbar;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;

    public TabDetailImage(String link) {
//        this.link = link;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        new AsyncGetAllCategory().execute();
        showImage();
        return v;
    }

    private void init(View v) {

//        toolbar = (Toolbar) v.findViewById(R.id.toolbar1);
//        getActivity().setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
//        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));

        objectDetailImage = new ObjectDetailImage();
        ivDetail = (ImageView) v.findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) v.findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) v.findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) v.findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) v.findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) v.findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);

    }

    public void showImage() {
        if (link != null) {
            Picasso.with(getActivity()).load(objectDetailImage.getImageDisplay()).centerCrop().into(ivDetail);
        }

    }

    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getFahrenheit();
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

    public void getFahrenheit() {

        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty("sUrl",link);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject  getLinkImageLandscapeResult= (SoapObject) response.getProperty("getLinkImageLandscapeDetailResult");

            for (int i = 0;i < getLinkImageLandscapeResult.getPropertyCount(); i++){
                SoapObject soapObject = (SoapObject)getLinkImageLandscapeResult.getProperty(i);
                Log.e("STT: "+i,soapObject.toString());

                objectDetailImage.setImageDisplay(soapObject.getProperty("ImageResolution").toString());
                objectDetailImage.setLinkDownload(soapObject.getProperty("LinkDown").toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
