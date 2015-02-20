package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.Random;

public class ViewActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private Toolbar _toolbar;
    private EditText _questionEditText;
    private EditText _answerEditText;
    private Menu _menu;
    private MenuItem _itemEdit;
    private MenuItem _itemConfirm;
    private MenuItem _itemBlank;
    private MenuItem _itemDelete;

    private String _originQuestion;
    private String _originAnswer;
    private Item _item;

    private boolean _onEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _toolbar.setTitle("View Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        int position = getIntent().getExtras().getInt("POSITION");
        _item = ItemFactory.getItemList().get(position);

        // Get the width of the Windows and set it as the minHeight of the card
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int cardMinHeight = displaymetrics.widthPixels - 100; // The number 100 includes the card's margins

        _questionEditText = (EditText) findViewById(R.id.question_display);
        _questionEditText.setBackgroundColor(randomColor());
        _questionEditText.setFocusable(false);
        _questionEditText.setMinHeight(cardMinHeight);

        _answerEditText = (EditText) findViewById(R.id.answer_display);
        _answerEditText.setBackgroundColor(randomColor());
        _answerEditText.setFocusable(false);
        _answerEditText.setMinHeight(cardMinHeight);

        _questionEditText.setText(_item.getQuestion());
        _answerEditText.setText(_item.getAnswer());

        _onEditMode = false;
    }

    private int randomColor() {

        Random r = new Random();
        int randomColorIndex = r.nextInt(CardColor.CardList.size() - 1 - 0 + 1) + 0;
        CardColor randomColor = CardColor.CardList.get(randomColorIndex);

        Log.d(TAG, randomColor.getColorName());
        return randomColor.getColorInt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);

        _menu = menu;

        _itemEdit = _menu.findItem(R.id.action_edit_view);
        _itemConfirm = _menu.findItem(R.id.action_confirm_view);
        _itemBlank = _menu.findItem(R.id.action_blank_view);
        _itemDelete = _menu.findItem(R.id.action_discard_view);

        _itemEdit.setVisible(true);
        _itemConfirm.setVisible(false);
        _itemBlank.setVisible(false);
        _itemDelete.setVisible(false);

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
                _onEditMode = false;
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;

            case R.id.action_edit_view:
                _onEditMode = true;
                startEdits();
                return true;

            case R.id.action_confirm_view:
                _onEditMode = false;
                confirmEdits();
                return true;

            case R.id.action_discard_view:
                _onEditMode = false;
                discardEdits();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startEdits() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        _questionEditText.setFocusableInTouchMode(true);
        _answerEditText.setFocusableInTouchMode(true);
        _itemConfirm.setVisible(true);
        _itemEdit.setVisible(false);
        _itemBlank.setVisible(true);
        _itemDelete.setVisible(true);
        _toolbar.setTitle("Edit Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        _originQuestion = _item.getQuestion();
        _originAnswer = _item.getAnswer();
    }

    private void discardEdits() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _questionEditText.setFocusable(false);
        _answerEditText.setFocusable(false);
        _itemConfirm.setVisible(false);
        _itemEdit.setVisible(true);
        _itemBlank.setVisible(false);
        _itemDelete.setVisible(false);
        _toolbar.setTitle("View Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        _item.setQuestion(_originQuestion);
        _item.setAnswer(_originAnswer);
        _questionEditText.setText(_originQuestion);
        _answerEditText.setText(_originAnswer);

        // hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_questionEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(_answerEditText.getWindowToken(), 0);
    }

    private void confirmEdits() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _questionEditText.setFocusable(false);
        _answerEditText.setFocusable(false);
        _itemConfirm.setVisible(false);
        _itemEdit.setVisible(true);
        _itemBlank.setVisible(false);
        _itemDelete.setVisible(false);
        _toolbar.setTitle("View Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        // hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_questionEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(_answerEditText.getWindowToken(), 0);

        _item.setQuestion(_questionEditText.getText().toString());
        _item.setAnswer(_answerEditText.getText().toString());
    }

    @Override
    public void onBackPressed() {

        if (_onEditMode == true) {
            discardEdits();
        }
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
