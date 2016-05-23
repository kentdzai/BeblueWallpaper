package com.soft.kent.bebluewallpaper.tabs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.soft.kent.bebluewallpaper.DetailImageActivity;
import com.soft.kent.bebluewallpaper.Listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.Listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.adapter.ImageAdapter;
import com.soft.kent.bebluewallpaper.model.ObjectImage;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class TabLatestWallpapers extends Fragment {

    private final String NAME_SPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private final String METHOD_NAME = "getLinkImageLandscape";
    private static String link ="http://www.hdwallpapers.in/latest_wallpapers/page/";

    private ArrayList<ObjectImage> arrI;
    private RecyclerView rvAnh;
    private ImageAdapter imageAdapter;
    private ProgressDialog dialog;
    private int index;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  v = inflater.inflate(R.layout.tab_latestwallpaper, container, false);

        init(v);

        return v;

    }

    public void init(View v){
        index = 1;
        rvAnh = (RecyclerView) v.findViewById(R.id.recycler_view);
        rvAnh.setLayoutManager(new GridLayoutManager(getContext(), 2));
        dialog = new ProgressDialog(getContext());
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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
                        new  AsyncGetAllCategory().execute();

                    }
                }, 1);
            }

        });

        rvAnh.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("linkDetail", arrI.get(position).getLinkDetail());
                        bundle.putInt("position", position);
                        Intent intent = new Intent(getActivity(), DetailImageActivity.class);
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                })
        );
    }


    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            getFahrenheit();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            imageAdapter.notifyDataSetChanged();

//            Toast.makeText(getActivity(), "Load trang: "+index, Toast.LENGTH_SHORT).show();

            if(index == 1) {
                dialog.dismiss();
            }

            imageAdapter.setLoaded();
            index++;

        }

        @Override
        protected void onPreExecute() {

            if(index == 1) {
                dialog.show();
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

    }

    public void getFahrenheit() {

        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty("sUrl",link+index);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject  getLinkImageLandscapeResult= (SoapObject)
                    response.getProperty("getLinkImageLandscapeResult");

            for (int i = 0;i < getLinkImageLandscapeResult.getPropertyCount(); i++){
                SoapObject soapObject = (SoapObject)getLinkImageLandscapeResult.getProperty(i);
                String ImageSmall = soapObject.getProperty("ImageSmall").toString();
                String LinkDetail = soapObject.getProperty("LinkDetail").toString();
                arrI.add(new ObjectImage(ImageSmall, LinkDetail));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
