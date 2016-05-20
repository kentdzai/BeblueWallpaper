package com.soft.kent.bebluewallpaper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.soft.kent.bebluewallpaper.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.tabs.TabDetailImage;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class DetailImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private final String METHOD_NAME = "getLinkImageLandscape";
    private static String link = "http://www.hdwallpapers.in/latest_wallpapers/page/";

    private ArrayList<ObjectImage> arrI;
    private ProgressDialog dialog;
    private int index;
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image);
        init();
    }

    private void init() {
        viewpager = (ViewPager) findViewById(R.id.vPAllImage);
        new AsyncGetAllCategory().execute();
    }

    public void setViewPager(ViewPager viewPager) {
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        if (arrI != null) {
            for (int i = 0; i < arrI.size(); i++) {
                adapter.addTab(new TabDetailImage(arrI.get(i).getLinkDetail()), "");
            }
        }

        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getCategories();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (index == 1) {
                dialog.dismiss();
            }
            index++;
            setViewPager(viewpager);
        }

        @Override
        protected void onPreExecute() {

            if (index == 1) {
                dialog.show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

    }

    public void getCategories() {
        arrI = new ArrayList<>();
        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty("sUrl", link + index);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject getLinkImageLandscapeResult = (SoapObject)
                    response.getProperty("getLinkImageLandscapeResult");

            for (int i = 0; i < getLinkImageLandscapeResult.getPropertyCount(); i++) {
                SoapObject soapObject = (SoapObject) getLinkImageLandscapeResult.getProperty(i);
                Log.e("STT: " + i, soapObject.toString());
                ObjectImage objectImage = new ObjectImage();
                objectImage.setImageSmall(soapObject.getProperty("ImageSmall").toString());
                objectImage.setLinkDetail(soapObject.getProperty("LinkDetail").toString());
                arrI.add(objectImage);
            }
            MyLog.e(arrI.get(0).getImageSmall());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
