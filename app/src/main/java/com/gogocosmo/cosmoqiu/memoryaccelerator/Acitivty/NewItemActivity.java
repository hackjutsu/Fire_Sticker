package com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;


public class NewItemActivity extends Activity
        implements View.OnClickListener {

    final private String TAG = "MEMORY-ACC";

    private Button _cancelButton = null;
    private Button _newItemButton = null;
    private EditText _questionInput = null;
    private EditText _answerInput = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        _cancelButton = (Button) findViewById(R.id.CancelItem);
        _newItemButton = (Button) findViewById(R.id.AddItem);

        _cancelButton.setOnClickListener(this);
        _newItemButton.setOnClickListener(this);

        _questionInput = (EditText) findViewById(R.id.editText_question);
        _answerInput = (EditText) findViewById(R.id.editText_answer);

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.CancelItem:
                finish();
                break;
            case R.id.AddItem:
                addNewItem();
                finish();
                break;
            default:
        }
    }

    private void addNewItem() {
        String newQuestion = _questionInput.getText().toString();
        String newAnswer = _answerInput.getText().toString();

        ItemFactory.createItem(newQuestion, newAnswer);
    }
}
