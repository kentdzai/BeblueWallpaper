package com.soft.kent.bebluewallpaper.model;

import com.soft.kent.bebluewallpaper.MyLog;
import com.soft.kent.bebluewallpaper.adapter.AdapterViewPager;
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
    private static final String NAME_SPACE = "http://tempuri.org/";
    private static final String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscape";
    private static final String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscape";
    private static final String METHOD_NAME = "getLinkImageLandscape";

    public static void getAllWallpaper(String link, ArrayList<ObjectImage> arrI) {
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
                arrI.add(new ObjectImage(ImageSmall, LinkDetail));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getAllWallpaperForDetailActivity(String link, ArrayList<ObjectImage> arrI, AdapterViewPager adapter) {
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
                arrI.add(new ObjectImage(ImageSmall, LinkDetail));
                adapter.addTab(new TabDetailImage(LinkDetail, i), "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
