package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class ManageShopActivity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tabLayout;
    Shop thisShop;
    ImageView imageView_shopImage;
    TextView tv_shopName, tv_address, tv_activeHour;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shop);
        addControl();
        setUI();
    }

    private void addControl() {
        pager = (ViewPager) this.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        FragmentManager manager = this.getSupportFragmentManager();
        ManageShopPageAdapter adapter = new ManageShopPageAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

        imageView_shopImage = findViewById(R.id.imageView_shopImage);
        tv_shopName = findViewById(R.id.tv_shopName);
        tv_address = findViewById(R.id.textView_address);
        tv_activeHour = findViewById(R.id.textView_activeHour);
    }

    void setUI() {
        final Intent intent = getIntent();
        Backend.getShop(intent.getStringExtra("sid"), new Backend.Callback<Shop>() {
            @Override
            public void call(Shop data) {
                if (data == null) {
                    Log.d("btag", "get shop error " + intent.getStringExtra("sid"));
                }
                else {
                    updateShop(data);

                }
            }
        });
    }

    private void updateShop(Shop data) {
        thisShop = data;
        tv_shopName.setText(data.name);
        tv_address.setText(data.address);
        tv_activeHour.setText(String.format("%s - %s", data.open_hour, data.close_hour));
        Backend.downloadAvatar("avatar/shop/" + data.sid + ".jpg", new Backend.Callback<Bitmap>() {
            @Override
            public void call(Bitmap data) {
                imageView_shopImage.setImageBitmap(data);
            }
        });
    }
}
