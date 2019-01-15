package com.example.root.mobilefinal;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment implements View.OnClickListener{

    RecyclerView rv_topItems, rv_topShop;
    EditText searchBar;
    public Home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View v){
        rv_topItems = v.findViewById(R.id.rv_topItems);
        setTopItems();

        rv_topShop = v.findViewById(R.id.rv_topshops);
        setTopShops();

        searchBar = v.findViewById(R.id.editText_searchBar);
        searchBar.setOnClickListener(this);
    }

    private void setTopItems(){
        Backend.getAllItems(new Backend.Callback<List<Item>>() {
            @Override
            public void call(List<Item> data) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_topItems.setLayoutManager(linearLayoutManager);
                rv_topItems.setAdapter(new TopItemAdapter(getContext(), data));
            }
        });
    }

    private void setTopShops(){

        Backend.getAllShops(new Backend.Callback<List<Shop>>() {
            @Override
            public void call(List<Shop> data) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_topShop.setLayoutManager(linearLayoutManager);
                rv_topShop.setAdapter(new TopShopAdapter(getContext(), data));
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == searchBar){
            startActivity(new Intent(getActivity(), SearchActivity.class));
        }
    }
}
