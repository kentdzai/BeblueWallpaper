package com.soft.kent.bebluewallpaper.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by kentd on 20/05/2016.
 */
public class AdapterViewPager extends FragmentPagerAdapter {

    ArrayList<Fragment> arrF = new ArrayList<>();
    ArrayList<String> arrT = new ArrayList<>();

    public AdapterViewPager(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        return arrF.get(position);
    }

    @Override
    public int getCount() {
        return arrF.size();
    }

    public void addTab(Fragment fragment, String title) {
        arrF.add(fragment);
        arrT.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return arrT.get(position);

    }
}