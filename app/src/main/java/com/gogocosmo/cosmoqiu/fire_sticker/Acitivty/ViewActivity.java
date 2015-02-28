package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Item;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.ArrayList;
import java.util.Random;

public class ViewActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private Toolbar _toolbar;
    private EditText _frontSideEditText;
    private EditText _backSideEditText;
    private EditText _titleEditText;
    private ImageView _bookMark;
    private Menu _menu;
    private MenuItem _itemEdit;
    private MenuItem _itemConfirm;
    private MenuItem _itemBlank;
    private MenuItem _itemDelete;
    private MenuItem _itemFlag;

    private String _originFrontSide;
    private String _originBackSide;
    private String _originTitle;
    private Item _item;
    private int _groupId;

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

        _groupId = getIntent().getExtras().getInt("GROUP");
        ArrayList<Item> itemList = ItemFactory.getItemList(_groupId);

        int position = getIntent().getExtras().getInt("POSITION");
        _item = itemList.get(position);

        // Get the width of the Windows and set it as the minHeight of the card
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int cardMinHeight = displaymetrics.widthPixels - 100; // The number 100 includes the card's margins

        _frontSideEditText = (EditText) findViewById(R.id.frontSide_display_EditText);
        _frontSideEditText.setBackgroundColor(randomColor());
        _frontSideEditText.setFocusable(false);
        _frontSideEditText.setMinHeight(cardMinHeight);

        _bookMark = (ImageView) findViewById(R.id.item_display_bookmark);
        if (_item.getBookMark() == 0) {
            _bookMark.setVisibility(View.INVISIBLE);
        }

        _backSideEditText = (EditText) findViewById(R.id.backSide_display_editText);
        _backSideEditText.setBackgroundColor(randomColor());
        _backSideEditText.setFocusable(false);
        _backSideEditText.setMinHeight(cardMinHeight);

        _titleEditText = (EditText) findViewById(R.id.title_display_editText);
        _titleEditText.setFocusable(false);

        _frontSideEditText.setText(_item.getFront());
        _backSideEditText.setText(_item.getBack());
        _titleEditText.setText(_item.getTitle());

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
        _itemFlag = _menu.findItem(R.id.action_flag_view);

        _itemEdit.setVisible(true);
        _itemConfirm.setVisible(false);
        _itemBlank.setVisible(false);
        _itemDelete.setVisible(false);
        _itemFlag.setVisible(true);

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
                Intent returnIntent = new Intent();
                returnIntent.putExtra("GROUP", _groupId);
                setResult(RESULT_OK, returnIntent);
                finish();
//                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

            case R.id.action_flag_view:
                if (_item.getBookMark() == 1) {
                    _item.setBookMark(0);
                    _bookMark.setVisibility(View.INVISIBLE);
                } else {
                    _item.setBookMark(1);
                    _bookMark.setVisibility(View.VISIBLE);
                }
                return true;

            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void startEdits() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        _frontSideEditText.setFocusableInTouchMode(true);
        _backSideEditText.setFocusableInTouchMode(true);
        _titleEditText.setFocusableInTouchMode(true);
        _itemConfirm.setVisible(true);
        _itemEdit.setVisible(false);
        _itemBlank.setVisible(true);
        _itemDelete.setVisible(true);
        _itemFlag.setVisible(false);
        _toolbar.setTitle("Edit Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        _originFrontSide = _item.getFront();
        _originBackSide = _item.getBack();
        _originTitle = _item.getTitle();
    }

    private void discardEdits() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _frontSideEditText.setFocusable(false);
        _backSideEditText.setFocusable(false);
        _titleEditText.setFocusable(false);
        _itemConfirm.setVisible(false);
        _itemEdit.setVisible(true);
        _itemBlank.setVisible(false);
        _itemDelete.setVisible(false);
        _itemFlag.setVisible(true);
        _toolbar.setTitle("View Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        _frontSideEditText.setText(_originFrontSide);
        _backSideEditText.setText(_originBackSide);
        _titleEditText.setText(_originTitle);

        // hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_frontSideEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(_backSideEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(_titleEditText.getWindowToken(), 0);

    }

    private void confirmEdits() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _frontSideEditText.setFocusable(false);
        _backSideEditText.setFocusable(false);
        _titleEditText.setFocusable(false);
        _itemConfirm.setVisible(false);
        _itemEdit.setVisible(true);
        _itemBlank.setVisible(false);
        _itemDelete.setVisible(false);
        _itemFlag.setVisible(true);
        _toolbar.setTitle("View Mode");
        _toolbar.setTitleTextColor(Color.WHITE);

        // hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(_frontSideEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(_backSideEditText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(_titleEditText.getWindowToken(), 0);

        _item.setFront(_frontSideEditText.getText().toString());
        _item.setBack(_backSideEditText.getText().toString());
        _item.setTitle(_titleEditText.getText().toString());

        ItemFactory.notifyItemUpdate();
    }

    @Override
    public void onBackPressed() {

        if (_onEditMode == true) {
            discardEdits();
            _onEditMode = false;
            return;
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("GROUP", _groupId);
        setResult(RESULT_OK, returnIntent);
        finish();
//        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
