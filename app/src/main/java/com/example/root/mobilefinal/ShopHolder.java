package com.example.root.mobilefinal;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopHolder extends RecyclerView.ViewHolder {
    ImageView imageView_avatar;
    TextView textView_name, textView_address, textView_workTime;

    public ShopHolder(View view) {
        super(view);
        imageView_avatar = view.findViewById(R.id.imageView_avatar);
        textView_name = view.findViewById(R.id.textView_name);
        textView_address = view.findViewById(R.id.textView_address);
        textView_workTime = view.findViewById(R.id.textView_workTime);
    }

    public void bind(Shop shop) {
        Backend.downloadAvatar("avatar/shop/" + shop.sid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_avatar.setImageBitmap(data);
            }
        });
        textView_name.setText(shop.name);
        textView_address.setText(shop.address);
        textView_workTime.setText(String.format("open: %s - close: %s", shop.open_hour, shop.close_hour));
    }
}
