package com.example.root.mobilefinal;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import java.util.Map;

public class CardItemHolder extends RecyclerView.ViewHolder {
    ImageView imageView_avatar;
    TextView textView_name, textView_price, textView_shopName;
    ElegantNumberButton quantityNumber;
    Item item;

    public CardItemHolder(View view) {
        super(view);
        imageView_avatar = view.findViewById(R.id.imageView_itemImage);
        textView_name = view.findViewById(R.id.textView_itemName);
        textView_price = view.findViewById(R.id.textView_price);
        textView_shopName = view.findViewById(R.id.textView_shopName);
        quantityNumber = view.findViewById(R.id.quantityNumber);
    }

    public void bind(final CartItem cartItem) {
        Backend.getItem(cartItem.iid, new Backend.Callback<Item>() {
            @Override
            public void call(Item data) {
                item = data;
                textView_name.setText(item.name);
                textView_price.setText(item.price.toString() + " đ");
                quantityNumber.setRange(0, Integer.valueOf(item.quantity));
                quantityNumber.setNumber(cartItem.quantity);
            }
        });

        Backend.downloadAvatar("avatar/item/" + cartItem.iid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_avatar.setImageBitmap(data);
            }
        });

    }
}
