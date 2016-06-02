package com.soft.kent.bebluewallpaper;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.soft.kent.bebluewallpaper.adapter.AdapterViewPager;
import com.soft.kent.bebluewallpaper.model.GetPage;
import com.soft.kent.bebluewallpaper.model.ObjectDetailImage;
import com.soft.kent.bebluewallpaper.model.ObjectImage;
import com.soft.kent.bebluewallpaper.tabs.TabDetailImage;
import com.soft.kent.bebluewallpaper.tabs.TabLatestWallpapers;
import com.soft.kent.bebluewallpaper.tabs.TabTopMostViewed;
import com.soft.kent.bebluewallpaper.model.Entity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DetailImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static String link = "http://www.hdwallpapers.in/latest_wallpapers/page/";

    int positionPage = 0;
    int page = 1;
    Bundle bundle;
    String from;

    AdapterViewPager adapter;
    ViewPager viewpager;
    Toolbar toolbar;
    int positionFragment;

    private ArrayList<ObjectImage> arrI;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_image);
        init(getIntent());
    }


    public void init(Intent it) {
        arrI = new ArrayList<>();
        if (it != null) {
            toolbar = (Toolbar) findViewById(R.id.toolbarAllImage);
            setSupportActionBar(toolbar);
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
            toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
            toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));
            viewpager = (ViewPager) findViewById(R.id.vPAllImage);
            adapter = new AdapterViewPager(getSupportFragmentManager());
            bundle = it.getBundleExtra("data");
            link = bundle.getString("linkPage");
            positionPage = bundle.getInt("position");
            page = Integer.valueOf(link.substring(link.length() - 1));
            from = bundle.getString(Entity.KEY_DETAIL);
            if (from.equals(Entity.LATEST_WALLPAPER)) {
                arrI = TabLatestWallpapers.arrI;
            } else if (from.equals(Entity.TOP_MOST_VIEWED)) {
                arrI = TabTopMostViewed.arrI;
            } else if (from.equals(Entity.CATEGORIRES)) {
                arrI = DetailCategoriesActivity.arrI;
            }
            for (int i = 0; i < arrI.size(); i++) {
                adapter.addTab(new TabDetailImage(arrI.get(i).getLinkDetail(), arrI.size()), "");
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
        if (arrI != null && position == (arrI.size() - 1)) {
            page++;
            link = link.replace(link.substring(link.length() - 1), String.valueOf(page));
            new AsyncGetAllCategory().execute(link);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class AsyncGetAllCategory extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            GetPage.getAllWallpaperForDetailActivity(params[0], arrI, adapter);
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
        return true;
    }

    ObjectDetailImage oD;
    static final int PICK_CONTACT = 1;
    TabDetailImage tD;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tD = (TabDetailImage) adapter.getItem(positionFragment);
        oD = tD.oD;
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_set_wallpaper:
                if (tD.getPatch() == null) {
                    tD.getContext().registerReceiver(tD.onComplete
                            , new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    tD.downloadFile(oD.linkDownload);
                } else {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, tD.getFile(oD.wallpaperName));
                    shareIntent.setType("image/jpeg");
                    startActivity(Intent.createChooser(shareIntent, "Chia sẻ ảnh"));
                }

                //Set wallpaper
                break;
            case R.id.action_set_contact:
                if (tD.getPatch() == null)
                    tD.downloadFile(oD.linkDownload);
                else
                    startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT);
                break;
            case R.id.action_share_image:
                MyLog.e(tD.getFile(oD.wallpaperName).toString());

                break;
            case R.id.action_download_image:
                if (tD.getPatch() == null)
                    tD.downloadFile(oD.linkDownload);
                else
                    myToast("Download rồi mà!");
                break;
            case R.id.action_add_favorite:

                break;
            case R.id.action_report_image:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void myToast(String msg) {
        Toast.makeText(DetailImageActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_CONTACT:
                if (resultCode == Activity.RESULT_OK) {
                    if (setDisplayPhotoByRawContactId(
                            ContentUris.parseId(data.getData().buildUpon().build())
                            , tD.getPatch()))
                        myToast("thành công!");
                    else
                        myToast("thất bại");
                }
                break;
        }
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

    //    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        link = link.replace("/page/" + (page), "/page/" + (page + 1));
//        page = Integer.parseInt(link.substring(link.length() - 1));
//        if (from.equals(Entity.LATEST_WALLPAPER)) {
//            TabLatestWallpapers.arrI = arrI;
//            TabLatestWallpapers.link = link;
//        } else if (from.equals(Entity.TOP_MOST_VIEWED)) {
//            TabTopMostViewed.arrI = arrI;
//            TabTopMostViewed.index = page;
//        } else if (from.equals(Entity.CATEGORIRES)) {
//            DetailCategoriesActivity.arrI = arrI;
//            DetailCategoriesActivity.index = page;
//        }
//        MyLog.e("DESTROYED");
//    }
}
