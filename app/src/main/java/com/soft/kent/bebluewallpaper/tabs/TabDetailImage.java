package com.soft.kent.bebluewallpaper.tabs;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import org.ksoap2.serialization.SoapPrimitive;
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
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;
    String a;
    public TabDetailImage(String link) {
        this.a = link;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_detail_image, container, false);
        init(v);
        new AsyncGetAllCategory().execute();
        ;
        return v;
    }





    private void init(View v) {

        arrrObbjectDetailImage = new ArrayList<>();
        ivDetail = (ImageView) v.findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) v.findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) v.findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) v.findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) v.findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) v.findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) v.findViewById(R.id.tvAuthorName);

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
            SoapObject  getLinkImageLandscapeResult = (SoapObject) response.getProperty("getLinkImageLandscapeDetailResult");

            SoapPrimitive soapObject = (SoapPrimitive) getLinkImageLandscapeResult.getProperty("ImageDisplay");


            ObjectDetailImage objectImage = new ObjectDetailImage();

            objectImage.setImageDisplay(soapObject.toString());
            objectImage.setLinkDownload("");
            arrrObbjectDetailImage.add(objectImage);
            Log.e("aaaaaaaaa",soapObject.toString());
            Picasso.with(getContext()).load("http://www.hdwallpapers.in/thumbs/michelangelo_tmnt_out_of_the_shadows-t2.jpg").into(ivDetail);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("abc", e.toString());
        }
    }




}
