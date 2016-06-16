package com.soft.kent.bebluewallpaper.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.soft.kent.bebluewallpaper.DetailCategoriesActivity;
import com.soft.kent.bebluewallpaper.controller.listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.view.adapter.CategoriesAdapter;
import com.soft.kent.bebluewallpaper.model.ObjectCategories;
import com.soft.kent.bebluewallpaper.model.DatabaseWallpaper;

import java.util.List;

/**
 * Created by kentd on 18/05/2016.
 */
public class TabCategories extends Fragment {
    RecyclerView rcCategories;
    List<ObjectCategories> listObjectCategories;
    DatabaseWallpaper db;
    CategoriesAdapter categoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_categories, container, false);
        init(v);

        return v;
    }

    private void init(View v) {
        rcCategories = (RecyclerView) v.findViewById(R.id.rcCategories);
        db = new DatabaseWallpaper(getContext());
        listObjectCategories = db.queryAllChuDe();
        rcCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesAdapter = new CategoriesAdapter(rcCategories, listObjectCategories);
        rcCategories.setAdapter(categoriesAdapter);
        rcCategories.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(getActivity(), DetailCategoriesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("linkCategories", listObjectCategories.get(position).linkChuDe);
                        bundle.putString("titleCategories", listObjectCategories.get(position).tenChuDe);
                        bundle.putInt("imgAvatar", listObjectCategories.get(position).anhDaiDien);
                        intent.putExtra("data", bundle);
                        startActivity(intent);
                    }
                })
        );
    }
}
