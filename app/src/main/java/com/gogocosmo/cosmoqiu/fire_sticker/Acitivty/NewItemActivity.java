package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.CardColor;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemGroup;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

import java.util.Random;


public class NewItemActivity extends ActionBarActivity
        implements View.OnClickListener {

    final private String TAG = "MEMORY-ACC";

    private Toolbar _toolbar;

    private Button _cancelButton = null;
    private Button _newItemButton = null;
    private EditText _questionInput = null;
    private EditText _answerInput = null;

    private EditText _questionEditText;
    private EditText _answerEditText;
    private EditText _titleEditText;

    private Spinner _spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _toolbar.setTitle("Create a New Sticker");
        _toolbar.setTitleTextColor(getResources().getColor(R.color.PURE_WHITE));

        // Get the width of the Windows and set it as the minHeight of the card
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int cardMinHeight = displaymetrics.widthPixels - 100; // The number 100 includes the card's margins

        _questionEditText = (EditText) findViewById(R.id.question_display);
        _questionEditText.setBackgroundColor(randomColor());
        _questionEditText.setMinHeight(cardMinHeight);

        _answerEditText = (EditText) findViewById(R.id.answer_display);
        _answerEditText.setBackgroundColor(randomColor());
        _answerEditText.setMinHeight(cardMinHeight);

        _titleEditText = (EditText) findViewById(R.id.title_display);

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
                R.layout.new_item_spinner_rowlayout, ItemGroup._itemGroupList);
        dataAdapter.setDropDownViewResource(R.layout.new_item_spinner_rowlayout);
        _spinner.setAdapter(dataAdapter);
        _spinner.setSelection(getIntent().getExtras().getInt("CURRENT_TAB"));

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
                finish();
                return true;
            case R.id.confirm_new_item:
                addNewItem();
                Intent returnIntent = new Intent();
                returnIntent.putExtra("UPDATED_GROUP", _spinner.getSelectedItemPosition());
                setResult(RESULT_OK, returnIntent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            default:
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

//        switch (v.getId()) {
//            case R.id.CancelItem:
//                finish();
//                break;
//            case R.id.AddItem:
//                addNewItem();
//                finish();
//                break;
//            default:
//        }
    }

    private void addNewItem() {
        String newQuestion = _questionEditText.getText().toString();
        String newAnswer = _answerEditText.getText().toString();
        String newTitle = _titleEditText.getText().toString();
        if (newTitle.isEmpty()) {
            newTitle = "";
        }

        ItemFactory.createItem(newQuestion, newAnswer, newTitle);
    }
}
