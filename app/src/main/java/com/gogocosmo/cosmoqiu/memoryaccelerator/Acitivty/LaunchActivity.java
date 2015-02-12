package com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;


public class LaunchActivity extends ActionBarActivity
        implements View.OnClickListener {

    final private String TAG = "MEMORY-ACC";

    private ImageButton _startButton = null;
    private ImageButton _addButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        _startButton = (ImageButton) findViewById(R.id.StartImageButton);
        _addButton = (ImageButton) findViewById(R.id.AddImageButton);

        _startButton.setOnClickListener(this);
        _addButton.setOnClickListener(this);

        String[] samples = new String[]{
                "Android", "iPhone", "WinPhone",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WinPhone"};

        for (int i=0; i< 20; ++i) {
            ItemFactory.createItem(samples[i], samples[20 - i]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
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
            case R.id.StartImageButton:
                Intent startIntent = new Intent(this, ListViewActivity.class);
                startActivity(startIntent);
                break;
            case R.id.AddImageButton:
                Intent newItemIntent = new Intent(this, NewItemActivity.class);
                startActivity(newItemIntent);
                break;
            default:
        }
    }
}
