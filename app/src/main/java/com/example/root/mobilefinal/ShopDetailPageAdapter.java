package com.example.root.mobilefinal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ShopDetailPageAdapter extends FragmentStatePagerAdapter {

    String sid;
    public ShopDetailPageAdapter(FragmentManager fm, String sid) {
        super(fm);
        this.sid = sid;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        Bundle args = new Bundle();
        args.putString("sid", sid);
        Log.d("btag", "ShopDetailPageAdapter getItem " + position + " sid " + sid);
        switch (position){
            case 0:
                frag = new ShopListItemsFragment();
                break;
            case 1:
                frag = new ShopCategoriesFragment();
                break;
        }
        frag.setArguments(args);
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
