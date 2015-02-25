package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

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
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.DrawerRecyclerViewAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TabFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.slidingtablibrary.SlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class LaunchActivity extends ActionBarActivity implements
        TabFragment.OnTabListItemClickListener,
        DrawerRecyclerViewAdapter.IDrawerListItemClickListener,
        android.view.ActionMode.Callback,
        SlidingTabLayout.OnPageScrollListener {

    static final private String TAG = "MEMORY-ACC";


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

        ItemFactory.createGroup("Job Steve");
        ItemFactory.createGroup("Larry Page");
        ItemFactory.createGroup("Elon Musk");
        ItemFactory.createGroup("Bill Gates");

        for (int j = 0; j < ItemFactory.getItemGroupList().size(); ++j) {

            for (int i = 0; i < 10; ++i) {
                String title = "";
                Boolean light = false;

                if (i % (j + 1) == 0) {
                    title = "Awesome Title";
                    light = true;
                }

                ItemFactory.createItem(j, questionSamples[i], answerSamples[i], title, light);
            }
        }
    }


    final private int EDIT_GROUP_REQ = 1;
    final private int VIEW_DETAILS_REQ = 2;
    final private int NEW_ITEM_REQ = 3;

    private ImageButton _fireButton;
    private Toolbar _toolbar;

    // Declaring Tab Views and Variables
    private ViewPager _pager;
    private ViewPagerAdapter _viewPagerAdapter;
    private SlidingTabLayout _slidingTabsLayout;
    private ArrayList<String> _titles;

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
    private ListView _activatedItemListView;
    private int _activatedGroupId;
    private ItemArrayAdapter _activatedItemArrayAdapter;

    // The _selectedView keeps track of the reference of the current selected view. It has to be
    // static since the ItemArrayAdapter will update the selected view reference during the list
    // view item recycling. It would be simpler if the view cycling is disabled, but this approach
    // will sacrifice memory performance.
