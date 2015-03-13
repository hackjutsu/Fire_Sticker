package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.DrawerRecyclerViewAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TabFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedShowcase;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.GroupsTableHelper;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.ItemsTableHelper;
import com.gogocosmo.cosmoqiu.slidingtablibrary.SlidingTabLayout;

import java.util.HashMap;

public class LaunchActivity extends ActionBarActivity implements
        TabFragment.OnTabListItemClickListener,
        DrawerRecyclerViewAdapter.IDrawerListItemClickListener,
        ActionMode.Callback,
        SlidingTabLayout.OnPageScrollListener {

    static final private String TAG = "MEMORY-ACC";

    final private int EDIT_GROUP_REQ = 1;
    final private int VIEW_DETAILS_REQ = 2;
    final private int NEW_ITEM_REQ = 3;

    private ImageButton mCarouselButton;
    private Toolbar mToolbar;

    // Declaring Tab Views and Variables
    private ViewPager mPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private SlidingTabLayout mSlidingTabsLayout;

    // Declaring Drawer Views and Variables
    private ActionBarDrawerToggle mDrawerToggle;
    private RecyclerView mDrawerRecyclerView;
    private RecyclerView.Adapter mDrawerViewAdapter;
    private RecyclerView.LayoutManager mDrawerRecyclerViewLayoutManager;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    // Action Mode and Variables
    private Menu mMenu;
    private android.view.ActionMode mActionMode;
    private ListView mActivatedItemListView;
    private int mActivatedGroupId;
    private ItemArrayAdapter mActivatedItemArrayAdapter;

    private int mProfile = R.drawable.owl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        /*********************************  DataBase Configurations  **********************************/
        ItemFactory.setItemsTableHelper(ItemsTableHelper.getInstance(this));
        ItemFactory.setGroupsTableHelper(GroupsTableHelper.getInstance(this));

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        mToolbar.inflateMenu(R.menu.menu_launch);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

        // Setting up Database and tips/tutorials for the first run
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = mPreference.getBoolean("NOTEIT_FIRST_RUN", true);
        if (isFirstRun == true) {
            // Code to run once
            CustomizedShowcase.loadDefaultDataForFirstInstallation();
            CustomizedShowcase.displayLaunchActivtyShowcaseViewZero(this);

            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean("NOTEIT_FIRST_RUN", false);
            editor.commit();
        }

        /*********************************  Tabs Configurations  **********************************/
        mPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabsLayout = (SlidingTabLayout) findViewById(R.id.tabs);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        mSlidingTabsLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        updateSlidingTabs();

        /*********************************  Button Configurations  ********************************/
        mCarouselButton = (ImageButton) findViewById(R.id.FireButton);
        mCarouselButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                if (ItemFactory.getItemGroupObjectList().size() == 0) {
                    CustomizedToast.showToast(LaunchActivity.this, "Please create a group first.");
                    return;
                }

                Intent intent = new Intent(LaunchActivity.this, CarouselActivity.class);
                intent.putExtra("GROUP", mActivatedGroupId);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        /*********************************  Drawer Configurations  *********************************/

        mDrawerRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mDrawerRecyclerView.setHasFixedSize(true);

        mDrawerRecyclerViewLayoutManager = new LinearLayoutManager(this);
        mDrawerRecyclerView.setLayoutManager(mDrawerRecyclerViewLayoutManager);

        mActivityTitle = getTitle().toString();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        updateDrawerItems();
        setupDrawer();
    }

    private void updateSlidingTabs() {

        mViewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                ItemFactory.getItemGroupObjectNameList(),
                ItemFactory.getItemGroupObjectList().size());
        mPager.setAdapter(mViewPagerAdapter);

        if (ItemFactory.getItemGroupObjectList().size() <= 3) {
            // To make the Tabs Fixed set this true, This makes the mSlidingTabsLayout Space Evenly
            // in Available width
            mSlidingTabsLayout.setDistributeEvenly(true);
        } else {
            mSlidingTabsLayout.setDistributeEvenly(false);
        }

        mSlidingTabsLayout.setViewPager(mPager); // Update the Tabs
    }

    private void updateDrawerItems() {

        String[] osArray = ItemFactory.getItemGroupObjectNameList().toArray(
                new String[ItemFactory.getItemGroupObjectNameList().size()]);
        mDrawerViewAdapter = new DrawerRecyclerViewAdapter(osArray, mProfile, this);

        // Setting the adapter to RecyclerView
        mDrawerRecyclerView.setAdapter(mDrawerViewAdapter);
    }

    private void setupDrawer() {

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0); // Disable hamburger/arrow animation
                // code here will execute once the drawer is opened
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
                getSupportActionBar().setTitle(Html.fromHtml("<b>" + mActivityTitle + "</b>"));
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // Disable hamburger/arrow animation
            }
        }; // Drawer Toggle Object Made

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
    }

    private void deleteListItem() {

        int toRemoveIndex = ItemFactory.getSelectedItemIndex(mActivatedGroupId);

        animatedRemoval(
                mActivatedItemArrayAdapter,
                mActivatedItemListView,
                toRemoveIndex);
    }

    @Override
    public void OnListItemLongClicked(ItemArrayAdapter adapter,
                                      ListView listView,
                                      View view,
                                      int groupId,
                                      int position) {

        listView.setItemChecked(position, true);

        ItemFactory.setSelectedItemIndex(groupId, position);
        mActivatedItemArrayAdapter = adapter;
        mActivatedItemListView = listView;
        mActivatedGroupId = groupId;

        startActionMode(this);
    }

    @Override
    public void OnListItemClicked(ItemArrayAdapter adapter,
                                  ListView listView,
                                  View view,
                                  int groupId,
                                  int position) {

        if (mActionMode != null) {

            ItemFactory.setSelectedItemIndex(groupId, position);
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
                mSlidingTabsLayout.setCurrentTab(position);
                mDrawerLayout.closeDrawers();
                break;
            case DrawerRecyclerViewAdapter.TYPE_HEADER:

                break;
            case DrawerRecyclerViewAdapter.TYPE_EDIT:

                Intent intentEdit = new Intent(this, EditGroupActivity.class);
                startActivityForResult(intentEdit, EDIT_GROUP_REQ);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;
            case DrawerRecyclerViewAdapter.TYPE_ABOUTAPP:

                Intent intentAbout = new Intent(this, AboutAppActivity.class);
                startActivity(intentAbout);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;
            default:
        }
    }

    @Override
    public void OnPageScrolled(int position) {
        // Called when clients scroll the page tab
        if (mActionMode != null) {
            mActionMode.finish();
        }

        mActivatedGroupId = position;
    }

    /* Action Mode Methods */
    @Override
    public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {

        // MenuInflater inflater = mode.getMenuInflater();
        // inflater.inflate(R.menu.menu_launch, menu);
        getSupportActionBar().setTitle("");

        MenuItem itemAdd = mMenu.findItem(R.id.action_add);
        MenuItem itemDelete = mMenu.findItem(R.id.action_delete);
        MenuItem itemMoveToTop = mMenu.findItem(R.id.action_move_to_top);
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);

        itemAdd.setVisible(false);
        itemMoveToTop.setVisible(true);
        itemDelete.setVisible(true);
        itemSlash.setVisible(true);

        mCarouselButton.setVisibility(View.INVISIBLE);
        mActionMode = mode;

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

        getSupportActionBar().setTitle(Html.fromHtml("<b>" + mActivityTitle + "</b>"));

        MenuItem itemAdd = mMenu.findItem(R.id.action_add);
        MenuItem itemDelete = mMenu.findItem(R.id.action_delete);
