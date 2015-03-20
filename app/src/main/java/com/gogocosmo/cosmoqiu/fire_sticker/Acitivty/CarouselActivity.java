package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.CardHolder;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.CardFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.Fragment.TopFragment;
import com.gogocosmo.cosmoqiu.fire_sticker.GesturesListener.OnMultiGesturesListener;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.DatabaseHelper;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class CarouselActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private ArrayList<CardHolder> mCardTrace;
    private int mItemIndex;
    private TopFragment mTopFragment;
    private CardFragment mCurrentCard;

    private Toolbar mToolbar;
    private TextView mTitle;
    private int mGroupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.STATUS_BAR_BACKGROUND));
        }

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(DatabaseHelper.getInstance(this));

        mGroupId = getIntent().getExtras().getInt("GROUP");

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTitle = (TextView) findViewById(R.id.toolbar_title);

        mTopFragment = new TopFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topFragmentContainer, mTopFragment)
                .hide(mTopFragment)
                .commit();

        mCardTrace = new ArrayList<>();
        mItemIndex = -1;

        FrameLayout layout = (FrameLayout) findViewById(R.id.fragmentContainer);
        layout.setOnTouchListener(new OnMultiGesturesListener(this) {
            @Override
            public void onSwipeLeft() {
                if (mTopFragment.isHidden()) {
                    nextCard();
                }
            }

            @Override
            public void onSwipeRight() {
                if (mTopFragment.isHidden()) {
                    previousCard();

                }
            }

            @Override
            public void onSwipeUp() {
                swipeUpEvent();
            }

            @Override
            public void onSwipeDown() {
                swipeDownEvent();
            }

            @Override
            public void onDoubleTapped() {
//                Toast.makeText(CarouselActivity.this, "Double Tapped ~", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton returnButton = (ImageButton) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);

                finishCarousel();
            }
        });
        nextCard();
    }

    void swipeUpEvent() {

        if (mCurrentCard != null) {
            if (mTopFragment.isHidden()) {

                getSupportFragmentManager()
                        .beginTransaction()
                        .show(mTopFragment)
                        .commit();

                mCurrentCard.swipeUpEvent(
                        ItemFactory.getItemList(mGroupId).get(mItemIndex).getBack());
            }
        }
    }

    void swipeDownEvent() {

        if (mCurrentCard != null) {
            if (!mTopFragment.isHidden()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(mTopFragment)
                        .commit();
                mCurrentCard.swipeDownEvent(ItemFactory.getItemList(mGroupId).get(mItemIndex).getFront());
            }
        }
    }

    private int randomColor() {

        Random r = new Random();
        int randomColorIndex = r.nextInt(CardColor.COLOR_LIST.size() - 1 - 0 + 1) + 0;
        CardColor randomColor = CardColor.COLOR_LIST.get(randomColorIndex);
        return randomColor.getColorInt();
    }

    private void nextCard() {

        mItemIndex++;

        // Return on invalid card index
        if (mItemIndex >= ItemFactory.getItemList(mGroupId).size()) {
            mItemIndex--;

            Toast.makeText(this, "This is the last card ~", Toast.LENGTH_SHORT).show();
            return;
        }

        Item item = ItemFactory.getItemList(mGroupId).get(mItemIndex);

        // Set up a new card with a new color
        CardFragment newCard = new CardFragment();
        int cardColor = randomColor();
        newCard.setCardColor(cardColor);
        newCard.setCardText(item.getFront());
        newCard.setMarked(item.getBookmark());
        newCard.setStamp(item.getStamp());

        // Add this new card info to card trace
        mCardTrace.add(new CardHolder(cardColor, item));
        mCurrentCard = newCard;

        // Add the new card to the foreground
        String id = UUID.randomUUID().toString();

        if (mItemIndex == 0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, newCard, id)
                    .addToBackStack(id)
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            R.anim.enter_from_right,
                            R.anim.exit_to_left)
                    .replace(R.id.fragmentContainer, newCard, id)
                    .addToBackStack(id)
                    .commit();
        }
        mTitle.setText(item.getTitle());
    }

    private void previousCard() {

        int lastIndex = mCardTrace.size() - 1;

        // Return if the array size is 0 or 1
        if (lastIndex <= 0) {

            Toast.makeText(this, "This is the first card ~", Toast.LENGTH_SHORT).show();
            return;
        }

        mItemIndex--;

        // Remove the last card in the card trace
        mCardTrace.remove(lastIndex);
        CardHolder currentHolder = mCardTrace.get(lastIndex - 1);

        // Create a new card fragment that restores the previous card info
        CardFragment preCard = new CardFragment();
        preCard.setCardColor(currentHolder.getColor());
        preCard.setCardText(currentHolder.getItem().getFront());
        preCard.setMarked(currentHolder.getItem().getBookmark());

        mCurrentCard = preCard;

        String id = UUID.randomUUID().toString();

        // Show up the "previosu" card
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.anim.enter_from_left,
                        R.anim.exit_to_right)
                .replace(R.id.fragmentContainer, preCard, id)
                .addToBackStack(id)
                .commit();

        mTitle.setText(currentHolder.getItem().getTitle());
    }

    private void finishCarousel() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        finishCarousel();
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
                finishCarousel();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
