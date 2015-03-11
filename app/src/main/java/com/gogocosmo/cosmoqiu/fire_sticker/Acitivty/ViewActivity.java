package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.GroupsTableHelper;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.ItemsTableHelper;

import java.util.ArrayList;
import java.util.Random;

public class ViewActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private Toolbar mToolbar;
    private EditText mFrontSideEditText;
    private EditText mBackSideEditText;
    private EditText mTitleEditText;
    private ImageView mBookmark;
    private ImageView mStampFront;
    private ImageView mStampBack;
    private LinearLayout mCardsContainer;

    private Item mItem;
    private int mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(ItemsTableHelper.getInstance(this));
        ItemFactory.setGroupsTableHelper(GroupsTableHelper.getInstance(this));

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle("Edit Notes");
        mToolbar.setTitleTextColor(Color.WHITE);

        mGroupId = getIntent().getExtras().getInt("GROUP");
        ArrayList<Item> itemList = ItemFactory.getItemList(mGroupId);

        int position = getIntent().getExtras().getInt("POSITION");
        mItem = itemList.get(position);

        // Get the width of the Windows and set it as the minHeight of the card
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int cardMinHeight = displaymetrics.widthPixels - 100; // The number 100 includes the card's margins

        int cardPadding = 50;

        mFrontSideEditText = (EditText) findViewById(R.id.frontSide_display_EditText);
        mFrontSideEditText.setBackgroundColor(randomColor());
        mFrontSideEditText.setMinHeight(cardMinHeight);
        mFrontSideEditText.setPadding(cardPadding, cardPadding, cardPadding, cardPadding);

        mBookmark = (ImageView) findViewById(R.id.item_display_bookmark);
        mStampFront = (ImageView) findViewById(R.id.item_card_front_done);
        mStampBack = (ImageView) findViewById(R.id.item_card_back_done);

        if (mItem.getBookMark() == 0) {
            mBookmark.setVisibility(View.INVISIBLE);
        }

        if (mItem.getStamp() == 0) {
            mStampFront.setVisibility(View.INVISIBLE);
            mStampBack.setVisibility(View.INVISIBLE);
        }

        mBackSideEditText = (EditText) findViewById(R.id.backSide_display_editText);
        mBackSideEditText.setBackgroundColor(randomColor());
        mBackSideEditText.setMinHeight(cardMinHeight);
        mBackSideEditText.setPadding(cardPadding, cardPadding, cardPadding, cardPadding);


        mTitleEditText = (EditText) findViewById(R.id.title_display_editText);

        mFrontSideEditText.setText(mItem.getFront());
        mBackSideEditText.setText(mItem.getBack());
        mTitleEditText.setText(mItem.getTitle());

        adjustCardTextFormat();

        // Hide the soft keyboard when tapping the white boarders
        mCardsContainer = (LinearLayout)findViewById(R.id.linear_cards_display);
        mCardsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mFrontSideEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mBackSideEditText.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(mTitleEditText.getWindowToken(), 0);
            }
        });
    }

    private int randomColor() {

        Random r = new Random();
        int randomColorIndex = r.nextInt(CardColor.COLOR_LIST.size() - 1 - 0 + 1) + 0;
        CardColor randomColor = CardColor.COLOR_LIST.get(randomColorIndex);

        return randomColor.getColorInt();
    }

    private void adjustCardTextFormat() {
        // If the line count in an EditText more than two, the texts should start from left;
        // else we put it in the center.

        mFrontSideEditText.post(new Runnable() {
            @Override
            public void run() {
                int lineCountFront = mFrontSideEditText.getLineCount();

                if (lineCountFront <= 1) {
                    mFrontSideEditText.setGravity(Gravity.CENTER);
                } else {
                    mFrontSideEditText.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
        });

        mBackSideEditText.post(new Runnable() {
            @Override
            public void run() {
                int lineCountFront = mBackSideEditText.getLineCount();

                if (lineCountFront <= 1) {
                    mBackSideEditText.setGravity(Gravity.CENTER);
                } else {
                    mBackSideEditText.setGravity(Gravity.CENTER_VERTICAL);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                confirmEdits();
                CustomizedToast.showToast(this, "SAVE");

                Intent returnIntent = new Intent();
                returnIntent.putExtra("GROUP", mGroupId);
                setResult(RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            case R.id.action_flag_view:
                if (mItem.getBookMark() == 1) {

                    mItem.setBookMark(0);
                    mBookmark.setVisibility(View.INVISIBLE);
                    CustomizedToast.showToast(this, "Clear Bookmarks");
                } else {

                    mItem.setBookMark(1);
                    mBookmark.setVisibility(View.VISIBLE);
                    CustomizedToast.showToast(this, "Bookmarked");
                }

                ItemFactory.notifyItemUpdate(ItemFactory.getItemGroupObjectList().get(mGroupId), mItem);
                return true;

            case R.id.action_stamp_view:
                if (mItem.getStamp() == 1) {

                    mItem.setStamp(0);
                    mStampFront.setVisibility(View.INVISIBLE);
                    mStampBack.setVisibility(View.INVISIBLE);
                    CustomizedToast.showToast(this, "Clear Stamps");
                } else {

                    mItem.setStamp(1);
                    mStampFront.setVisibility(View.VISIBLE);
                    mStampBack.setVisibility(View.VISIBLE);
                    CustomizedToast.showToast(this, "Stamped");
                }

                ItemFactory.notifyItemUpdate(ItemFactory.getItemGroupObjectList().get(mGroupId), mItem);
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirmEdits() {

        mItem.setFront(mFrontSideEditText.getText().toString());
        mItem.setBack(mBackSideEditText.getText().toString());
        mItem.setTitle(mTitleEditText.getText().toString());

        ItemFactory.notifyItemUpdate(ItemFactory.getItemGroupObjectList().get(mGroupId), mItem);
    }

    @Override
    public void onBackPressed() {

        confirmEdits();
        CustomizedToast.showToast(this, "SAVE");

        Intent returnIntent = new Intent();
        returnIntent.putExtra("GROUP", mGroupId);
        setResult(RESULT_OK, returnIntent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
