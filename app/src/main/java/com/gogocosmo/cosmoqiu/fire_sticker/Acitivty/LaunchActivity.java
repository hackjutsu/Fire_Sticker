package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
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
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.DrawerRecyclerViewAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TabFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
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

    private ImageButton _fireButton;
    private Toolbar _toolbar;

    // Declaring Tab Views and Variables
    private ViewPager _pager;
    private ViewPagerAdapter _viewPagerAdapter;
    private SlidingTabLayout _slidingTabsLayout;

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

    private int PROFILE = R.drawable.owl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        /*********************************  DataBase Configurations  **********************************/
        ItemFactory.setItemsTableHelper(ItemsTableHelper.getInstance(this));
        ItemFactory.setGroupsTableHelper(GroupsTableHelper.getInstance(this));

        // Toolbar Configurations
        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        _toolbar.inflateMenu(R.menu.menu_launch);
        setSupportActionBar(_toolbar);
        _toolbar.setTitleTextColor(Color.WHITE);

        // Setting up Database and tips/tutorials for the first run
        SharedPreferences mPreference = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstRun = mPreference.getBoolean("NOTEIT_FIRST_RUN", true);
        if (isFirstRun == true) {
            // Code to run once
            LoadDefaultData();
            SharedPreferences.Editor editor = mPreference.edit();
            editor.putBoolean("NOTEIT_FIRST_RUN", false);
            editor.commit();
        }

        /*********************************  Tabs Configurations  **********************************/
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

                if (ItemFactory.getItemGroupObjectList().size() == 0) {
                    Toast.makeText(LaunchActivity.this, "Please create a group first.", Toast.LENGTH_SHORT).show();
                    return;
                }

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

        _viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                ItemFactory.getItemGroupObjectNameList(),
                ItemFactory.getItemGroupObjectList().size());
        _pager.setAdapter(_viewPagerAdapter);

        if (ItemFactory.getItemGroupObjectList().size() <= 3) {
            // To make the Tabs Fixed set this true, This makes the _slidingTabsLayout Space Evenly
            // in Available width
            _slidingTabsLayout.setDistributeEvenly(true);
        } else {
            _slidingTabsLayout.setDistributeEvenly(false);
        }

        _slidingTabsLayout.setViewPager(_pager); // Update the Tabs
    }

    private void updateDrawerItems() {

        String[] osArray = ItemFactory.getItemGroupObjectNameList().toArray(
                new String[ItemFactory.getItemGroupObjectNameList().size()]);
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
                getSupportActionBar().setTitle(Html.fromHtml("<b>" + _activityTitle + "</b>"));
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

        listView.setItemChecked(position, true);

        ItemFactory.setSelectedItemIndex(groupId, position);
        _activatedItemArrayAdapter = adapter;
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

        if (_actionMode != null) {

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
                _slidingTabsLayout.setCurrentTab(position);
                _drawerLayout.closeDrawers();
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
        if (_actionMode != null) {
            _actionMode.finish();
        }

        _activatedGroupId = position;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        _menu = menu;

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemMoveToTop = _menu.findItem(R.id.action_move_to_top);
//        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

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

                if (ItemFactory.getItemGroupObjectList().size() == 0) {
                    Toast.makeText(this, "Please create a group first.", Toast.LENGTH_SHORT).show();
                    return true;
                }

                Intent intent = new Intent(this, NewItemActivity.class);
                intent.putExtra("CURRENT_TAB", _slidingTabsLayout.getCurrentTabPosition());
                startActivityForResult(intent, NEW_ITEM_REQ);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.action_move_to_top:

                moveToTop();
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

//        MenuInflater inflater = mode.getMenuInflater();
//        inflater.inflate(R.menu.menu_launch, menu);
        getSupportActionBar().setTitle("");

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemMoveToTop = _menu.findItem(R.id.action_move_to_top);
//        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

        itemAdd.setVisible(false);
//        itemBack.setVisible(true);
        itemMoveToTop.setVisible(true);
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

        getSupportActionBar().setTitle(Html.fromHtml("<b>" + _activityTitle + "</b>"));

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
//        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);
        MenuItem itemMoveToTop = _menu.findItem(R.id.action_move_to_top);

        itemAdd.setVisible(true);
//        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemMoveToTop.setVisible(false);
        itemSlash.setVisible(false);

        _fireButton.setVisibility(View.VISIBLE);
        _actionMode = null;

        if (_activatedItemListView != null) {
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

    // Move the selected item to the top of the list. The list will automatically scroll to the top,
    // and blink the first item view to notify the users.
    private void moveToTop() {

        int toMoveToTopIndex = ItemFactory.getSelectedItemIndex(_activatedGroupId);
        Item oldItem = ItemFactory.getItemList(_activatedGroupId).get(toMoveToTopIndex);

        Item newItem = ItemFactory.createItem(
                _activatedGroupId,
                oldItem.getFront(),
                oldItem.getBack(),
                oldItem.getTitle(),
                oldItem.getBookMark(),
                oldItem.getStamp()
        );

        // Interestingly, when adapter.remove() is called, the notifyDataSetChanged() method is
        // also called. Therefore, we don't need to call notifyDataSetChanged() method to notify
        // the adding of the new item.
        _activatedItemArrayAdapter.remove(oldItem);
        ItemFactory.notifyItemDeletion(oldItem);

        // Set the listview to display to the top of the list. Refer to the following link to see
        // why I use a runnable().
        _activatedItemListView.post(new Runnable() {
            @Override
            public void run() {
                // https://groups.google.com/forum/#!topic/android-developers/EnyldBQDUwE
                _activatedItemListView.setSelection(0);
            }
        });

        // If the data set is large, the scrolling animation will make people fill dizzy
        // Scroll to the top
//        _activatedItemListView.smoothScrollToPosition(0);

        // When scrolled to the top, blink the first item view
        _activatedItemListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // When the first visible view is the top view...
                boolean topOfFirst = _activatedItemListView.getChildAt(0).getTop() == 0;
                if (topOfFirst) {

                    final View v = _activatedItemListView.getChildAt(0);
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

                    // Cancel the OnScrollListener so that the top view won't blink during normal scroll
                    _activatedItemListView.setOnScrollListener(null);
                }
            }
        });
    }

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
    protected void onDestroy() {
        ItemFactory.closeAllDatabase();
        super.onDestroy();
    }

    private void LoadDefaultData() {

        ItemFactory.createGroup("Welcome!");
        ItemFactory.createGroup("Great Ideas");
        ItemFactory.createGroup("To-Do List");

        // Add default items to group "Welcome!"
        ItemFactory.createItem(0, "Bookmark the important notes", "", "BookMark", 1, 0);
        ItemFactory.createItem(0, "Try to edit this note ^_^", "Stamp it when the task is finished", "Edit Notes", 0, 1);
        ItemFactory.createItem(0, "You can bookmark and stamp at the same time", "Awesome note", "Long Press to Delete", 1, 1);
        ItemFactory.createItem(0, "Welcome to Note it! Here, every note has two colorful sides.",
                "You can write down the notes on the front and add hint on the back. " +
                        "Or just write the question on the front, and solutions on the back",
                "Welcome to Note it!", 1, 0);

        // Add default items to group "Great ideas"
        ItemFactory.createItem(1, "Change the world!", "Less pollution.", "Awesome Idea", 1, 0);
        ItemFactory.createItem(1, "Do meditate. Don't stay up all night. ",
                "Studies show that those who meditate daily for at least 30 minutes" +
                        "have better focus", "Ready, Meditate", 0, 1);
        ItemFactory.createItem(1, "Do trust yourself. Don't go it alone.",
                "When people believe they can grow their brainpower," +
                        "they become more curious and more open-minded.", "Ready, Trust", 1, 1);
        ItemFactory.createItem(1, "Do model the great. Don't be a sheep.",
                "Think, what are smart people doing, and what can that teach me?", "Ready, Model", 0, 0);
        ItemFactory.createItem(1, "Do pay attention. Don't just pass judgement.",
                "Listen closely. Be observant and informed. Be patient and in the moment", "Ready, Attentions", 0, 0);

        // Add default items to group "To-Do List"
        ItemFactory.createItem(2, "Read the Wiki about Scotland History in 19 century.", "Watch the documentary.", "Investigate Scotland History", 1, 1);
        ItemFactory.createItem(2, "Everyone is talking about it. It must be interesting.", "Order it online!", "Order The Lean Startup", 0, 0);
        ItemFactory.createItem(2, "Egg, Milk, Onions, Cheese", "Maybe some pens.", "Target Shopping", 0, 1);
        ItemFactory.createItem(2, "Just do it!", "Keep running!", "30 minutes' Running", 1, 0);

        displayShowcaseViewZero();
    }

    private void displayShowcaseViewZero() {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.CENTER_IN_PARENT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 8);

        } else {
            lps.setMargins(margin, margin, margin, margin * 8);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(this)
                .setContentTitle(Html.fromHtml("<b>" + "Note it!" + "</b>"))
                .setContentText("A quick guide within 15 seconds.")
                .setTarget(Target.NONE)
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        displayShowcaseViewOne();
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                }).build();
        sv.setButtonPosition(lps);
    }

    private void displayShowcaseViewOne() {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 5);

        } else {
            lps.setMargins(margin, margin, margin, margin);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(this)
                .setContentTitle("New Note")
                .setContentText("Click here to create your note.")
                .setTarget(new ViewTarget(R.id.showCasePoint_Add, this))
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        showOverlayTutorialTwo();
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                }).build();
        sv.setButtonPosition(lps);
    }

    public void showOverlayTutorialTwo() {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 5);

        } else {
            lps.setMargins(margin, margin, margin, margin);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(this)
                .setContentTitle("Group Navigation")
                .setContentText("Tap on the group name for navigation or ... Just swipe!")
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(new ViewTarget(R.id.showCasePoint_Tab, this))
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        showOverlayTutorialThree();
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                })
                .build();
        sv.setButtonPosition(lps);
    }

    public void showOverlayTutorialThree() {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 5);

        } else {
            lps.setMargins(margin, margin, margin, margin);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(this)
                .setContentTitle("Quick Overview")
                .setContentText("Take a quick look through the notes in the selected group.")
                .setStyle(R.style.CustomShowcaseThemeEnd)
                .setTarget(new ViewTarget(R.id.FireButton, this))
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        scv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                })
                .build();

        sv.setButtonPosition(lps);
    }
}

