package com.gogocosmo.cosmoqiu.first_sticker.Acitivty;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gogocosmo.cosmoqiu.first_sticker.Adapter.DrawerRecyclerViewAdapter;
import com.gogocosmo.cosmoqiu.first_sticker.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.first_sticker.Fragment.TabFragment;
import com.gogocosmo.cosmoqiu.first_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.first_sticker.R;

import com.gogocosmo.cosmoqiu.slidingtablibrary.SlidingTabLayout;

import java.util.ArrayList;

public class LaunchActivity extends ActionBarActivity implements
        TabFragment.OnListItemLongClickListener,
        DrawerRecyclerViewAdapter.IDrawerListItemClickListener,
        android.view.ActionMode.Callback,
        SlidingTabLayout.OnPageScrollListener {

    // Load the inital data for testing purpose
    static {

        String[] questionSamples = new String[]{
                "How many rings on the Olympic flag?",
                "What is the currency of Austria?",
                "How did Alfred Nobel make his money?",
                "Which car company makes the Celica?",
                "What is a baby rabbit called?",
                "What is a Winston Churchill?",
                "What plant does the Colorado beetle attack?",
                "Who wrote the Opera Madam Butterfly?",
                "Which country do Sinologists study?",
                "What was the first James Bond film?",
                "How many rings on the Olympic flag?",
                "What is the currency of Austria?",
                "How did Alfred Nobel make his money?",
                "Which car company makes the Celica?",
                "What is a baby rabbit called?",
                "What is a Winston Churchill?",
                "What plant does the Colorado beetle attack?",
                "Who wrote the Opera Madam Butterfly?",
                "Which country do Sinologists study?",
                "What was the first James Bond film?"
        };

        String[] answerSamples = new String[]{
                "Five",
                "Schilling",
                "He invented Dynamite",
                "Toyota",
                "Kit or Kitten",
                "Cigar",
                "Potato",
                "Puccini",
                "China",
                "Dr No",
                "Five",
                "Schilling",
                "He invented Dynamite",
                "Toyota",
                "Kit or Kitten",
                "Cigar",
                "Potato",
                "Puccini",
                "China",
                "Dr No"
        };

        for (int i = 0; i < 20; ++i) {
            ItemFactory.createItem(questionSamples[i], answerSamples[i]);
        }
    }

    final private String TAG = "MEMORY-ACC";

    private ImageButton _fireButton;
    private Toolbar _toolbar;

    // Declaring Tab Views and Variables
    private ViewPager _pager;
    private ViewPagerAdapter _viewPagerAdapter;
    private SlidingTabLayout _slidingTabsLayout;
    private ArrayList<String> _titles;
    private int _tabsNum = 3;

    // Declaring Drawer Views and Variables
    private ActionBarDrawerToggle _drawerToggle;
    private RecyclerView _drawerRecyclerView;
    private RecyclerView.Adapter _drawerViewAdapter;
    private RecyclerView.LayoutManager _drawerRecyclerViewLayoutManager;
    private DrawerLayout _drawerLayout;
    private String _activityTitle;

    // Action Mode and Variables
    private Menu _menu;
    private android.view.ActionMode _actionMode;

    // The _selectedView keeps track of the reference of the current selected view. It has to be
    // static since the ItemArrayAdapter will update the selected view reference during the list
    // view item recycling. It would be simpler if the view cycling is disabled, but this approach
    // will sacrifice memory performance.
    static public View _selectedView = null;

    int PROFILE = R.drawable.lollipop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);

        /*********************************  Tabs Configurations  **********************************/
        _titles = new ArrayList<>();
        _titles.add("TAB 0");
        _titles.add("TAB 1");
        _titles.add("TAB 2");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number
        // Of Tabs.
        _viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), _titles, _tabsNum);

        // Assigning ViewPager View and setting the _viewPagerAdapter
        _pager = (ViewPager) findViewById(R.id.pager);
        _pager.setAdapter(_viewPagerAdapter);

        // Assigning the Sliding Tab Layout View
        _slidingTabsLayout = (SlidingTabLayout) findViewById(R.id.tabs);
        if (_tabsNum <= 3) {
            // To make the Tabs Fixed set this true, This makes the _slidingTabsLayout Space Evenly
            // in Available width
            _slidingTabsLayout.setDistributeEvenly(true);
        }

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        _slidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        _slidingTabsLayout.setViewPager(_pager);

        /*********************************  Button Configurations  ********************************/
        _fireButton = (ImageButton) findViewById(R.id.FireButton);
        _fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LaunchActivity.this, CarouselActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        /*********************************  Drawer Configurations  *********************************/

        _drawerRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        _drawerRecyclerView.setHasFixedSize(true);

        _drawerRecyclerViewLayoutManager = new LinearLayoutManager(this);
        _drawerRecyclerView.setLayoutManager(_drawerRecyclerViewLayoutManager);

        _activityTitle = getTitle().toString();
        _drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        updateDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void updateDrawerItems() {

        String[] osArray = _titles.toArray(new String[_titles.size()]);
        _drawerViewAdapter = new DrawerRecyclerViewAdapter(osArray, "", "", PROFILE, this);

        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        // Setting the adapter to RecyclerView
        _drawerRecyclerView.setAdapter(_drawerViewAdapter);
    }

    private void setupDrawer() {

        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, _toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                if (_actionMode != null) {
                    _actionMode.finish();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
                getSupportActionBar().setTitle(_activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


        }; // Drawer Toggle Object Made

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        _drawerToggle.setDrawerIndicatorEnabled(true);
        _drawerLayout.setDrawerListener(_drawerToggle); // Drawer Listener set to the Drawer toggle
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        _menu = menu;

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemSlash.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

//        Activate the navigation drawer toggle
        if (_drawerToggle.onOptionsItemSelected(item)) {
            if (_actionMode != null) {
                _actionMode.finish();
            }
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_delete:
                Toast.makeText(this,
                        "DELETE",
                        Toast.LENGTH_SHORT).show();
                if (_actionMode != null) {
                    _actionMode.finish();
                }
                return true;
            case R.id.action_add:
                Intent intent = new Intent(this, NewItemActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_back:
                if (_actionMode != null) {
                    _actionMode.finish();
                }
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        _drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void OnListItemLongClicked(View view, int position) {

        if (_selectedView != null) {
            _selectedView.setBackgroundColor(Color.WHITE);
        }

        ItemFactory.setSelectedItemIndex(position);
        _selectedView = view;
        startActionMode(this);
    }

    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {

        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_launch, menu);
        getSupportActionBar().setTitle("");

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

        itemAdd.setVisible(false);
        itemBack.setVisible(true);
        itemDelete.setVisible(true);
        itemSlash.setVisible(true);

        _fireButton.setVisibility(View.INVISIBLE);

        if (_selectedView != null) {

            _selectedView.setBackgroundColor(Color.parseColor("#E57373"));
        }

        _actionMode = mode;

        // Return false since we are using a fake Action Mode with a toolbar
        return false;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        // We are using a fake Action Mode with a toolbar
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
        // We are using a fake Action Mode with a toolbar
        return false;
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {
        // We are using a fake Action Mode with a toolbar
        Log.d(TAG, "onDestroyActionMode");

        getSupportActionBar().setTitle(_activityTitle);

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);


        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemSlash.setVisible(false);


//        _fireButton.animate().setDuration(1000).scaleX(1).scaleY(1)
//                .withStartAction(new Runnable() {
//                    @Override
//                    public void run() {
//                        _fireButton.setVisibility(View.VISIBLE);
//                        _fireButton.setScaleX(1);
//                        _fireButton.setScaleY(1);
//
//                    }});
        _fireButton.setVisibility(View.VISIBLE);

        if (_selectedView != null) {
            Log.d(TAG, "restore background color");

            _selectedView.setBackgroundColor(Color.WHITE);
            ItemFactory.setSelectedItemIndex(-1);
            _selectedView = null;
        }

        _actionMode = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        _slidingTabsLayout.registerPageScrollListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _slidingTabsLayout.unregisterPageScrollListener(this);

        // End the Action Mode when this Activity is no longer visible
        if (_actionMode != null) {
            _actionMode.finish();
            _actionMode = null;
        }
    }

    @Override
    public void OnPageScrolled() {
        // Called when clients scroll the page tab
        if (_actionMode != null) {
            _actionMode.finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (_actionMode != null) {
            // If the Action Mode is on, then pressing BACK should exit the Action Mode, instead of
            // exit the App.
            _actionMode.finish();
            _actionMode = null;
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onDrawerItemClicked(View v, int position, int viewType) {

        switch (viewType) {
            case DrawerRecyclerViewAdapter.TYPE_ITEM:
                // Set the new Tab as the current Tab
                _slidingTabsLayout.setCurrentTab(position);
                _drawerLayout.closeDrawers();
                break;
            case DrawerRecyclerViewAdapter.TYPE_HEADER:

                break;
            case DrawerRecyclerViewAdapter.TYPE_END:
                String newColumn = "TAB " + String.valueOf(position);
                _viewPagerAdapter.addNewTab(newColumn);
                _slidingTabsLayout.setViewPager(_pager); // Update the Tabs

                updateDrawerItems();

//                Intent intent = new Intent(this, NewItemActivity.class);
//                startActivity(intent);
                break;
            default:

        }
    }
}

