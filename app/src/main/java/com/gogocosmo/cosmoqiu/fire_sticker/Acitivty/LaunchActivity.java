package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.DrawerRecyclerViewAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.GridTabFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.ListTabFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedShowcase;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.DatabaseHelper;
import com.gogocosmo.cosmoqiu.slidingtablibrary.SlidingTabLayout;

import java.util.HashMap;

public class LaunchActivity extends ActionBarActivity implements
        GridTabFragment.OnTabListItemClickListener,
        ListTabFragment.OnTabListItemClickListener,
        DrawerRecyclerViewAdapter.IDrawerListItemClickListener,
        ActionMode.Callback,
        SlidingTabLayout.OnPageScrollListener {

    static final private String TAG = "MEMORY-ACC";

    final private int EDIT_GROUP_REQ = 1;
    final private int VIEW_DETAILS_REQ = 2;
    final private int NEW_ITEM_REQ = 3;
    final private int SETTINGS_REQ = 4;

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
    private GridView mActivatedItemGridView;
    private ListView mActivatedItemListView;
    public int mActivatedGroupId;
    private ArrayAdapter mActivatedItemArrayAdapter;

    private int mProfile = R.drawable.owl_small;
    private SharedPreferences mPreference;

    private boolean mAuthenticated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.STATUS_BAR_BACKGROUND));
        }

        /*********************************  DataBase Configurations  **********************************/
        ItemFactory.setItemsTableHelper(DatabaseHelper.getInstance(this));
