package com.soft.kent.bebluewallpaper.controller;

import android.os.Bundle;

import com.soft.kent.bebluewallpaper.view.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.model.DatabaseWallpaper;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.tabs.TabDetailImage;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

/**
 * Created by kentd on 26/05/2016.
 */
public class GetPage {
    public final static String KEY_DETAIL = "KEY_DETAIL";
    public final static String LATEST_WALLPAPER = "LATEST_WALLPAPER";
    public final static String TOP_MOST_VIEWED = "TOP_MOST_VIEWED";
    public final static String CATEGORIRES = "CATEGORIRES";
    public final static String FAVORITE = "FAVORITE";

    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private static final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private static final String METHOD_NAME = "getLinkImageLandscape";

    DatabaseWallpaper db;

    public static void getAllWallpaper(String link, ArrayList<ObjectImage> arrI, DatabaseWallpaper db) {
        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty("sUrl", link);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject getLinkImageLandscapeResult = (SoapObject)
                    response.getProperty("getLinkImageLandscapeResult");
            for (int i = 0; i < getLinkImageLandscapeResult.getPropertyCount(); i++) {
                SoapObject soapObject = (SoapObject) getLinkImageLandscapeResult.getProperty(i);
                String ImageSmall = soapObject.getProperty("ImageSmall").toString();
                String LinkDetail = soapObject.getProperty("LinkDetail").toString();
                arrI.add(new ObjectImage(ImageSmall, LinkDetail, db.checkFavorite(LinkDetail), false));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        arrI.add(new ObjectImage("", "", false, true));
    }

    public static void getAllWallpaperForDetailActivity(String link, ArrayList<ObjectImage> arrI, AdapterViewPager adapter, DatabaseWallpaper db) {
//        MyLog.e("TASK GET PAGE: " + link);
        SoapObject request = new SoapObject(NAME_SPACE, METHOD_NAME);
        request.addProperty("sUrl", link);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            SoapObject getLinkImageLandscapeResult = (SoapObject)
                    response.getProperty("getLinkImageLandscapeResult");
            for (int i = 0; i < getLinkImageLandscapeResult.getPropertyCount(); i++) {
                SoapObject soapObject = (SoapObject) getLinkImageLandscapeResult.getProperty(i);
                String ImageSmall = soapObject.getProperty("ImageSmall").toString();
                String LinkDetail = soapObject.getProperty("LinkDetail").toString();
                arrI.add(new ObjectImage(ImageSmall, LinkDetail, db.checkFavorite(LinkDetail)));
                TabDetailImage tD = new TabDetailImage();
                Bundle bD = new Bundle();
                bD.putString("LinkDetail", LinkDetail);
                bD.putString("ImageSmall", ImageSmall);
                tD.setArguments(bD);
                adapter.addTab(tD, "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
