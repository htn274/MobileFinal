package com.example.root.mobilefinal;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TopItemHolder extends RecyclerView.ViewHolder {
    ImageView imageView_avatar;
    TextView textView_name, textView_price;
    public TopItemHolder(View view) {
        super(view);
        imageView_avatar = view.findViewById(R.id.imageView_item);
        textView_name = view.findViewById(R.id.textView_itemName);
        textView_price = view.findViewById(R.id.textView_price);

    }

    public void bind(Item item) {
        Backend.downloadAvatar("avatar/item/" + item.iid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_avatar.setImageBitmap(data);
            }
        });
        textView_name.setText(item.name);
        textView_price.setText(item.price.toString() + " đ");
    }
}
