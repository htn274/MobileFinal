package com.example.root.mobilefinal;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

public class CardItemAdapter extends RecyclerView.Adapter {
    List<CartItem> cartItemList;
    Context context;

    public CardItemAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listview_mycart, parent, false);
        return new CardItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
//        ((TopItemHolder)holder).imageView_avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, ItemDetail.class);
//                intent.putExtra("iid", itemList.get(position).iid);
//                context.startActivity(intent);
//            }
//        });
        ((CardItemHolder)holder).bind(cartItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }
}
