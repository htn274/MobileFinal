package com.example.root.mobilefinal;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TopShopHolder extends RecyclerView.ViewHolder {
    ImageView imageView_avatar;
    TextView textView_name;
    ImageView btn_like;
    public TopShopHolder(View view) {
        super(view);
        imageView_avatar = view.findViewById(R.id.imageView_avatarShop);
        textView_name = view.findViewById(R.id.textView_shopName);btn_like = view.findViewById(R.id.btn_like);

    }

    public void bind(Shop shop) {
        Backend.downloadAvatar("avatar/shop/" + shop.sid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_avatar.setImageBitmap(data);
            }
        });
        textView_name.setText(shop.name.toString());
    }
}
