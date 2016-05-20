package com.soft.kent.bebluewallpaper.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.soft.kent.bebluewallpaper.DetailCategoriesActivity;
import com.soft.kent.bebluewallpaper.Listener.RecyclerItemClickListener;
import com.soft.kent.bebluewallpaper.R;
import com.soft.kent.bebluewallpaper.adapter.CategoriesAdapter;
import com.soft.kent.bebluewallpaper.model.ObjectCategories;
import com.soft.kent.bebluewallpaper.model.ChuDeDatabase;

import java.util.List;

/**
 * Created by kentd on 18/05/2016.
 */
public class TabCategories extends Fragment {
    RecyclerView rcCategories;
    List<ObjectCategories> listObjectCategories;
    ChuDeDatabase db;
    CategoriesAdapter categoriesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_categories, container, false);
        init(v);
        rcCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        categoriesAdapter = new CategoriesAdapter(rcCategories, listObjectCategories);
        rcCategories.setAdapter(categoriesAdapter);

        rcCategories.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(), listObjectCategories.get(position).getLinkChuDe()+"", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), DetailCategoriesActivity.class);
                        intent.putExtra("link", listObjectCategories.get(position).getLinkChuDe());
                        startActivity(intent);
                    }
                })
        );


        return v;
    }

    private void init(View v) {
        rcCategories = (RecyclerView) v.findViewById(R.id.rcCategories);
        db = new ChuDeDatabase(getContext());
        listObjectCategories = db.queryAllChuDe();
    }
}
