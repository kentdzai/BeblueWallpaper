package com.soft.kent.bebluewallpaper.tabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.soft.kent.bebluewallpaper.DetailImageActivity;
import com.soft.kent.bebluewallpaper.MyLog;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.adapter.ImageAdapter;
import com.soft.kent.bebluewallpaper.listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.model.GetPage;
import com.soft.kent.bebluewallpaper.model.MyHandler;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.model.Entity;

import java.util.ArrayList;

/**
 * Created by kentd on 18/05/2016.
 */
public class TabTopMostViewed extends Fragment implements com.soft.kent.bebluewallpaper.listener.OnLoadMoreListener {
    private static String link = "http://www.hdwallpapers.in/top_view_wallpapers/page/";

    public static ArrayList<ObjectImage> arrI;
    private RecyclerView rcTopMostViewed;
    private ImageAdapter imageAdapter;
    private ProgressDialog dialog;
    public static int index = 1;

    Activity activity;
    MyHandler mh;
    int column = 2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_topmostviewed, container, false);
        init(v);
        return v;
    }

    public void init(View v) {
        arrI = new ArrayList<>();

        mh = new MyHandler(activity);
        if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            column = 2;
        } else {
            column = 4;
        }
        rcTopMostViewed = (RecyclerView) v.findViewById(R.id.rcTopMostViewed);
        rcTopMostViewed.setLayoutManager(new GridLayoutManager(getContext(), column));

        if (link.endsWith("/")) {
            link = new StringBuilder(link).append(index).toString();
        } else {
            index = Integer.parseInt(link.substring(link.length() - 1));
        }

        dialog = new ProgressDialog(getContext());
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        new AsyncGetAllCategory().execute(link);

        imageAdapter = new ImageAdapter(rcTopMostViewed, arrI);
        rcTopMostViewed.setAdapter(imageAdapter);
        imageAdapter.setOnLoadMoreListener(this);
        rcTopMostViewed.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Bundle bundle = new Bundle();
                        bundle.putString("linkPage", link);
                        bundle.putInt("position", position);
                        bundle.putString(Entity.KEY_DETAIL, Entity.TOP_MOST_VIEWED);
                        Intent intent = new Intent(getActivity(), DetailImageActivity.class);
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
                link = link.replace("/page/" + (index - 1), "/page/" + index);
                new AsyncGetAllCategory().execute(link);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MyLog.e("onResume: " + index);
    }

    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            GetPage.getAllWallpaper(params[0], arrI);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            imageAdapter.notifyDataSetChanged();
            imageAdapter.setLoaded();
            index++;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rcTopMostViewed.setLayoutManager(new GridLayoutManager(getContext(), mh.getScreenOrientation(newConfig)));
    }
}
