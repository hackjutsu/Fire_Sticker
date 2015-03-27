package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.GridTabFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.ListTabFragment;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    final private String TAG = "MEMORY-ACC";

    private CharSequence mTitles[]; // This will Store the mTitles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    private int mNumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private ArrayList<String> mTitleArray;
    private Context mContext;

    public ViewPagerAdapter(Context context ,FragmentManager fm, ArrayList<String> titles, int mNumbOfTabsumb) {
        super(fm);

        this.mTitleArray = titles;
        this.mTitles = mTitleArray.toArray(new CharSequence[mTitleArray.size()]);
        this.mNumbOfTabs = mNumbOfTabsumb;
        this.mContext = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        //TODO:Add Support for Recycled Tabs

        Bundle args = new Bundle();
        args.putInt("GROUP", position);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        int ViewMode = preferences.getInt("VIEWMODE", 1);
        if (ViewMode == 1) {

            GridTabFragment gridTabFragment = new GridTabFragment();
            gridTabFragment.setArguments(args);
            return gridTabFragment;
        } else if (ViewMode == 2) {

            ListTabFragment listTabFragment = new ListTabFragment();
            listTabFragment.setArguments(args);
            return listTabFragment;
        }

        return null;
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