package com.example.root.mobilefinal;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopListItemsFragment extends Fragment {
    RecyclerView rv_shopItems;
    String sid;
    public ShopListItemsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        sid = getArguments().getString("sid");
        Log.d("btag", "ShopListItemFragment onCreateView " + sid);
        View v = inflater.inflate(R.layout.fragment_shop_list_items, container, false);
        setUI(v);
        return v;
    }

    private void setUI(View v) {
        rv_shopItems = v.findViewById(R.id.rv_shopItems);

        Backend.getShopItems(sid, new Backend.Callback<List<Item>>() {
            @Override
            public void call(List<Item> data) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_shopItems.setLayoutManager(linearLayoutManager);
                rv_shopItems.setAdapter(new ItemAdapter(getContext(), data, false));
            }
        });

    }

}
