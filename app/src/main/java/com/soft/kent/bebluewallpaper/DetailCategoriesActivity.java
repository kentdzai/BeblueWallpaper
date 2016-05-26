package com.soft.kent.bebluewallpaper;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.soft.kent.bebluewallpaper.adapter.ImageAdapter;
import com.soft.kent.bebluewallpaper.listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.model.GetPage;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.model.Entity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailCategoriesActivity extends AppCompatActivity implements OnLoadMoreListener {
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
        titleCategories = bundle.getString("titleCategories");
        linkCategories = bundle.getString("linkCategories") + index;
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
                    @Override
                    public void onItemClick(View view, int position) {
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
            GetPage.getAllWallpaper(params[0], arrI);
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