package com.example.manop.mashop.Adapter;

/**
 * Created by Manop on 11/3/2018.
 */

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.manop.mashop.Fragments.ShopFragment;
import com.example.manop.mashop.Fragments.MyProductFragment;
import com.example.manop.mashop.Fragments.TabFragment3;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ShopFragment tab1 = new ShopFragment();
                return tab1;
            case 1:
                MyProductFragment tab2 = new MyProductFragment();
                return tab2;
            case 2:
                TabFragment3 tab3 = new TabFragment3();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}