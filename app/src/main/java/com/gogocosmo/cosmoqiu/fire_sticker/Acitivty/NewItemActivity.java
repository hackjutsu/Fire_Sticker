package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.GroupsTableHelper;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.ItemsTableHelper;

import java.util.Random;


public class NewItemActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private Toolbar _toolbar;

    private EditText _frontSideEditText;
    private EditText _backSideEditText;
    private EditText _titleEditText;

    private Spinner _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        // Toolbar Configurations
        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _toolbar.setTitleTextColor(getResources().getColor(R.color.PURE_WHITE));

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(ItemsTableHelper.getInstance(this));
        ItemFactory.setGroupsTableHelper(GroupsTableHelper.getInstance(this));

        // Get the width of the Windows and set it as the minHeight of the card
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int cardMinHeight = displaymetrics.widthPixels - 100; // The number 100 includes the card's margins

        _frontSideEditText = (EditText) findViewById(R.id.frontSide_input_EditText);
        _frontSideEditText.setBackgroundColor(randomColor());
        _frontSideEditText.setMinHeight(cardMinHeight);

        _backSideEditText = (EditText) findViewById(R.id.backSide_input_editText);
        _backSideEditText.setBackgroundColor(randomColor());
        _backSideEditText.setMinHeight(cardMinHeight);

        _titleEditText = (EditText) findViewById(R.id.title_input_editText);

//        _cancelButton = (Button) findViewById(R.id.CancelItem);
//        _newItemButton = (Button) findViewById(R.id.AddItem);
//
//        _cancelButton.setOnClickListener(this);
//        _newItemButton.setOnClickListener(this);
//
//        _questionInput = (EditText) findViewById(R.id.editText_question);
//        _answerInput = (EditText) findViewById(R.id.editText_answer);


        _spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.new_item_spinner_rowlayout, ItemFactory.getItemGroupObjectNameList());
        dataAdapter.setDropDownViewResource(R.layout.new_item_spinner_rowlayout);
        _spinner.setAdapter(dataAdapter);
        _spinner.setSelection(getIntent().getExtras().getInt("CURRENT_TAB"));

    }

    private int randomColor() {

        Random r = new Random();
        int randomColorIndex = r.nextInt(CardColor.CardList.size() - 1 - 0 + 1) + 0;
        CardColor randomColor = CardColor.CardList.get(randomColorIndex);

//        Log.d(TAG, randomColor.getColorName());
        return randomColor.getColorInt();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_item, menu);
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
                Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            case R.id.confirm_new_item:
                addNewItem();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("UPDATED_GROUP", _spinner.getSelectedItemPosition());
                setResult(RESULT_OK, returnIntent);
                finish();
                Toast.makeText(this, "SAVE", Toast.LENGTH_SHORT).show();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    private void addNewItem() {
        String newFrontSide = _frontSideEditText.getText().toString();
        String newBackSide = _backSideEditText.getText().toString();
        String newTitle = _titleEditText.getText().toString();

        if (newTitle.isEmpty()) {
            newTitle = "";
        }

        if (newFrontSide.isEmpty()) {
            newFrontSide = "";
        }

        if (newBackSide.isEmpty()) {
            newBackSide = "";
        }

        ItemFactory.createItem(_spinner.getSelectedItemPosition(), newFrontSide, newBackSide, newTitle, 0, 0);
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(this, "CANCEL", Toast.LENGTH_SHORT).show();
        finish();
    }
}
