package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TabFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    final private String TAG = "MEMORY-ACC";

    private CharSequence mTitles[]; // This will Store the mTitles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int mNumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private ArrayList<String> mTitleArray;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> titles, int mNumbOfTabsumb) {
        super(fm);

        this.mTitleArray = titles;
        this.mTitles = mTitleArray.toArray(new CharSequence[mTitleArray.size()]);
        this.mNumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        //TODO:Add Support for Recycled Tabs

        TabFragment tabFragment = new TabFragment();

        Bundle args = new Bundle();
        args.putInt("GROUP", position);
        tabFragment.setArguments(args);

        return tabFragment;
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return mNumbOfTabs;
    }
}