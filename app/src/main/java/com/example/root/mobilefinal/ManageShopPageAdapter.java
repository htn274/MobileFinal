package com.example.root.mobilefinal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ManageShopPageAdapter extends FragmentStatePagerAdapter {
    String sid;
    public ManageShopPageAdapter(FragmentManager fm, String sid) {
        super(fm);
        this.sid = sid;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        Bundle arg = new Bundle();
        arg.putString("sid", sid);
        Log.e("btag", "Fragment getItem sid = " + sid);
        switch (position) {
            case 0:
                frag = new ManageShopItemFragment();
                break;
            case 1:
                frag = new ManageOdersShopFragment();
                break;
            case 2:
                frag = new StatisticShopFragment();
                break;
        }
        frag.setArguments(arg);
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "List Items";
                break;
            case 1:
                title = "Orders";
                break;
            case 2:
                title = "Statistic";
                break;
        }
        return title;
    }
}