//        ItemFactory.getItemGroupObjectNameList();

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        mToolbar.inflateMenu(R.menu.menu_launch);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);

        // Setting up Database and tips/tutorials for the first run
        mPreference = PreferenceManager.getDefaultSharedPreferences(this);
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
                this,
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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Delete");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to delete the selected note?")
                .setCancelable(false)
                .setPositiveButton("DELETE",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        int toRemoveIndex = ItemFactory.getSelectedItemIndex(mActivatedGroupId);

                        int ViewMode = mPreference.getInt("VIEWMODE", 1);

                        if (ViewMode == 1) { // GridView Mode

                            // Delete the item from the adapter
                            Item toDeleteItem = (Item) mActivatedItemArrayAdapter.getItem(toRemoveIndex);
                            mActivatedItemArrayAdapter.remove(toDeleteItem);
                            // Notify Database the Deletion
                            ItemFactory.notifyItemDeletion(toDeleteItem);
                        } else if (ViewMode == 2) { // ListView Removal

                            animatedRemoval(
                                    mActivatedItemArrayAdapter,
                                    mActivatedItemListView,
                                    toRemoveIndex);
                        }
                    }
                })
                .setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    // ListView item long click
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

    // ListView item click
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

        if (ItemFactory.getItemList(groupId).get(position).getLock() == 1) {


        }

        startActivityForResult(intent, VIEW_DETAILS_REQ);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // GridView item long click
    @Override
    public void OnListItemLongClicked(ArrayAdapter adapter,
                                      GridView listView,
                                      View view,
                                      int groupId,
                                      int position) {

        listView.setItemChecked(position, true);

        ItemFactory.setSelectedItemIndex(groupId, position);
        mActivatedItemArrayAdapter = adapter;
        mActivatedItemGridView = listView;
        mActivatedGroupId = groupId;

        startActionMode(this);
    }

    // GridView item click
    @Override
    public void OnListItemClicked(ArrayAdapter adapter,
                                  GridView listView,
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

        if (ItemFactory.getItemList(groupId).get(position).getLock() == 1) {

            showStartAuthDialog(intent);
            return;
        }

        startActivityForResult(intent, VIEW_DETAILS_REQ);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private void showStartAuthDialog(final Intent intent) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_authentication);

        dialog.setCanceledOnTouchOutside(false);

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_dialog_auth);
        TextView confirmButton = (TextView) dialog.findViewById(R.id.confirm_dialog_auth);
        final EditText passwordBox = (EditText) dialog.findViewById(R.id.editText_password_auth);
        passwordBox.setText("");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateSlidingTabs();
                mSlidingTabsLayout.setCurrentTab(mActivatedGroupId);
                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = passwordBox.getText().toString();
                if (password.equals(mPreference.getString("Credentials", "1989"))) {

                    dialog.dismiss();
                    startActivityForResult(intent, VIEW_DETAILS_REQ);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    CustomizedToast.showToast(LaunchActivity.this, "Please try again :)");
                    passwordBox.setText("");
                }
            }
        });

        passwordBox.setOnEditorActionListener(new EditText.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Do your action
                    String password = passwordBox.getText().toString();

                    if (password.equals(mPreference.getString("Credentials", "1989"))) {

                        startActivityForResult(intent, VIEW_DETAILS_REQ);
                        //overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        dialog.dismiss();
                    } else {

                        CustomizedToast.showToast(LaunchActivity.this, "Please try again :)");
                        passwordBox.setText("");
                    }
                }
                return false;
            }
        });

        dialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {

                    updateSlidingTabs();
                    mSlidingTabsLayout.setCurrentTab(mActivatedGroupId);
                    dialog.dismiss();
                    return true;

                }
                return false;
            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        // Automatically pop up the keyboard
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        passwordBox.requestFocus();

        dialog.show();
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
            case DrawerRecyclerViewAdapter.TYPE_EDIT_GROUPS:

                Intent intentEdit = new Intent(this, EditGroupActivity.class);
                startActivityForResult(intentEdit, EDIT_GROUP_REQ);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                break;
            case DrawerRecyclerViewAdapter.TYPE_SETTINGS:

                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivityForResult(intentSettings, SETTINGS_REQ);
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
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);
        MenuItem itemMoveToTop = mMenu.findItem(R.id.action_move_to_top);

        itemAdd.setVisible(true);
        itemDelete.setVisible(false);
        itemMoveToTop.setVisible(false);
        itemSlash.setVisible(false);

        mCarouselButton.setVisibility(View.VISIBLE);
        mActionMode = null;

        int ViewMode = mPreference.getInt("VIEWMODE", 1);

        if (ViewMode == 1 && mActivatedItemGridView != null) {
            mActivatedItemGridView.setItemChecked(
                    ItemFactory.getSelectedItemIndex(mActivatedGroupId), false);
        } else if (ViewMode == 2 && mActivatedItemListView != null) {
            mActivatedItemListView.setItemChecked(
                    ItemFactory.getSelectedItemIndex(mActivatedGroupId), false);
        }
    }

    // Move the selected item to the top of the list. The list will automatically scroll to the top,
    // and blink the first item view to notify the users.
    private void animatedSendToTop() {

        int toMoveToTopIndex = ItemFactory.getSelectedItemIndex(mActivatedGroupId);
        Item oldItem = ItemFactory.getItemList(mActivatedGroupId).get(toMoveToTopIndex);

        ItemFactory.createItem(
                mActivatedGroupId,
                oldItem.getFront(),
                oldItem.getBack(),
                oldItem.getTitle(),
                oldItem.getBookmark(),
                oldItem.getStamp(),
                oldItem.getLock()
        );

        // Interestingly, when adapter.remove() is called, the notifyDataSetChanged() method is
        // also called. Therefore, we don't need to call notifyDataSetChanged() method to notify
        // the adding of the new item.
        mActivatedItemArrayAdapter.remove(oldItem);
        ItemFactory.notifyItemDeletion(oldItem);

        // Set the listview to display to the top of the list. Refer to the following link to see
        // why I use a runnable().
        AbsListView displayView = null;
        int ViewMode = mPreference.getInt("VIEWMODE", 1);
        if (ViewMode == 1) {
            displayView = mActivatedItemGridView;
        } else if (ViewMode == 2) {
            displayView = mActivatedItemListView;
        }

        final AbsListView finalDisplayView = displayView;
        if (finalDisplayView != null) {
            finalDisplayView.post(new Runnable() {
                @Override
                public void run() {
                    // https://groups.google.com/forum/#!topic/android-developers/EnyldBQDUwE

                    // When scrolled to the top, blink the first item view
                    finalDisplayView.setOnScrollListener(new AbsListView.OnScrollListener() {

                        @Override
                        public void onScrollStateChanged(AbsListView view, int scrollState) {

                        }

                        @Override
                        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                            // When the first visible view is the top view...
                            boolean topOfFirst = finalDisplayView.getChildAt(0).getTop() == 0;
                            if (topOfFirst) {

                                final View v = finalDisplayView.getChildAt(0);
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
                                // displayView.smoothScrollToPosition(0);

                                // Cancel the OnScrollListener so that the top view won't blink during normal scroll
                                finalDisplayView.setOnScrollListener(null);
                            }
                        }
                    });

                    finalDisplayView.setSelection(0);
                }
            });
        }

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
            mActivatedItemArrayAdapter.notifyDataSetChanged();
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
        } else if (requestCode == SETTINGS_REQ) {
            if (resultCode == RESULT_OK) {

                updateSlidingTabs();
                updateDrawerItems();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }//onActivityResult
}