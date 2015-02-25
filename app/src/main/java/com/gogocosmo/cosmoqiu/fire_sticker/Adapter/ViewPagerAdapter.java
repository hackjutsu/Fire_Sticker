package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TabFragment;

import java.util.ArrayList;

/**
 * Created by cosmoqiu on 2/10/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    final private String TAG = "MEMORY-ACC";

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    ArrayList<String> _titles;
    private ArrayList<TabFragment> _tabList;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> titles, int mNumbOfTabsumb) {
        super(fm);

        this._titles = titles;
        this.Titles = _titles.toArray(new CharSequence[_titles.size()]);
        this.NumbOfTabs = mNumbOfTabsumb;
        _tabList = new ArrayList<>();
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        //TODO:Add Support for Recycled Tabs

        TabFragment tabFragment = null;
//
//        if (position < 0 || position > _tabList.size()) {
//
//            Log.d(TAG, "Invalid Tab ID!" + String.valueOf(position));
//        } else if (position == _tabList.size()) {
//
//            tabFragment = new TabFragment();
//            tabFragment.setGrouId(position);
//            _tabList.add(tabFragment);
//        } else {
//
//            tabFragment = _tabList.get(position);
//        }

        tabFragment = new TabFragment();

        Bundle args = new Bundle();
        args.putInt("GROUP", position);
        tabFragment.setArguments(args);
//        tabFragment.setGrouId(position);

        return tabFragment;
    }

    // This method return the titles for the Tabs in the Tab Strip
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip
    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}