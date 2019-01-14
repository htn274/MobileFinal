package com.example.root.mobilefinal;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyShopFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton btn_add;
    FragmentActivity myContext;
    RecyclerView rv_myShops;
    public MyShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_shop, container, false);
        btn_add = view.findViewById(R.id.btn_addShop);
        rv_myShops = view.findViewById(R.id.rv_myShops);

        btn_add.setOnClickListener(this);
        updateShops();

        return view;
    }

    private void updateShops() {
        Backend.getMyShops(new Backend.Callback<List<Shop>>() {
            @Override
            public void call(List<Shop> data) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_myShops.setLayoutManager(linearLayoutManager);
                rv_myShops.setAdapter(new ShopAdapter(getContext(), data));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_add) {
            Intent intent = new Intent(myContext.getApplicationContext(), AddShopForm.class);
            startActivity(intent);
        }
    }
}
