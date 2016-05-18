package com.soft.kent.bebluewallpaper.tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soft.kent.bebluewallpaper.R;

/**
 * Created by kentd on 18/05/2016.
 */
public class TabTopMostViewed extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_topmostviewed, container, false);
        init(v);
        return v;
    }

    private void init(View v) {


    }
}