//        MenuItem itemBack = mMenu.findItem(R.id.action_back);
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);
        MenuItem itemMoveToTop = mMenu.findItem(R.id.action_move_to_top);

        itemAdd.setVisible(true);
//        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemMoveToTop.setVisible(false);
        itemSlash.setVisible(false);

        mCarouselButton.setVisibility(View.VISIBLE);
        mActionMode = null;

        if (mActivatedItemListView != null) {
            mActivatedItemListView.setItemChecked(ItemFactory.getSelectedItemIndex(mActivatedGroupId), false);
        }
    }

    // Move the selected item to the top of the list. The list will automatically scroll to the top,
    // and blink the first item view to notify the users.
    private void animatedSendToTop() {

        int toMoveToTopIndex = ItemFactory.getSelectedItemIndex(mActivatedGroupId);
        Item oldItem = ItemFactory.getItemList(mActivatedGroupId).get(toMoveToTopIndex);

        Item newItem = ItemFactory.createItem(
                mActivatedGroupId,
                oldItem.getFront(),
                oldItem.getBack(),
                oldItem.getTitle(),
                oldItem.getBookmark(),
                oldItem.getStamp()
        );

        // Interestingly, when adapter.remove() is called, the notifyDataSetChanged() method is
        // also called. Therefore, we don't need to call notifyDataSetChanged() method to notify
        // the adding of the new item.
        mActivatedItemArrayAdapter.remove(oldItem);
        ItemFactory.notifyItemDeletion(oldItem);

        // Set the listview to display to the top of the list. Refer to the following link to see
        // why I use a runnable().
        mActivatedItemListView.post(new Runnable() {
            @Override
            public void run() {
                // https://groups.google.com/forum/#!topic/android-developers/EnyldBQDUwE

                // When scrolled to the top, blink the first item view
                mActivatedItemListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {

                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                        // When the first visible view is the top view...
                        boolean topOfFirst = mActivatedItemListView.getChildAt(0).getTop() == 0;
                        if (topOfFirst) {

                            final View v = mActivatedItemListView.getChildAt(0);
                            Animation fadeOut = new AlphaAnimation(1.0f, 0.1f);
                            fadeOut.setDuration(500);
                            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    Animation fadeIn = new AlphaAnimation(0.1f, 1.0f);
                                    fadeIn.setDuration(500);
                                    v.startAnimation(fadeIn);
                                }
                            });

                            v.startAnimation(fadeOut);

                            // If the data set is large, the scrolling animation will make people fill dizzy
                            // Scroll to the top
                            // mActivatedItemListView.smoothScrollToPosition(0);

                            // Cancel the OnScrollListener so that the top view won't blink during normal scroll
                            mActivatedItemListView.setOnScrollListener(null);
                        }
                    }
                });

                mActivatedItemListView.setSelection(0);
            }
        });

    }

    // Adapted from Google I/O 2013
    public void animatedRemoval(final ArrayAdapter adapter,
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
        Item toDeleteItem = (Item) adapter.getItem(position);
        adapter.remove(toDeleteItem);
        // Notify Database the Deletion
        ItemFactory.notifyItemDeletion(toDeleteItem);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        mMenu = menu;

        MenuItem itemAdd = mMenu.findItem(R.id.action_add);
        MenuItem itemDelete = mMenu.findItem(R.id.action_delete);
        MenuItem itemMoveToTop = mMenu.findItem(R.id.action_move_to_top);
//        MenuItem itemBack = mMenu.findItem(R.id.action_back);
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);

        itemAdd.setVisible(true);
