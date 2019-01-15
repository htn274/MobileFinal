package com.example.root.mobilefinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MyCart extends AppCompatActivity {

    RecyclerView rv_cartItem;
    TextView textView_totalPrice;
    Button btn_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        setCartItem();
    }

    void initViews(){
        textView_totalPrice = findViewById(R.id.textView_totalPrice);
    }

    private int getTotalPrice(List<CartItem> cartItemList){
        return 0;
    }

    private void setCartItem() {
        Backend.getCartItems(new Backend.Callback<List<CartItem>>() {
            @Override
            public void call(List<CartItem> data) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_cartItem.setLayoutManager(linearLayoutManager);
                rv_cartItem.setAdapter(new CardItemAdapter(getApplicationContext(), data));

            }
        });
    }

}
