package com.soft.kent.bebluewallpaper;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.soft.kent.bebluewallpaper.view.adapter.PageAdapter;
import com.soft.kent.bebluewallpaper.controller.GetPage;
import com.soft.kent.bebluewallpaper.controller.MyHandler;
import com.soft.kent.bebluewallpaper.controller.listener.OnLoadMoreListener;
import com.soft.kent.bebluewallpaper.controller.listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.model.DatabaseWallpaper;
import com.soft.kent.bebluewallpaper.model.ObjectImage;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements OnLoadMoreListener, NavigationView.OnNavigationItemSelectedListener {
    public static ArrayList<ObjectImage> arrI;
    public static PageAdapter pageAdapter;
    public static int index = 1;

    private RecyclerView rcFavorite;
    DatabaseWallpaper db;
    MyHandler mh;
    int column = 2;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        init();
    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.favToolbar);
        toolbar.setTitle("Favorite");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fav_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.fav_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = new DatabaseWallpaper(this);
        arrI = new ArrayList<>();
        arrI = db.queryAllFavorite();
        mh = new MyHandler(this);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            column = 2;
        } else {
            column = 4;
        }

        rcFavorite = (RecyclerView) findViewById(R.id.rcFavorite);
        rcFavorite.setLayoutManager(new GridLayoutManager(this, column));

        pageAdapter = new PageAdapter(rcFavorite, arrI);

        rcFavorite.setAdapter(pageAdapter);
        pageAdapter.setOnLoadMoreListener(this);
        rcFavorite.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(FavoriteActivity.this, DetailImageActivity.class);
                        intent.putExtra("data", mh.pushDetail(null, position, arrI, GetPage.FAVORITE));
                        startActivity(intent);
                    }
                })
        );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        rcFavorite.setLayoutManager(new GridLayoutManager(this, mh.getScreenOrientation(newConfig)));
    }

    @Override
    public void onLoadMore() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                pageAdapter.notifyItemRemoved(arrI.size());
            }
        });
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(FavoriteActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.nav_favorite:

                break;
            case R.id.nav_history:

                break;
            case R.id.nav_setting:

                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.fav_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
