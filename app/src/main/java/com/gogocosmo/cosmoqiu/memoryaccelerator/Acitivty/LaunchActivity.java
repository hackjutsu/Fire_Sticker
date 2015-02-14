package com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Fragment.TabFragment;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Library.SlidingTabLayout;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;

import java.util.ArrayList;

public class LaunchActivity extends ActionBarActivity implements
        TabFragment.OnListItemLongClickListener,
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

    // Declaring Tab Views and Variables
    private ViewPager _pager;
    private ViewPagerAdapter _viewPagerAdapter;
    private SlidingTabLayout _slidingTabsLayout;
    private ArrayList<String> _titles;
    private int _tabsNum = 3;

    // Declaring Drawer Views and Variables
    private ListView _drawerList;
    private ArrayAdapter<String> _drawerListAdapter;
    private ActionBarDrawerToggle _drawerToggle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        /*********************************  Tabs Configurations  **********************************/
        _titles = new ArrayList<>();
        _titles.add("AAA");
        _titles.add("BBB");
        _titles.add("CCC");

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
        _drawerList = (ListView) findViewById(R.id.navList);
        _drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == _titles.size()) {
                    // ADD A NEW COLUMN
                    String newColumn = "TAB " + String.valueOf(position);
                    _viewPagerAdapter.addNewTab(newColumn);
                    _slidingTabsLayout.setViewPager(_pager); // Update the Tabs

                    updateDrawerItems(); // Update Drawer list

                    if (_tabsNum <= 5) {
                        _slidingTabsLayout.setDistributeEvenly(true);
                    } else {
                        _slidingTabsLayout.setDistributeEvenly(false);
                    }
                }

                _slidingTabsLayout.setCurrentTab(position); // Set the new Tab as the current Tab
                _drawerLayout.closeDrawers();
            }
        });

        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _activityTitle = getTitle().toString();

        updateDrawerItems();
        setupDrawer();

        //TODO: Understand these settings
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void updateDrawerItems() {

        ArrayList<String> drawerArray = new ArrayList<>(_titles);
        drawerArray.add("NEW COLUMN");
        String[] osArray = drawerArray.toArray(new String[drawerArray.size()]);
        _drawerListAdapter = new ArrayAdapter<>(this, R.layout.drawer_list_rowlayout, osArray);
        _drawerList.setAdapter(_drawerListAdapter);
    }

    private void setupDrawer() {

        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {

                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                if (_actionMode != null) {
                    _actionMode.finish();
                }
                getSupportActionBar().setTitle("Navigation");
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {

                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(_activityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        //TODO: Understand these settings
        _drawerToggle.setDrawerIndicatorEnabled(true);
        _drawerLayout.setDrawerListener(_drawerToggle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        _menu = menu;

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);

        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Activate the navigation drawer toggle
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
        // assumes that you have "contexual.xml" menu resources
        inflater.inflate(R.menu.menu_launch, menu);
        getSupportActionBar().setTitle("");

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);

        itemAdd.setVisible(false);
        itemBack.setVisible(true);
        itemDelete.setVisible(true);

        _fireButton.setVisibility(View.INVISIBLE);

        if (_selectedView != null) {

            _selectedView.setBackgroundColor(Color.rgb(227, 239, 209));
        }

        _actionMode = mode;

        return false;
    }

    @Override
    public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:

                Toast.makeText(this,
                        "on Action DELETE",
                        Toast.LENGTH_SHORT).show();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(android.view.ActionMode mode) {

        Log.d(TAG, "onDestroyActionMode");

        getSupportActionBar().setTitle(_activityTitle);

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);

        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);


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
    }

    @Override
    public void OnPageScrolled() {
        // Called when clients scroll the page tab
        if (_actionMode != null) {
            _actionMode.finish();
        }
    }
}
