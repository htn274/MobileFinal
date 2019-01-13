package com.example.root.mobilefinal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ShopDetailPageAdapter extends FragmentStatePagerAdapter {

    public ShopDetailPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new ShopListItemsFragment();
                break;
            case 1:
                frag = new ShopCategoriesFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title = "List Items";
                break;
            case 1:
                title = "Category";
                break;
        }
        return title;
    }
}
