package com.soft.kent.bebluewallpaper.tabs;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.soft.kent.bebluewallpaper.Listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.Listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.adapter.AnhAdapter;
import com.soft.kent.bebluewallpaper.model.Anh;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kentd on 18/05/2016.
 */
public class TabLatestWallpapers extends Fragment {
    private final String NAMESPACE = "http://tempuri.org/";
    private final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private final String METHOD_NAME = "getLinkImageLandscape";
    private static String link="http://www.hdwallpapers.in/latest_wallpapers/page/";
//    private static String link2="http://www.hdwallpapers.in/top_download_wallpapers.html";

    List<Anh> listAnh;
    private RecyclerView recyclerView;
    private AnhAdapter mAdapter;
    ProgressDialog dialog;
    int index = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_latestwallpaper, container, false);

        dialog = new ProgressDialog(getContext());
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        listAnh = new ArrayList<>();

        if(index == 1) {
            new AsyncGetAllCategory().execute();
        }

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);


        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdapter = new AnhAdapter(recyclerView, listAnh);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
//                Log.e("haint", "Load More");

                //Load more data for reyclerview
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        Log.e("haint", "Load More 2");
                        mAdapter.notifyItemRemoved(listAnh.size());
//                        mAdapter.setLoaded();
                        new  AsyncGetAllCategory().execute();

                    }
                }, 1);
            }


        });

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(),listAnh.get(position).getLinkDetail()+"", Toast.LENGTH_LONG).show();
                    }
                })
        );

        return v;

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
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), ""+index, Toast.LENGTH_SHORT).show();
            if(index == 1) {
                dialog.dismiss();
            }
            mAdapter.setLoaded();
            index++;


        }

        @Override
        protected void onPreExecute() {
//            Log.i(Constant.TAG, "onPreExecute");
            if(index == 1) {
                dialog.show();
            }



        }

        @Override
        protected void onProgressUpdate(Void... values) {
//            Log.i(Constant.TAG, "onProgressUpdate");
        }

    }

    public void getFahrenheit() {
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        request.addProperty("sUrl",link+index);
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
//            Log.e("getLinkImaglResponse",response.toString());
            SoapObject  getLinkImageLandscapeResult= (SoapObject) response.getProperty("getLinkImageLandscapeResult");
            for (int i=0;i<getLinkImageLandscapeResult.getPropertyCount();i++){
                SoapObject soapObject = (SoapObject)getLinkImageLandscapeResult.getProperty(i);
                Log.e("a "+i,soapObject.toString());
                Anh anh = new Anh();
                anh.setImageSmall(soapObject.getProperty("ImageSmall").toString());
                anh.setLinkDetail(soapObject.getProperty("LinkDetail").toString());

                listAnh.add(anh);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
