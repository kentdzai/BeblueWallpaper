package com.soft.kent.bebluewallpaper;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.soft.kent.bebluewallpaper.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.tabs.TabCategories;
import com.soft.kent.bebluewallpaper.tabs.TabLatestWallpapers;
import com.soft.kent.bebluewallpaper.tabs.TabTopMostViewed;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    Toolbar toolbar;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.mipmap.ic_menu2);
        tabs = (TabLayout) findViewById(R.id.tabs);
        setViewPager(viewPager);
        tabs.setupWithViewPager(viewPager);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setViewPager(ViewPager viewPager) {
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        adapter.addTab(new TabCategories(), "Categories");
        adapter.addTab(new TabLatestWallpapers(), "Latest Wallpapers");
        adapter.addTab(new TabTopMostViewed(), "Top Most Viewed");
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:

                break;
            case R.id.nav_favorite:

                break;
            case R.id.nav_history:

                break;
            case R.id.nav_setting:

                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
