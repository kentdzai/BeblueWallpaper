package com.soft.kent.bebluewallpaper;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.controller.MyLog;
import com.soft.kent.bebluewallpaper.view.adapter.PageAdapter;
import com.soft.kent.bebluewallpaper.controller.MySpanSizeLookup;
import com.soft.kent.bebluewallpaper.controller.listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.controller.GetPage;
import com.soft.kent.bebluewallpaper.controller.MyHandler;
import com.soft.kent.bebluewallpaper.model.DatabaseWallpaper;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class DetailCategoriesActivity extends AppCompatActivity implements OnLoadMoreListener {
    private static String linkCategories;
    public static String titleCategories;
    int avatar;

    Toolbar toolbar;
    ImageView ivAvatar;
    TextView tvTitleCategories;

    public static ArrayList<ObjectImage> arrI;
    private RecyclerView rcDetailCategories;
    public static PageAdapter pageAdapter;
    public static int index = 1;
    DatabaseWallpaper db;
    MyHandler mh;
    int column = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_categories);
        init();
    }

    GridLayoutManager gridLayoutManager;

    public void init() {
        arrI = new ArrayList<>();
        db = new DatabaseWallpaper(this);
        mh = new MyHandler(DetailCategoriesActivity.this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            column = 2;
        else column = 4;

        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        toolbar = (Toolbar) findViewById(R.id.toolbarCategories);
        tvTitleCategories = (TextView) findViewById(R.id.tvTitleCategories);
        rcDetailCategories = (RecyclerView) findViewById(R.id.rcDetailCategories);
        gridLayoutManager = new GridLayoutManager(this, column);
        rcDetailCategories.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new MySpanSizeLookup(arrI, 1, 2));
        Bundle bundle = getIntent().getBundleExtra("data");
        titleCategories = bundle.getString("titleCategories");
        linkCategories = bundle.getString("linkCategories") + index;
        MyLog.e("TITLE REI: " + titleCategories);
        MyLog.e("CAT REI: " + linkCategories);
        avatar = bundle.getInt("imgAvatar");

        tvTitleCategories.setText(titleCategories);
        ivAvatar.setImageResource(avatar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);

        if (linkCategories.endsWith("/")) {
            linkCategories = new StringBuilder(linkCategories).append(index).toString();
        } else {
            index = Integer.parseInt(linkCategories.substring(linkCategories.length() - 1));
        }

        new AsyncGetAllCategory().execute(linkCategories);

        pageAdapter = new PageAdapter(rcDetailCategories, arrI, linkCategories, GetPage.CATEGORIRES);
        rcDetailCategories.setAdapter(pageAdapter);
        pageAdapter.setOnLoadMoreListener(this);

    }

    public void getKeyHash() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.soft.kent.bebluewallpaper", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    @Override
    public void onLoadMore() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                pageAdapter.notifyItemRemoved(arrI.size());
                linkCategories = linkCategories.replace("/page/" + (index - 1), "/page/" + index);
                new AsyncGetAllCategory().execute(linkCategories);
            }
        });
    }

    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            GetPage.getAllWallpaper(params[0], arrI, db);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            pageAdapter.notifyDataSetChanged();
            pageAdapter.setLoaded();
            index++;
            if (arrI != null) {
                if (arrI.size() > 0) {
                    Picasso.with(getApplicationContext())
                            .load(arrI.get(arrI.size() - 2).imageSmall)
                            .into(ivAvatar);

                }

            }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rcDetailCategories.setLayoutManager(gridLayoutManager);
        gridLayoutManager.setSpanSizeLookup(new MySpanSizeLookup(arrI, 1, mh.getScreenOrientation(newConfig)));
    }
}