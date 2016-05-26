package com.soft.kent.bebluewallpaper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.tabs.TabDetailImage;
import com.soft.kent.bebluewallpaper.tabs.TabLatestWallpapers;
import com.soft.kent.bebluewallpaper.tabs.TabTopMostViewed;
import com.soft.kent.bebluewallpaper.view.Entity;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class DetailImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private final String METHOD_NAME = "getLinkImageLandscape";
    private static String link = "http://www.hdwallpapers.in/latest_wallpapers/page/";

    int positionPage = 0;
    int page = 1;
    int pageOpen = 1;
    Bundle bundle;
    AdapterViewPager adapter;
    ViewPager viewpager;
    Toolbar toolbar;

    private ArrayList<ObjectImage> arrI;
    private ProgressDialog dialog;
    private int index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image);
        arrI = new ArrayList<>();
//        init(getIntent());
        mahInit();
    }

    public void mahInit() {
        Intent it = getIntent();
        if (it != null) {
            toolbar = (Toolbar) findViewById(R.id.toolbarAllImage);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
            toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));

            viewpager = (ViewPager) findViewById(R.id.vPAllImage);
            adapter = new AdapterViewPager(getSupportFragmentManager());
            bundle = it.getBundleExtra("data");
            link = bundle.getString("linkPage");
            MyLog.e("REI: " + link);

            positionPage = bundle.getInt("position");
            page = Integer.valueOf(link.substring(link.length() - 1));

            MyLog.e("PAGE REI: " + page);
            String from = bundle.getString(Entity.KEY_DETAIL);
            MyLog.e("FROM " + from);
            if (from.equals(Entity.LATEST_WALLPAPER)) {
                arrI = TabLatestWallpapers.arrI;
            } else if (from.equals(Entity.TOP_MOST_VIEWED)) {
                arrI = TabTopMostViewed.arrI;
            } else if (from.equals(Entity.CATEGORIRES)) {
                arrI = DetailCategoriesActivity.arrI;
            }
            for (int i = 0; i < arrI.size(); i++) {
                adapter.addTab(new TabDetailImage(arrI.get(i).getLinkDetail(), arrI.size()), "");
            }
            setViewPager(viewpager);
            viewpager.setCurrentItem(positionPage);
        }
    }


    public void setViewPager(ViewPager viewPager) {
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
//        MyLog.e("position " + position);
        if (arrI != null && position == (arrI.size() - 1)) {
            page++;
            link = link.replace(link.substring(link.length() - 1), String.valueOf(page));
//            MyLog.e("MAX: " + link);
            new AsyncGetAllCategory().execute(link);

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


    @SuppressLint("NewApi")
    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            getCategories(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }

    public void getCategories(String link) {
        MyLog.e("TASK_DETAIL: " + link);
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
                adapter.addTab(new TabDetailImage(LinkDetail, arrI.size()), "");
            }
            MyLog.e("SIZE: " + arrI.size());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
