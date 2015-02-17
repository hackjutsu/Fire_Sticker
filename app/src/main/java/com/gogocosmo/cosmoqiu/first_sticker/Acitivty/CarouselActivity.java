package com.gogocosmo.cosmoqiu.first_sticker.Acitivty;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gogocosmo.cosmoqiu.first_sticker.Adapter.CardHolder;
import com.gogocosmo.cosmoqiu.first_sticker.Fragment.CardFragment;
import com.gogocosmo.cosmoqiu.first_sticker.Fragment.TopFragment;
import com.gogocosmo.cosmoqiu.first_sticker.GesturesListener.OnMultiGesturesListener;
import com.gogocosmo.cosmoqiu.first_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.first_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.first_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.first_sticker.R;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

public class CarouselActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";
    private ArrayList<CardHolder> _cardTrace;
    private int _itemIndex;
    private TopFragment _topFragment;
    private CardFragment _currentCard;

    Toolbar _toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carousel);

        // Creating The Toolbar and setting it as the Toolbar for the activity

        _toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(_toolbar);

        _topFragment = new TopFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.topFragmentContainer, _topFragment)
                .hide(_topFragment)
                .commit();

        _cardTrace = new ArrayList<>();
        _itemIndex = -1;

        FrameLayout layout = (FrameLayout) findViewById(R.id.fragmentContainer);
        layout.setOnTouchListener(new OnMultiGesturesListener(this) {
            @Override
            public void onSwipeLeft() {
                if (_topFragment.isHidden()) {
                    nextCard();
                }
            }

            @Override
            public void onSwipeRight() {
                if (_topFragment.isHidden()) {
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
                Toast.makeText(CarouselActivity.this, "Double Tapped ~", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton returnButton = (ImageButton) findViewById(R.id.ReturnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishCarousel();
            }
        });


        nextCard();


    }

    void swipeUpEvent() {
//        CardFragment currentCard = (CardFragment) getCurrentFragment();
        if (_currentCard != null) {
            if (_topFragment.isHidden()) {

                getSupportFragmentManager()
                        .beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.abc_slide_in_top,
//                                R.anim.abc_slide_out_top
//                        )
                        .show(_topFragment)
                        .commit();

                _currentCard.swipeUpEvent(
                        ItemFactory.getItemList().get(_itemIndex).getAnswer());
            }
        }
    }

    void swipeDownEvent() {
//        CardFragment currentCard = (CardFragment) getCurrentFragment();
        if (_currentCard != null) {
            if (!_topFragment.isHidden()) {
                getSupportFragmentManager()
                        .beginTransaction()
//                        .setCustomAnimations(
//                                R.anim.abc_slide_in_top,
//                                R.anim.abc_slide_out_top
//                        )
                        .hide(_topFragment)
                        .commit();
                _currentCard.swipeDownEvent(ItemFactory.getItemList().get(_itemIndex).getQuestion());
            }
        }
    }

    private int randomColor() {

        Random r = new Random();
        int randomColorIndex = r.nextInt(CardColor.CardList.size() - 1 - 0 + 1) + 0;
        CardColor randomColor = CardColor.CardList.get(randomColorIndex);

        Log.d(TAG, randomColor.getColorName());
//        Toast.makeText(this, randomColor.getColorName(), Toast.LENGTH_SHORT).show();
        return randomColor.getColorInt();
    }


    private void nextCard() {

        _itemIndex++;

        // Return on invalid card index
        if (_itemIndex >= ItemFactory.getItemList().size()) {
            _itemIndex--;

            Toast.makeText(this, "This is the last card ~", Toast.LENGTH_SHORT).show();
            return;
        }
        ;

        Item item = ItemFactory.getItemList().get(_itemIndex);

        // Set up a new card with a new color
        CardFragment newCard = new CardFragment();
        int cardColor = randomColor();
        newCard.setCardColor(cardColor);
        newCard.setCardText(item.getQuestion());

        // Add this new card info to card trace
        _cardTrace.add(new CardHolder(cardColor, item));
        _currentCard = newCard;

        // Add the new card to the foreground
        String id = UUID.randomUUID().toString();

        if (_itemIndex == 0) {
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
    }

    private void previousCard() {

        int lastIndex = _cardTrace.size() - 1;

        // Return if the array size is 0 or 1
        if (lastIndex <= 0) {

            Toast.makeText(this, "This is the first card ~", Toast.LENGTH_SHORT).show();
            return;
        }

        _itemIndex--;

        // Remove the last card in the card trace
        _cardTrace.remove(lastIndex);
        CardHolder currentHolder = _cardTrace.get(lastIndex - 1);

        // Create a new card fragment that restores the previous card info
        CardFragment preCard = new CardFragment();
        preCard.setCardColor(currentHolder.getColor());
        preCard.setCardText(currentHolder.getItem().getQuestion());
        _currentCard = preCard;

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

//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ftransaction = fm.beginTransaction();
//
//        if (fm.getBackStackEntryCount() > 1) {
//            fm.popBackStack();
//            ftransaction.commit();
//        }
    }

    private Fragment getCurrentFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Log.d(TAG, "fragmentTag = " + fragmentTag);
        Log.d(TAG, "BackStackEntryCount() = " + fragmentManager.getBackStackEntryCount());
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        return currentFragment;
    }

    @Override
    public void onBackPressed() {
        finishCarousel();
    }

    private void finishCarousel() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
