package com.example.root.mobilefinal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ShopDetail.class);
                intent.putExtra("sid", shopList.get(position).sid);
                context.startActivity(intent);
            }
        });
        ((ShopHolder)holder).bind(shopList.get(position));

        ((ShopHolder)holder).button_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, ((ShopHolder) holder).button_menu);
                //inflating menu from xml resource
                popup.inflate(R.menu.option_menu_myshop);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.myshop_edit:
                                Intent intent = new Intent(context, ManageShopActivity.class);
                                intent.putExtra("sid", shopList.get(position).sid);
                                context.startActivity(intent);
                                //handle edit shop
                                break;
                            case R.id.myshop_delete:
                                Backend.deleteShop(shopList.get(position).sid, new Backend.Callback<Boolean>() {
                                    @Override
                                    public void call(Boolean data) {
                                        shopList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                    }
                                });
                                //handle delete shop
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }
}
