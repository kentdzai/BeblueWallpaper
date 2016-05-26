package com.soft.kent.bebluewallpaper.model;

import com.soft.kent.bebluewallpaper.MyLog;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by kentd on 26/05/2016.
 */
public class GetDetailImage {
    private final static String NAME_SPACE = "http://tempuri.org/";
    private final static String URL = "http://api.ixinh.net/services.asmx?op=getLinkImageLandscapeDetail";
    private final static String SOAP_ACTION = "http://tempuri.org/getLinkImageLandscapeDetail";
    private final static String METHOD_NAME = "getLinkImageLandscapeDetail";

    public static ObjectDetailImage getDetailsImage(String link) {
//        MyLog.e("DETAIL TASK: " + link);
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
            SoapObject getLinkImageLandscapeDetailResult = (SoapObject)
                    response.getProperty("getLinkImageLandscapeDetailResult");
            SoapObject DownloadLinks = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("DownloadLinks");
            SoapObject ImageLandScape = (SoapObject)
                    DownloadLinks.getProperty("ImageLandScape");
            String ImageDisplay = getLinkImageLandscapeDetailResult.getPropertyAsString("ImageDisplay");
            String ImageResolution = ImageLandScape.getPropertyAsString("ImageResolution");
            String LinkDown = ImageLandScape.getPropertyAsString("LinkDown");
            SoapObject ImageRelate = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("ImageRelate");
            SoapObject ImageLandscapeThumb = (SoapObject)
                    ImageRelate.getProperty("ImageLandscapeThumb");
            String ImageSmall = ImageLandscapeThumb.getPropertyAsString("ImageSmall");
            String LinkDetail = ImageLandscapeThumb.getPropertyAsString("LinkDetail");
            SoapObject Tags = (SoapObject)
                    getLinkImageLandscapeDetailResult.getProperty("Tags");
            String tag1 = Tags.getPropertyAsString(0);
            String tag2 = Tags.getPropertyAsString(1);
            String WallpaperName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("WallpaperName");
            String CatetoryName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("CatetoryName");
            int Download =
                    Integer.parseInt(getLinkImageLandscapeDetailResult.getPropertyAsString("Download"));
            String DateTime =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("DateTime");
            String AuthorName =
                    getLinkImageLandscapeDetailResult.getPropertyAsString("AuthorName");
//            MyLog.e("WallpaperName " + WallpaperName);
//            MyLog.e("LinkDetail: " + LinkDetail);
//            MyLog.e("ImageDisplay: " + ImageDisplay);
//            MyLog.e("CatetoryName: " + CatetoryName);
//            MyLog.e("ImageResolution: " + ImageResolution);
//            MyLog.e("ImageSmall: " + ImageSmall);
//            MyLog.e("LinkDown: " + LinkDown);
//            MyLog.e("DateTime: " + DateTime);
//            MyLog.e("AuthorName: " + AuthorName);

            return new ObjectDetailImage(WallpaperName, ImageDisplay, LinkDown, Download, AuthorName);
        } catch (Exception e) {
            MyLog.e("http://stackoverflow.com/search?q=" + e.getMessage());
        }
        return null;
    }
}










