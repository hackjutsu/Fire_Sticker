package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.PasswordManager;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.DatabaseHelper;

import java.util.Random;


public class NewItemActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private Toolbar mToolbar;

    private EditText mFrontSideEditText;
    private EditText mBackSideEditText;
    private EditText mTitleEditText;
    private LinearLayout mCardsContainer;

    private Spinner mSpinner;
    private MenuItem mLock;
    private MenuItem mLocked;

    private int mLockIndicator = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.STATUS_BAR_BACKGROUND));
        }

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.PURE_WHITE));

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(DatabaseHelper.getInstance(this));

        // Get the width of the Windows and set it as the minHeight of the card
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int cardMinHeight = displaymetrics.widthPixels;

        mFrontSideEditText = (EditText) findViewById(R.id.frontSide_input_EditText);
        mFrontSideEditText.setBackgroundColor(randomColor());
        mFrontSideEditText.setMinHeight(cardMinHeight);

        mBackSideEditText = (EditText) findViewById(R.id.backSide_input_editText);
        mBackSideEditText.setBackgroundColor(randomColor());
        mBackSideEditText.setMinHeight(cardMinHeight);

        mTitleEditText = (EditText) findViewById(R.id.title_input_editText);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.new_item_spinner_rowlayout, ItemFactory.getItemGroupObjectNameList());
        dataAdapter.setDropDownViewResource(R.layout.new_item_spinner_rowlayout);
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setSelection(getIntent().getExtras().getInt("CURRENT_TAB"));

        // Hide the soft keyboard when tapping the white boarders
        mCardsContainer = (LinearLayout) findViewById(R.id.linear_cards_input);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_item, menu);

        mLock = menu.findItem(R.id.action_lock_view);
        mLocked = menu.findItem(R.id.action_locked_view);

        mLocked.setVisible(false);
        mLock.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        switch (id) {
            case android.R.id.home:
                CustomizedToast.showToast(this, "CANCEL");
                finish();
                return true;
            case R.id.confirm_new_item:
                addNewItem();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("UPDATED_GROUP", mSpinner.getSelectedItemPosition());
                setResult(RESULT_OK, returnIntent);
                finish();
                CustomizedToast.showToast(this, "SAVE");
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.action_lock_view:
                if (!PasswordManager.isPasswordTurnedOn(this)) {
                    PasswordManager.showRequirePasswordSetupDialog(this);
                    return false;
                }
                mLocked.setVisible(true);
                mLock.setVisible(false);
                mLockIndicator = 1;
                return true;
            case R.id.action_locked_view:
                mLocked.setVisible(false);
                mLock.setVisible(true);
                mLockIndicator = 0;
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewItem() {
        String newFrontSide = mFrontSideEditText.getText().toString();
        String newBackSide = mBackSideEditText.getText().toString();
        String newTitle = mTitleEditText.getText().toString();

        if (newTitle.isEmpty()) {
            newTitle = "";
        }

        if (newFrontSide.isEmpty()) {
            newFrontSide = "";
        }

        if (newBackSide.isEmpty()) {
            newBackSide = "";
        }

        ItemFactory.createItem(
                mSpinner.getSelectedItemPosition(),
                newFrontSide,
                newBackSide,
                newTitle,
                0,
                0,
                mLockIndicator);
    }

    @Override
    public void onBackPressed() {

        CustomizedToast.showToast(this, "CANCEL");
        finish();
    }
}
