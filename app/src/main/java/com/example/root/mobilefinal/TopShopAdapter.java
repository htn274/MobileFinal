package com.example.root.mobilefinal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TopShopAdapter extends RecyclerView.Adapter {
    List<Shop> shopList;
    Context context;

    public TopShopAdapter(Context context, List<Shop> itemList) {
        this.context = context;
        this.shopList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_topshops, parent, false);
        return new TopShopHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((TopShopHolder)holder).btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        ((TopShopHolder)holder).textView_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetail.class);
                intent.putExtra("sid", shopList.get(position).sid);
                context.startActivity(intent);
            }
        });

        ((TopShopHolder)holder).bind(shopList.get(position));
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}
