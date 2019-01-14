package com.example.root.mobilefinal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ManageShopPageAdapter extends FragmentStatePagerAdapter {
    public ManageShopPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
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
