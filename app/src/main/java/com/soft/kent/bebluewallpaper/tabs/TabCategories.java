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
import com.soft.kent.bebluewallpaper.adapter.ChuDeAdapter;
import com.soft.kent.bebluewallpaper.model.ChuDe;
import com.soft.kent.bebluewallpaper.model.ChuDeDatabase;

import java.util.List;

/**
 * Created by kentd on 18/05/2016.
 */
public class TabCategories extends Fragment {
    RecyclerView rcCategories;
    List<ChuDe> listChuDe;
    ChuDeDatabase db;
    ChuDeAdapter chuDeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_categories, container, false);
        init(v);
        rcCategories.setLayoutManager(new LinearLayoutManager(getContext()));
        chuDeAdapter = new ChuDeAdapter(rcCategories, listChuDe);
        rcCategories.setAdapter(chuDeAdapter);

        rcCategories.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Toast.makeText(getContext(),listChuDe.get(position).getLinkChuDe()+"", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getActivity(), DetailCategoriesActivity.class);
                        intent.putExtra("link", listChuDe.get(position).getLinkChuDe());
                        startActivity(intent);
                    }
                })
        );


        return v;
    }

    private void init(View v) {
        rcCategories = (RecyclerView) v.findViewById(R.id.rcCategories);
        db = new ChuDeDatabase(getContext());
        listChuDe = db.queryAllChuDe();
    }
}
