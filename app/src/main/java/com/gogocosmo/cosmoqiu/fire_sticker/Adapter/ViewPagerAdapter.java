package com.gogocosmo.cosmoqiu.fire_sticker.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TabFragment;

import java.util.ArrayList;

/**
 * Created by cosmoqiu on 2/10/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    ArrayList<String> _titles;

    public ViewPagerAdapter(FragmentManager fm, ArrayList<String> titles, int mNumbOfTabsumb) {
        super(fm);

        this._titles = titles;
        this.Titles = _titles.toArray(new CharSequence[_titles.size()]);
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        //TODO:Add Support for Group Id

        TabFragment tabFragment = new TabFragment();
        tabFragment.setGrouId(position);
        return tabFragment;




//        if (position == 0) // if the position is 0 we are returning the First tab
//        {
//            TabFragment tabFragment = new TabFragment();
//            return tabFragment;
//        } else {
//            TabFragment tabFragment = new TabFragment();
//            return tabFragment;
//        }
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

    public void addNewTab(String title) {

        this.NumbOfTabs++;
        this._titles.add(title);
        this.Titles = _titles.toArray(new CharSequence[_titles.size()]);
        notifyDataSetChanged();
    }

    public void notifyListViewDataChanged(int groupId) {
        //TODO: UPDATE MODEL SYSTEM TO SUPPORT MULTIPLE GROUPS
//        ((TabFragment)getItem(groupId)).notifyDataChanged();
    }
}