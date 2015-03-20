package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.DatabaseHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.STATUS_BAR_BACKGROUND));
        }

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(DatabaseHelper.getInstance(this));

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

        if (mItem.getBookmark() == 0) {
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

            case R.id.action_share_view:

                // Hide the cursors when taking a "screen shot"
                mFrontSideEditText.setCursorVisible(false);
                mBackSideEditText.setCursorVisible(false);

                // To draw a ScrollView, we need to draw the whole view instead of the visible part,
                // which means we cannot simply catch a screen shot.
                final ScrollView iv = (ScrollView) findViewById(R.id.scrollView_display);
                Bitmap bitmap = Bitmap.createBitmap(
                        iv.getChildAt(0).getWidth(),
                        iv.getChildAt(0).getHeight(),
                        Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(bitmap);
                iv.getChildAt(0).draw(c);
                saveBitmap(bitmap);

                // Show the hidden cursors
                mFrontSideEditText.setCursorVisible(true);
                mBackSideEditText.setCursorVisible(true);
                return true;

            case R.id.action_flag_view:
                if (mItem.getBookmark() == 1) {

                    mItem.setBookmark(0);
                    mBookmark.setVisibility(View.INVISIBLE);
                    CustomizedToast.showToast(this, "Clear Bookmarks");
                } else {

                    mItem.setBookmark(1);
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

    private void shareNote(String path) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);

        shareIntent.setType("image/png");
        Uri myUri = Uri.parse("file://" + path);
        shareIntent.putExtra(Intent.EXTRA_STREAM, myUri);
        startActivity(Intent.createChooser(shareIntent, "Share Notes..."));
    }

    private void saveBitmap(Bitmap bitmap) {

        // Using the time stamp as the unique name for saved image
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStamp = df.format(Calendar.getInstance().getTime());

        String filePath = Environment.getExternalStorageDirectory()
                + File.separator + "Pictures/NotesScreenShot-" + timeStamp +".png";
        File imagePath = new File(filePath);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            shareNote(filePath);
        } catch (FileNotFoundException e) {
//            Log.e(TAG, e.getMessage(), e);
        } catch (IOException e) {
//            Log.e(TAG, e.getMessage(), e);
        }
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
