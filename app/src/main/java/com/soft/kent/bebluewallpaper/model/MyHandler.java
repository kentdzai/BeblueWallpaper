package com.soft.kent.bebluewallpaper.model;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.Display;
import android.widget.Toast;

/**
 * Created by kentd on 27/05/2016.
 */
public class MyHandler {
    Activity activity;

    public MyHandler(Activity activity) {
        this.activity = activity;
    }

    public void myToast(String msg) {
        Toast.makeText(activity, "" + msg, Toast.LENGTH_LONG).show();
    }

    public int getScreenOrientation(Configuration configuration) {
        Display getOrient = activity.getWindowManager().getDefaultDisplay();
        int orientation = configuration.ORIENTATION_UNDEFINED;
        if (getOrient.getWidth() == getOrient.getHeight()) {
            orientation = configuration.ORIENTATION_SQUARE;
        } else {
            if (getOrient.getWidth() < getOrient.getHeight()) {
//                orientation = configuration.ORIENTATION_PORTRAIT;
                orientation = 2;
            } else {
//                orientation = configuration.ORIENTATION_LANDSCAPE;
                orientation = 4;
            }
        }
        return orientation;
    }
}
