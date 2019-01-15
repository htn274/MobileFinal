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

public class ItemAdapter extends RecyclerView.Adapter {
    List<Item> itemList;
    Context context;
    Boolean edit = false;

    public ItemAdapter(Context context, List<Item> itemList, boolean edit) {
        this.context = context;
        this.itemList = itemList;
        this.edit = edit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemDetail.class);
                intent.putExtra("iid", itemList.get(position).iid);
                context.startActivity(intent);
            }
        });
        final ItemHolder itemHolder = (ItemHolder) holder;
        itemHolder.bind(itemList.get(position));

        if (!edit) {
            itemHolder.button_menu.setVisibility(View.GONE);
        }
        else {
            itemHolder.button_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
    //                creating a popup menu
                    PopupMenu popup = new PopupMenu(context, itemHolder.button_menu);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.option_menu_myitem);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_edit:
                                    Intent intent = new Intent(context, AddItem.class);
                                    intent.putExtra("iid", itemList.get(position).iid);
                                    context.startActivity(intent);
                                    //handle edit shop
                                    break;
                                case R.id.item_delete:
                                    Backend.deleteItem(itemList.get(position).iid, new Backend.Callback<Boolean>() {
                                        @Override
                                        public void call(Boolean data) {
                                            itemList.remove(position);
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
                }});
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
