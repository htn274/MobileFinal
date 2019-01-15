package com.example.root.mobilefinal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class TopShopAdapter extends RecyclerView.Adapter {
    List<Shop> itemList;
    Context context;

    public TopShopAdapter(Context context, List<Shop> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_topshops, parent, false);
        return new TopShopHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, ItemDetail.class);
//                intent.putExtra("iid", itemList.get(position).iid);
//                context.startActivity(intent);
            }
        });
        ((TopShopHolder)holder).bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
