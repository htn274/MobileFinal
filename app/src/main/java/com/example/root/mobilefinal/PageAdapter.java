package com.example.root.mobilefinal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class PageAdapter extends FragmentStatePagerAdapter {

    PageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment frag=null;
        switch (position){
            case 0:
                frag = new AccountFragment();
                break;
            case 1:
                frag = new MyShopFragment();
                break;
            case 2:
                frag = new PurchaseFragment();
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
        switch (position){
            case 0:
                title = "Account";
                break;
            case 1:
                title = "My Shop";
                break;
            case 2:
                title = "Buying";
                break;
        }
        return title;
    }
}