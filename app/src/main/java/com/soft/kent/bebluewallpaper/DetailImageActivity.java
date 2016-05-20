package com.soft.kent.bebluewallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailImageActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ImageView ivDetail;
    FloatingActionButton fabFavorite;
    TextView tvTitleDetailImage;
    TextView tvCountViewDetailImage;
    Button btnDownloadImage;
    Button btnSetWallpaper;
    TextView tvAuthorName;
    RecyclerView rcRelatesDetailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_image);

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.mipmap.ic_backarrow);
        toolbar.setOverflowIcon(getResources().getDrawable(R.mipmap.ic_menu2));
        ivDetail = (ImageView) findViewById(R.id.ivDetail);
        fabFavorite = (FloatingActionButton) findViewById(R.id.fabFavorite);
        tvTitleDetailImage = (TextView) findViewById(R.id.tvTitleDetailImage);
        tvCountViewDetailImage = (TextView) findViewById(R.id.tvCountViewDetailImage);
        btnDownloadImage = (Button) findViewById(R.id.btnDownloadImage);
        btnSetWallpaper = (Button) findViewById(R.id.btnSetWallpaper);
        tvAuthorName = (TextView) findViewById(R.id.tvAuthorName);
        rcRelatesDetailImage = (RecyclerView) findViewById(R.id.rcRelatesDetailImage);

        fabFavorite.setOnClickListener(this);
        btnDownloadImage.setOnClickListener(this);
        btnSetWallpaper.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabFavorite:
                thongBao("Replace with your own action", v);
                break;
            case R.id.btnDownloadImage:
                thongBao("Downloading..", v);
                break;
            case R.id.btnSetWallpaper:
                thongBao("Set as wallpaper..", v);
                break;
        }
    }

    public void thongBao(String msg, View v) {
        Snackbar.make(  v, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(DetailImageActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.action_set_wallpaper:
//                Đặt làm hình nền
                break;
            case R.id.action_set_contact:
//                Đặt làm liên hệ
                break;
            case R.id.action_share_image:
//                Chia sẻ
                break;
            case R.id.action_download_image:
//                Tải về
                break;
            case R.id.action_add_favorite:
//                Thêm vào mục yêu thích
                break;
            case R.id.action_report_image:
//                Báo cáo
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
