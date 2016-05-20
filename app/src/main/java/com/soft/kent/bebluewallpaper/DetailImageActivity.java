package com.soft.kent.bebluewallpaper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.model.DetailImage;
import com.soft.kent.bebluewallpaper.model.ImageLandScape;
import com.squareup.picasso.Picasso;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.List;

public class DetailImageActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    Toolbar toolbar;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;
    RecyclerView rcRelatesDetailImage;
    List<ImageLandScape> landScapeList;
    List<DetailImage> listDetailImage;

    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscapeDetail";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscapeDetail";
    private final String METHOD_NAME = "getLinkImageLandscapeDetail";
    private static String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);
        init();
        new AsyncGetAllCategory().execute();

//        Picasso.with(this).load(listDetailImage.get(0).getImageDisplay()).into(ivDetail);
    }

    private void init() {
        link = getIntent().getStringExtra("linkDetail");
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));
        ivDetail = (ImageView) findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) findViewById(R.id.tvAuthorName);
//        rcRelatesDetailImage = (RecyclerView) findViewById(R.id.rcRelatesDetailImage);

        fabFavorite.setOnClickListener(this);
        btnDownloadImage.setOnClickListener(this);
        btnSetWallpaper.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabFavorite:
                thongBao("Replace with your own action", v);
                break;
            case R.id.btnDownloadImage:
                thongBao("Downloading..", v);
                break;
            case R.id.btnSetWallpaper:
                thongBao("Set as wallpaper..", v);
                break;
        }
    }

    public void thongBao(String msg, View v) {
        Snackbar.make(  v, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
//            Log.i(Constant.TAG, "doInBackground");
            getFahrenheit();

            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
//            Log.i(Constant.TAG, "onPostExecute");

        }

        @Override
        protected void onPreExecute() {
//            Log.i(Constant.TAG, "onPreExecute");


        }

        @Override
        protected void onProgressUpdate(Void... values) {
//            Log.i(Constant.TAG, "onProgressUpdate");
        }

    }

    public void getFahrenheit() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("sUrl",link);
//        Log.e("request", request.toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            //Invole web service
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            Log.e("getLinkImaglResponse",response.toString());
            SoapObject  getLinkImageLandscapeResult= (SoapObject) response.getProperty("getLinkImageLandscapeDetailResult");

            for (int i=0;i<getLinkImageLandscapeResult.getPropertyCount();i++){
                SoapObject soapObject = (SoapObject)getLinkImageLandscapeResult.getProperty(i);
                Log.e("a "+i,soapObject.toString());
                DetailImage detailImage = new DetailImage();
                detailImage.setImageDisplay(soapObject.getProperty("ImageDisplay").toString());
                SoapObject  getDownloadLinks= (SoapObject) response.getProperty("ImageLandScape");

                for (int n = 0; n < getDownloadLinks.getPropertyCount(); n++) {
                    SoapObject soapObject1 = (SoapObject)getDownloadLinks.getProperty(i);
                    ImageLandScape imageLandScape = new ImageLandScape();
                    imageLandScape.setImageResolution(soapObject1.getProperty("ImageResolution").toString());
                    imageLandScape.setLinkDown(soapObject1.getProperty("LinkDown").toString());

                    landScapeList.add(imageLandScape);
                }
                detailImage.setList(landScapeList);
                listDetailImage.add(detailImage);

            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(DetailImageActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.action_set_wallpaper:
//                Đặt làm hình nền
                break;
            case R.id.action_set_contact:
//                Đặt làm liên hệ
                break;
            case R.id.action_share_image:
//                Chia sẻ
                break;
            case R.id.action_download_image:
//                Tải về
                break;
            case R.id.action_add_favorite:
//                Thêm vào mục yêu thích
                break;
            case R.id.action_report_image:
//                Báo cáo
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
