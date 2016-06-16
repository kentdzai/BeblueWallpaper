package com.soft.kent.bebluewallpaper;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.soft.kent.bebluewallpaper.view.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.controller.GetDetailImage;
import com.soft.kent.bebluewallpaper.controller.GetPage;
import com.soft.kent.bebluewallpaper.controller.MyHandler;
import com.soft.kent.bebluewallpaper.model.DatabaseWallpaper;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.soft.kent.bebluewallpaper.model.ObjectDownloads;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.tabs.TabDetailImage;
import com.soft.kent.bebluewallpaper.tabs.TabLatestWallpapers;
import com.soft.kent.bebluewallpaper.tabs.TabTopMostViewed;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DetailImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    private static String link = "http://www.hdwallpapers.in/latest_wallpapers/page/";

    public static int positionPage = 0;
    int page = 1;
    Bundle bundle;
    String from;
    final String original = "-Original";
    AdapterViewPager adapter;
    ViewPager viewpager;
    Toolbar toolbar;
    int positionFragment;
    DatabaseWallpaper db;

    public static ArrayList<ObjectImage> arrI;

    ObjectDetailImage oD;
    public static final int PICK_CONTACT = 2;
    TabDetailImage tD;
    MyHandler myHandler;
    public static MenuItem itFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image);
        init(getIntent());
    }

    public void init(Intent it) {
        arrI = new ArrayList<>();
        db = new DatabaseWallpaper(this);
        if (it != null) {
            myHandler = new MyHandler(this);
            toolbar = (Toolbar) findViewById(R.id.toolbarAllImage);
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
            toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));
            viewpager = (ViewPager) findViewById(R.id.vPAllImage);
            adapter = new AdapterViewPager(getSupportFragmentManager());
            bundle = it.getBundleExtra("data");
            link = bundle.getString("linkPage");
            positionPage = bundle.getInt("position");

            from = bundle.getString(GetPage.KEY_DETAIL);
            arrI = (ArrayList<ObjectImage>) bundle.getSerializable(GetDetailImage.LIST_DETAIL);
            if (link != null)
                page = Integer.valueOf(link.substring(link.length() - 1));

            for (int i = 0; i < arrI.size(); i++) {
                TabDetailImage tD = new TabDetailImage();
                ObjectImage oi = arrI.get(i);
                Bundle bD = new Bundle();
                bD.putString("LinkDetail", oi.linkDetail);
                bD.putSerializable("ImageSmall", oi.imageSmall);
                tD.setArguments(bD);
                adapter.addTab(tD, "");
            }
            setViewPager(viewpager);
            viewpager.setCurrentItem(positionPage);
        }
    }

    public void setViewPager(ViewPager viewPager) {
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        positionFragment = position;
        if (arrI != null && itFav != null) {
            if (tD == null)
                tD = (TabDetailImage) adapter.getItem(positionFragment);
            boolean f = db.checkFavorite(arrI.get(position).linkDetail);
            String t = parseBooleanFavToTitle(f);
            itFav.setTitle(t);
        }
        if (arrI != null && position == (arrI.size() - 1)) {
            page++;
            if (link != null) {
                link = link.replace(link.substring(link.length() - 1), String.valueOf(page));
                new AsyncGetAllCategory().execute(link);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            GetPage.getAllWallpaperForDetailActivity(params[0], arrI, adapter, db);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        itFav = menu.findItem(R.id.action_add_favorite);
        boolean f = db.checkFavorite(arrI.get(positionPage).linkDetail);
        String t = parseBooleanFavToTitle(f);
        itFav.setTitle(t);
        return true;
    }

    ArrayList<ObjectDownloads> arrR;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tD = (TabDetailImage) adapter.getItem(positionFragment);
        oD = tD.oD;
        tD.bmp = myHandler.getBitMap(oD.wallpaperName + original);
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_set_wallpaper:
                Intent itDownloadToSetWallpaper = new Intent(getApplicationContext(), SelectDownLoadActivity.class);
                itDownloadToSetWallpaper.putExtra("LIST_LINK", tD.arrD);
                itDownloadToSetWallpaper.putExtra("PROGRESS", 2);
                itDownloadToSetWallpaper.putExtra("WALLPAPER_NAME", oD.wallpaperName);
                startActivity(itDownloadToSetWallpaper);
//                Done!
                break;
            case R.id.action_set_contact:
                if (tD.bmp == null)
                    myHandler.downloadBitmap(oD.linkDownload,
                            oD.wallpaperName + original,
                            myHandler.DOWNLOAD_AND_SET_CONTACT);
                else
                    startActivityForResult(new Intent(Intent.ACTION_PICK,
                                    ContactsContract.Contacts.CONTENT_URI),
                            PICK_CONTACT);
//                Done!
                break;
            case R.id.action_share_image:
//                Share
                break;
            case R.id.action_download_image:
                if (tD.arrD != null) {
                    Intent itDownload = new Intent(getApplicationContext(), SelectDownLoadActivity.class);
                    itDownload.putExtra("LIST_LINK", tD.arrD);
                    itDownload.putExtra("PROGRESS", 0);
                    itDownload.putExtra("WALLPAPER_NAME", oD.wallpaperName);
                    startActivity(itDownload);
                } else
                    myHandler.downloadBitmap(oD.linkDownload, oD.wallpaperName + "-Original", myHandler.ONLY_DOWNLOAD);
//              Done!
                break;
            case R.id.action_add_favorite:
                if (tD.cbFavorite.isChecked()) {
                    itFav.setTitle(parseBooleanFavToTitle(false));
                    tD.cbFavorite.setChecked(false);
                    arrI.get(positionPage).favorite = false;
                } else {
                    itFav.setTitle(parseBooleanFavToTitle(true));
                    tD.cbFavorite.setChecked(true);
                    arrI.get(positionPage).favorite = true;
                }
//                Done!
                break;
            case R.id.action_report_image:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String parseBooleanFavToTitle(boolean isChecked) {
        if (isChecked)
            return "Remove from favorite";
        else
            return "Add to favorite";
    }


    public boolean setDisplayPhotoByRawContactId(long rawContactId, Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        Uri pictureUri = Uri.withAppendedPath(ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                rawContactId), ContactsContract.RawContacts.DisplayPhoto.CONTENT_DIRECTORY);
        try {
            AssetFileDescriptor afd = getContentResolver().openAssetFileDescriptor(pictureUri, "rw");
            OutputStream os = afd.createOutputStream();
            os.write(byteArray);
            os.close();
            afd.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    if (setDisplayPhotoByRawContactId(
                            ContentUris.parseId(data.getData().buildUpon().build())
                            , myHandler.getBitMap(oD.wallpaperName + original)))
                        myHandler.myToast("Set contact success!");
                    else
                        myHandler.myToast("Set contact fail!");
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (from.equals(GetPage.LATEST_WALLPAPER)) {
            setFavForPageOnFinish(TabLatestWallpapers.arrI, arrI);
            TabLatestWallpapers.pageAdapter.notifyDataSetChanged();
        } else if (from.equals(GetPage.TOP_MOST_VIEWED)) {
            setFavForPageOnFinish(TabTopMostViewed.arrI, arrI);
            TabTopMostViewed.pageAdapter.notifyDataSetChanged();
        } else if (from.equals(GetPage.CATEGORIRES)) {
            setFavForPageOnFinish(DetailCategoriesActivity.arrI, arrI);
            DetailCategoriesActivity.pageAdapter.notifyDataSetChanged();
        } else if (from.equals(GetPage.FAVORITE)) {
            for (int i = 0; i < arrI.size(); i++)
                if (!db.checkFavorite(arrI.get(i).linkDetail))
                    FavoriteActivity.arrI.remove(i);
            FavoriteActivity.pageAdapter.notifyDataSetChanged();
        }
    }

    public void setFavForPageOnFinish(ArrayList<ObjectImage> arr1, ArrayList<ObjectImage> arr2) {
        for (int i = 0; i < arr2.size(); i++)
            arr1.get(i).favorite = db.checkFavorite(arr2.get(i).linkDetail);
    }
}