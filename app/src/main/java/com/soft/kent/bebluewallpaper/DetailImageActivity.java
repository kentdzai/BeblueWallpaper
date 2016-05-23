package com.soft.kent.bebluewallpaper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
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
    int page = 1;
    AdapterViewPager adapter;
    ViewPager viewpager;

    private ArrayList<ObjectImage> arrI;
    private ProgressDialog dialog;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image);
        init();
    }

    private void init() {
        if (link.endsWith("/")) {
            link = new StringBuilder(link).append(page).toString();
        } else {
            page = Integer.parseInt(link.substring(link.length() - 1));
        }
        arrI = new ArrayList<>();
        viewpager = (ViewPager) findViewById(R.id.vPAllImage);
        adapter = new AdapterViewPager(getSupportFragmentManager());
        new AsyncGetAllCategory().execute(link);
    }

    public void setViewPager(ViewPager viewPager) {
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    int i = 0;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


    }

    @Override
    public void onPageSelected(int position) {
        if (arrI != null && position == (arrI.size() - 4)) {
            page++;
            if (!link.endsWith("/")) {
//                MyLog.e("Link 1: " + link);
//                MyLog.e("Page: " + page);
                link = link.replace(link.substring(link.length() - 1), String.valueOf(page));
//                MyLog.e("Link 2: " + link);
                new AsyncGetAllCategory().execute(link);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getCategories(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (arrI.size() == 14) {
                setViewPager(viewpager);
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    public void getCategories(String link) {
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
            SoapObject getLinkImageLandscapeResult = (SoapObject)
                    response.getProperty("getLinkImageLandscapeResult");
            for (int i = 0; i < getLinkImageLandscapeResult.getPropertyCount(); i++) {
                SoapObject soapObject = (SoapObject) getLinkImageLandscapeResult.getProperty(i);
                String ImageSmall = soapObject.getProperty("ImageSmall").toString();
                String LinkDetail = soapObject.getProperty("LinkDetail").toString();
                arrI.add(new ObjectImage(ImageSmall, LinkDetail));
                adapter.addTab(new TabDetailImage(LinkDetail), "");
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }
}
