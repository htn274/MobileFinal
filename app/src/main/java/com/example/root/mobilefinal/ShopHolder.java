package com.example.root.mobilefinal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopHolder extends RecyclerView.ViewHolder {
    ImageView imageView_avatar;
    TextView textView_name, textView_address, textView_openHour, textView_closeHour;

    public ShopHolder(View view) {
        super(view);
        imageView_avatar = view.findViewById(R.id.imageView_avatar);
        textView_name = view.findViewById(R.id.textView_name);
        textView_address = view.findViewById(R.id.textView_address);
        textView_openHour = view.findViewById(R.id.textView_openHour);
        textView_closeHour = view.findViewById(R.id.textView_closeHour);
    }

    public void bind(Shop shop) {
        imageView_avatar.setImageBitmap(shop.avatar);
        textView_name.setText(shop.name);
        textView_address.setText(shop.address);
        textView_openHour.setText(shop.openHour.toString());
        textView_closeHour.setText(shop.closeHour.toString());
    }
}
