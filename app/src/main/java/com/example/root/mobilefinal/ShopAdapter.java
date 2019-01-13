package com.example.root.mobilefinal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter {
    List<Shop> shopList;
    Context context;

    public ShopAdapter(Context context, List<Shop> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ShopHolder)holder).bind(shopList.get(position));
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}
