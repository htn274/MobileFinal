package com.example.root.mobilefinal;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ManageShopActivity extends AppCompatActivity {

    private ViewPager pager;
    private TabLayout tabLayout;
    Shop thisShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_shop);
        addControl();
    }

    private void addControl() {
        Intent intent = getIntent();
        Backend.getShop(intent.getStringExtra("sid"), new Backend.Callback<Shop>() {
            @Override
            public void call(Shop data) {

            }
        });
        pager = (ViewPager) this.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        FragmentManager manager = this.getSupportFragmentManager();
        ManageShopPageAdapter adapter = new ManageShopPageAdapter(manager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));
    }
}
