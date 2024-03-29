package com.example.root.mobilefinal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.Semaphore;

public class MyCart extends AppCompatActivity {

    RecyclerView rv_cartItem;
    TextView textView_totalPrice;
    Button btn_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        initViews();
        setCartItem();
    }

    void initViews(){
        textView_totalPrice = findViewById(R.id.textView_totalPrice);
        rv_cartItem = findViewById(R.id.rv_cartItem);
    }

    private void getTotalPrice(List<CartItem> cartItemList, final Backend.Callback<Long> cb){
        final Long[] sum = {0l};
        final Integer[] cnt = { cartItemList.size() };
        final Semaphore mut = new Semaphore(1);
        for (final CartItem item: cartItemList)
            Backend.getItem(item.iid, new Backend.Callback<Item>() {
                @Override
                public void call(Item data) {
                    try {
                        mut.acquire();
                        cnt[0]--;
                        sum[0] += Long.valueOf(data.price) * Long.valueOf(item.quantity);
                        if (cnt[0] == 0) {
                            cb.call(sum[0]);
                        }
                        mut.release();
                    }
                    catch (InterruptedException ex) {

                    }
                }
            });
    }

    private void setCartItem() {
        Backend.getCart(new Backend.Callback<List<CartItem>>() {
            @Override
            public void call(final List<CartItem> data) {

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_cartItem.setLayoutManager(linearLayoutManager);

                Backend.Callback<Integer> onCartItemUpdated = new Backend.Callback<Integer>() {
                    @Override
                    public void call(Integer pos) {
                        getTotalPrice(data, new Backend.Callback<Long>() {
                            @Override
                            public void call(Long data)  {
                                textView_totalPrice.setText(data.toString() + " đ");
                            }
                        });
                    }
                };

                rv_cartItem.setAdapter(new CardItemAdapter(getApplicationContext(), data, onCartItemUpdated));
            }
        });
    }

}
