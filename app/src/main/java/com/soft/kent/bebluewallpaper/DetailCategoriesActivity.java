package com.soft.kent.bebluewallpaper;

import android.content.Intent;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.Listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.Listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.adapter.ImageAdapter;
import com.soft.kent.bebluewallpaper.listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.view.Entity;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.util.ArrayList;

public class DetailCategoriesActivity extends AppCompatActivity implements OnLoadMoreListener {
    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private final String METHOD_NAME = "getLinkImageLandscape";

    private static String linkCategories;
    public static String titleCategories;
    int avatar;

    Toolbar toolbar;
    ImageView ivAvatar;
    TextView tvTitleCategories;

    public static ArrayList<ObjectImage> arrI;
    private RecyclerView rvAnh;
    private ImageAdapter imageAdapter;
    private int index = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datail_categories);
        init();
    }

    public void init() {
        arrI = new ArrayList<>();

        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        toolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        tvTitleCategories = (TextView) findViewById(R.id.tvTitleCategories);
        rvAnh = (RecyclerView) findViewById(R.id.recycler_view_detail_categories);
        rvAnh.setLayoutManager(new GridLayoutManager(this, 2));

        Bundle bundle = getIntent().getBundleExtra("data");
        ten = bundle.getString("ten");
        link = bundle.getString("link");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleCategories = bundle.getString("titleCategories");
        linkCategories = bundle.getString("linkCategories") + index;
        MyLog.e("CAT REI: " + linkCategories);
        avatar = bundle.getInt("imgAvatar");

        tvTitleCategories.setText(titleCategories);
        ivAvatar.setImageResource(avatar);
        toolbar.setTitle(titleCategories);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView cover = (ImageView) findViewById(R.id.cover);
        cover.setImageResource(R.mipmap.cover);
        getSupportActionBar().setTitle(ten);
        index = 1;
        rvAnh = (RecyclerView) findViewById(R.id.recycler_view_detail_categories);
        rvAnh.setLayoutManager(new GridLayoutManager(this, 2));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);


        if (linkCategories.endsWith("/")) {
            linkCategories = new StringBuilder(linkCategories).append(index).toString();
        } else {
            index = Integer.parseInt(linkCategories.substring(linkCategories.length() - 1));
        }

        new AsyncGetAllCategory().execute(linkCategories);

        imageAdapter = new ImageAdapter(rvAnh, arrI);
        rvAnh.setAdapter(imageAdapter);
        imageAdapter.setOnLoadMoreListener(this);

        rvAnh.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("linkPage", linkCategories);
                        bundle.putInt("position", position);
                        bundle.putString(Entity.KEY_DETAIL, Entity.CATEGORIRES);
                        Intent intent = new Intent(getBaseContext(), DetailImageActivity.class);
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                })
        );
    }

    @Override
    public void onLoadMore() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                imageAdapter.notifyItemRemoved(arrI.size());
                linkCategories = linkCategories.replace("/page/" + (index - 1), "/page/" + index);
                new AsyncGetAllCategory().execute(linkCategories);
            }
        });

        rvAnh.addOnItemTouchListener(
                new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
//                        Intent intent = new Intent(getBaseContext(), DetailImageActivity.class);
//                        intent.putExtra("linkDetail", arrI.get(position).getLinkDetail());
//                        startActivity(intent);

                    }
                })
        );
    }



    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getAllLatestWallpaper(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            imageAdapter.notifyDataSetChanged();
            imageAdapter.setLoaded();
//            if (index == 1) {
//                dialog.dismiss();
//            }
            index++;
            if (arrI != null) {
                Picasso.with(getApplicationContext())
                        .load(arrI.get(arrI.size() - 1).getImageSmall())
                        .into(ivAvatar);
            }
        }

        @Override
        protected void onPreExecute() {
//            if (index == 1) {
//                dialog.show();
//            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void getAllLatestWallpaper(String link) {
        MyLog.e("TASK_CATEGORIES: " + link);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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