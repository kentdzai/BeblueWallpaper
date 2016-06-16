package com.soft.kent.bebluewallpaper.controller;

import android.support.v7.widget.GridLayoutManager;

import com.soft.kent.bebluewallpaper.model.ObjectImage;

import java.util.ArrayList;

/**
 * Created by kentd on 13/06/2016.
 */
public class MySpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    int spanPos, spanCnt1, spanCnt2;
    ArrayList<ObjectImage> arrI;

    public MySpanSizeLookup(int spanPos, int spanCnt1, int spanCnt2) {
        this.spanPos = spanPos;
        this.spanCnt1 = spanCnt1;
        this.spanCnt2 = spanCnt2;
    }

    public MySpanSizeLookup(ArrayList<ObjectImage> arrI, int spanCnt1, int spanCnt2) {
        this.spanCnt1 = spanCnt1;
        this.spanCnt2 = spanCnt2;
        this.arrI = arrI;
    }

    @Override
    public int getSpanSize(int position) {
        return (arrI.get(position).advertisement ? spanCnt2 : spanCnt1);
    }
}