//        itemBack.setVisible(false);
        itemMoveToTop.setVisible(false);
        itemDelete.setVisible(false);
        itemSlash.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            if (mActionMode != null) {
                mActionMode.finish();
            }
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_delete:

                deleteListItem();
                if (mActionMode != null) {
                    mActionMode.finish();
                }
                return true;
            case R.id.action_add:

                if (ItemFactory.getItemGroupObjectList().size() == 0) {
                    CustomizedToast.showToast(this, "Please create a group first.");
                    return true;
                }

                Intent intent = new Intent(this, NewItemActivity.class);
                intent.putExtra("CURRENT_TAB", mSlidingTabsLayout.getCurrentTabPosition());
                startActivityForResult(intent, NEW_ITEM_REQ);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.action_move_to_top:

                animatedSendToTop();
                if (mActionMode != null) {

                    mActionMode.finish();
                }
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {

        if (mActionMode != null) {
            // If the Action Mode is on, then pressing BACK should exit the Action Mode, instead of
            // exit the App.
            mActionMode.finish();
            mActionMode = null;
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSlidingTabsLayout.registerPageScrollListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        mSlidingTabsLayout.unregisterPageScrollListener(this);

        // End the Action Mode when this Activity is no longer visible
        if (mActionMode != null) {
            mActionMode.finish();
            mActionMode = null;
        }
    }

    @Override
    protected void onDestroy() {
        ItemFactory.closeAllDatabase();
        super.onDestroy();
    }

    @Override
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
                mSlidingTabsLayout.setCurrentTab(mActivatedGroupId);
            }
            if (resultCode == RESULT_CANCELED) {

            }
        } else if (requestCode == NEW_ITEM_REQ) {
            if (resultCode == RESULT_OK) {

                int updatedGroupId = data.getExtras().getInt("UPDATED_GROUP");
                updateSlidingTabs();
                mSlidingTabsLayout.setCurrentTab(updatedGroupId);
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }//onActivityResult
}