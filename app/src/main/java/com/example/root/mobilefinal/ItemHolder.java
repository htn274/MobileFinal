package com.example.root.mobilefinal;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemHolder extends RecyclerView.ViewHolder {
    ImageView imageView_avatar;
    TextView textView_name, textView_category, textView_price, textView_variation;
    ImageView button_menu;
    public ItemHolder(View view) {
        super(view);
        imageView_avatar = view.findViewById(R.id.imageView_avatar);

        textView_name = view.findViewById(R.id.textView_name);
        textView_category = view.findViewById(R.id.textView_category);
        textView_price = view.findViewById(R.id.textView_price);
        textView_variation = view.findViewById(R.id.textView_variation);

        button_menu = view.findViewById(R.id.btn_menu);
    }

    public void bind(Item item) {
        Backend.downloadAvatar("avatar/item/" + item.iid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_avatar.setImageBitmap(data);
            }
        });
        textView_name.setText(item.name);
        textView_category.setText(item.category);
        textView_price.setText(item.price.toString());
        textView_variation.setText(String.format("color: %s - size: %s", item.variation.get("color"), item.variation.get("size")));
    }
}
