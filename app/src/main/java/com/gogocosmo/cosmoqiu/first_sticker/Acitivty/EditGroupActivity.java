package com.gogocosmo.cosmoqiu.first_sticker.Acitivty;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.gogocosmo.cosmoqiu.first_sticker.Model.ItemGroup;
import com.gogocosmo.cosmoqiu.first_sticker.R;

import java.util.ArrayList;

public class EditGroupActivity extends ActionBarActivity {

    final private String TAG = "MEMORY-ACC";

    private Toolbar _toolbar;
    private Button _addButton;
    private Button _submitButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ListView listview = (ListView) findViewById(R.id.listview);

        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, ItemGroup._itemGroupList);
        listview.setAdapter(adapter);

        _addButton = (Button) findViewById(R.id.AddGroup);
        _addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ItemGroup._itemGroupList.add("TAB " + String.valueOf(ItemGroup._itemGroupList.size()));
                adapter.notifyDataSetChanged();
            }
        });

        _submitButton = (Button) findViewById(R.id.SubmitGroupChanges);
        _submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent returnIntent = new Intent();
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_group, menu);
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
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

}
