package com.soft.kent.bebluewallpaper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.soft.kent.bebluewallpaper.Listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.adapter.ImageAdapter;
import com.soft.kent.bebluewallpaper.model.ObjectImage;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class DetailCategoriesActivity extends AppCompatActivity {

    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private final String METHOD_NAME = "getLinkImageLandscape";
    private static String link;
    public static String ten;
    private ArrayList<ObjectImage> arrI;
    private RecyclerView rvAnh;
    private ImageAdapter imageAdapter;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datail_categories);
        init();
    }

    public void init() {
        Bundle bundle = getIntent().getBundleExtra("data");
        ten = bundle.getString("ten");
        link = bundle.getString("link");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setBackgroundResource(R.drawable.cover);
        ImageView cover = (ImageView) findViewById(R.id.cover);
        cover.setImageResource(R.drawable.cover);
        getSupportActionBar().setTitle(ten);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(android.R.color.transparent));
//        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.custom_title);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.naruto_hinata));
        index = 1;
        rvAnh = (RecyclerView) findViewById(R.id.recycler_view_detail_categories);
        rvAnh.setLayoutManager(new GridLayoutManager(this, 2));

        arrI = new ArrayList<>();
        new AsyncGetAllCategory().execute();
        imageAdapter = new ImageAdapter(rvAnh, arrI);
        rvAnh.setAdapter(imageAdapter);
        imageAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageAdapter.notifyItemRemoved(arrI.size());
                        new AsyncGetAllCategory().execute();

                    }
                }, 1);
            }

        });

//        rvAnh.addOnItemTouchListener(
//                new RecyclerItemClickListener(getBaseContext(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        Intent intent = new Intent(getBaseContext(), DetailImageActivity.class);
//                        intent.putExtra("linkDetail", arrI.get(position).getLinkDetail());
//                        startActivity(intent);
//                    }
//                })
//        );
    }


    @SuppressLint("NewApi")
    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getFahrenheit();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            imageAdapter.notifyDataSetChanged();

            Toast.makeText(getBaseContext(), "Load trang: " + index, Toast.LENGTH_SHORT).show();

            imageAdapter.setLoaded();
            index++;

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
        request.addProperty("sUrl", link + index);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject getLinkImageLandscapeResult = (SoapObject) response.getProperty("getLinkImageLandscapeResult");

            for (int i = 0; i < getLinkImageLandscapeResult.getPropertyCount(); i++) {
                SoapObject soapObject = (SoapObject) getLinkImageLandscapeResult.getProperty(i);
//                Log.e("STT: "+i,soapObject.toString());
                String ImageSmall = soapObject.getProperty("ImageSmall").toString();
                String LinkDetail = soapObject.getProperty("LinkDetail").toString();
                arrI.add(new ObjectImage(ImageSmall, LinkDetail));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