//    static public View _selectedView = null;

    int PROFILE = R.drawable.lollipop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);
        _toolbar.setTitleTextColor(Color.WHITE);

        /*********************************  Tabs Configurations  **********************************/
        _titles = ItemFactory.getItemGroupList();

        _pager = (ViewPager) findViewById(R.id.pager);
        _slidingTabsLayout = (SlidingTabLayout) findViewById(R.id.tabs);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        _slidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        updateSlidingTabs();

        /*********************************  Button Configurations  ********************************/
        _fireButton = (ImageButton) findViewById(R.id.FireButton);
        _fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                // TODO: (DONE) add groupId support for carousel activity
                Intent intent = new Intent(LaunchActivity.this, CarouselActivity.class);
                intent.putExtra("GROUP", _activatedGroupId);
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
    }

    private void updateSlidingTabs() {

        // TODO: Maybe add support for method notifyDataChanged() here
        _viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                ItemFactory.getItemGroupList(),
                ItemFactory.getItemGroupList().size());
        _pager.setAdapter(_viewPagerAdapter);

        if (ItemFactory.getItemGroupList().size() <= 4) {
            // To make the Tabs Fixed set this true, This makes the _slidingTabsLayout Space Evenly
            // in Available width
            _slidingTabsLayout.setDistributeEvenly(true);
        } else {
            _slidingTabsLayout.setDistributeEvenly(false);
        }

        _slidingTabsLayout.setViewPager(_pager); // Update the Tabs
    }

    private void updateDrawerItems() {

        String[] osArray = ItemFactory.getItemGroupList().toArray(new String[ItemFactory.getItemGroupList().size()]);
        _drawerViewAdapter = new DrawerRecyclerViewAdapter(osArray, "", "", PROFILE, this);

        // Setting the adapter to RecyclerView
        _drawerRecyclerView.setAdapter(_drawerViewAdapter);
    }

    private void setupDrawer() {

        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, _toolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // Disable hamburger/arrow animation
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

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // Disable hamburger/arrow animation
            }
        }; // Drawer Toggle Object Made

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        _drawerToggle.setDrawerIndicatorEnabled(true);
        _drawerLayout.setDrawerListener(_drawerToggle); // Drawer Listener set to the Drawer toggle
    }

    private void deleteListItem() {

        int toRemoveIndex = ItemFactory.getSelectedItemIndex(_activatedGroupId);

        animateRemoval(
                _activatedItemArrayAdapter,
                _activatedItemListView,
                toRemoveIndex);
    }

    @Override
    public void OnListItemLongClicked(ItemArrayAdapter adapter,
                                      ListView listView,
                                      View view,
                                      int groupId,
                                      int position) {
        //TODO: (DONE) Add Support for item list click
        listView.setItemChecked(position, true);

        ItemFactory.setSelectedGroupItemIndex(groupId, position);
        _activatedItemArrayAdapter = adapter;
//        _selectedView = view;
        _activatedItemListView = listView;
        _activatedGroupId = groupId;

        startActionMode(this);
    }

    @Override
    public void OnListItemClicked(ItemArrayAdapter adapter,
                                  ListView listView,
                                  View view,
                                  int groupId,
                                  int position) {
        //TODO: (DONE) Add Support for item list long click
        if (_actionMode != null) {
//            _selectedView = view;
            ItemFactory.setSelectedGroupItemIndex(groupId, position);
            return;
        }

        Intent intent = new Intent(this, ViewActivity.class);
        intent.putExtra("POSITION", position);
        intent.putExtra("GROUP", groupId);
        startActivityForResult(intent, VIEW_DETAILS_REQ);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

                Intent intent = new Intent(this, EditGroupActivity.class);
                startActivityForResult(intent, EDIT_GROUP_REQ);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;
            default:
        }
    }

    @Override
    public void OnPageScrolled(int position) {
        // Called when clients scroll the page tab
        if (_actionMode != null) {
            _actionMode.finish();
        }

        _activatedGroupId = position;
//        Log.d(TAG, "_activatedGroupId = " + String.valueOf(_activatedGroupId));
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

                deleteListItem();
                if (_actionMode != null) {
                    _actionMode.finish();
                }
                return true;
            case R.id.action_add:

                Intent intent = new Intent(this, NewItemActivity.class);
                intent.putExtra("CURRENT_TAB", _slidingTabsLayout.getCurrentTabPosition());
                startActivityForResult(intent, NEW_ITEM_REQ);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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


        _fireButton.setVisibility(View.VISIBLE);
        _actionMode = null;

        if (_activatedItemListView != null) {
            //TODO: (DONE) Add Support for Group ID
            _activatedItemListView.setItemChecked(ItemFactory.getSelectedItemIndex(_activatedGroupId), false);
        }
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == EDIT_GROUP_REQ) {
            if (resultCode == RESULT_OK) {
                updateDrawerItems();
                updateSlidingTabs();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == VIEW_DETAILS_REQ) {
            if (resultCode == RESULT_OK) {

                updateSlidingTabs();
                _slidingTabsLayout.setCurrentTab(_activatedGroupId);

            }
            if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == NEW_ITEM_REQ) {
            if (resultCode == RESULT_OK) {

                int updatedGroupId = data.getExtras().getInt("UPDATED_GROUP");
                updateSlidingTabs();
                _slidingTabsLayout.setCurrentTab(updatedGroupId);
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }//onActivityResult

    // Adapted from Google I/O 2013
    public void animateRemoval(final ArrayAdapter adapter,
                               final ListView listview,
                               int position) {

        final HashMap<Long, Integer> mItemIdTopMap = new HashMap<>();

        int firstVisiblePosition = listview.getFirstVisiblePosition();

        for (int i = 0; i < listview.getChildCount(); ++i) {

            int tmpPosition = firstVisiblePosition + i;

            if (tmpPosition != position) {
                View child = listview.getChildAt(i);
                long itemId = adapter.getItemId(tmpPosition);
                mItemIdTopMap.put(itemId, child.getTop());
            }
        }
        // Delete the item from the adapter
        adapter.remove(adapter.getItem(position));

        final ViewTreeObserver observer = listview.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                boolean firstAnimation = true;
                int firstVisiblePosition = listview.getFirstVisiblePosition();
                for (int i = 0; i < listview.getChildCount(); ++i) {
                    final View child = listview.getChildAt(i);
                    int position = firstVisiblePosition + i;
                    long itemId = adapter.getItemId(position);
                    Integer startTop = mItemIdTopMap.get(itemId);
                    int top = child.getTop();
                    if (startTop != null) {
                        if (startTop != top) {
                            int delta = startTop - top;
                            child.setTranslationY(delta);
                            child.animate().setDuration(300).translationY(0);
                            if (firstAnimation) {
                                child.animate().withEndAction(new Runnable() {
                                    public void run() {

                                        listview.setEnabled(true);
                                    }
                                });
                                firstAnimation = false;
                            }
                        }
                    } else {
                        // Animate new views along with the others. The catch is that they did not
                        // exist in the start state, so we must calculate their starting position
                        // based on neighboring views.
                        int childHeight = child.getHeight() + listview.getDividerHeight();
                        startTop = top + (i > 0 ? childHeight : -childHeight);
                        int delta = startTop - top;
                        child.setTranslationY(delta);
                        child.animate().setDuration(300).translationY(0);
                        if (firstAnimation) {
                            child.animate().withEndAction(new Runnable() {
                                public void run() {

                                    listview.setEnabled(true);
                                }
                            });
                            firstAnimation = false;
                        }
                    }
                }
                mItemIdTopMap.clear();
                return true;
            }
        });
    }
}

