package com.soft.kent.bebluewallpaper;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;

import com.soft.kent.bebluewallpaper.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.tabs.TabDetailImage;

public class DetailImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image);
        init();
    }

    private void init() {
        viewpager = (ViewPager) findViewById(R.id.vPAllImage);
        setViewPager(viewpager);

    }

    public void setViewPager(ViewPager viewPager) {
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
//        for (int i = 0; i < 5; i++) {
            adapter.addTab(new TabDetailImage(getIntent().getStringExtra("linkDetail")),"");
//        }
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
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



}
