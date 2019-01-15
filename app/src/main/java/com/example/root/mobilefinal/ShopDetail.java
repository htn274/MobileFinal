package com.example.root.mobilefinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopDetail extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tabLayout;
    ShopDetailPageAdapter adapter;
    Shop thisShop;
    ImageView imageView_shopImage;
    ImageView btn_getDirection, btn_like;
    TextView tv_shopName, tv_address, tv_activeHour;
    String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            sid = savedInstanceState.getString("sid");
            Log.e("btag", "ShopDetail bundle != null " + sid);
        }
        else {
            final Intent intent = getIntent();
            sid = intent.getStringExtra("sid");
            Log.e("btag", "ShopDetail bundle == null " + sid);
        }
        setContentView(R.layout.activity_shop_detail);
        addControl();
        setUI();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sid", sid);
    }

    private void addControl() {
        imageView_shopImage = findViewById(R.id.shop_image);
        tv_shopName = findViewById(R.id.tv_shopName);
        tv_address = findViewById(R.id.textView_address);
        tv_activeHour = findViewById(R.id.textView_activeHour);

        btn_getDirection = findViewById(R.id.btn_getDirection);
        btn_getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String geo = String.valueOf(thisShop.loc.get("lat")) + "," + String.valueOf(thisShop.loc.get("lng"));
                Log.i("Geography", geo);
                Uri gmmIntentUri = Uri.parse("geo:" + geo + "?q=" + geo);
                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        });
        btn_like = findViewById(R.id.btn_like);
        btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });
    }

    void setUI() {
        Log.e("btag", "njnasfkjsagfkjsfgkjsdfgkjdfgkndgfnjegknjdgfsfnjfdg");
        pager = (ViewPager) this.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        FragmentManager manager = this.getSupportFragmentManager();
        adapter = new ShopDetailPageAdapter(manager, sid);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

        Backend.getShop(sid, new Backend.Callback<Shop>() {
            @Override
            public void call(Shop data) {
                if (data == null) {
                    Log.d("btag", "get shop error " + sid);
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
