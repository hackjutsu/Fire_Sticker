package com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter.ViewPagerAdapter;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;
import com.gogocosmo.cosmoqiu.memoryaccelerator.library.SlidingTabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class TabActivity extends ActionBarActivity {

    static {

        String[] questionSamples = new String[]{
                "How many rings on the Olympic flag?",
                "What is the currency of Austria?",
                "How did Alfred Nobel make his money?",
                "Which car company makes the Celica?",
                "What is a baby rabbit called?",
                "What is a Winston Churchill?",
                "What plant does the Colorado beetle attack?",
                "Who wrote the Opera Madam Butterfly?",
                "Which country do Sinologists study?",
                "What was the first James Bond film?",
                "How many rings on the Olympic flag?",
                "What is the currency of Austria?",
                "How did Alfred Nobel make his money?",
                "Which car company makes the Celica?",
                "What is a baby rabbit called?",
                "What is a Winston Churchill?",
                "What plant does the Colorado beetle attack?",
                "Who wrote the Opera Madam Butterfly?",
                "Which country do Sinologists study?",
                "What was the first James Bond film?"
        };

        String[] answerSamples = new String[]{
                "Five",
                "Schilling",
                "He invented Dynamite",
                "Toyota",
                "Kit or Kitten",
                "Cigar",
                "Potato",
                "Puccini",
                "China",
                "Dr No",
                "Five",
                "Schilling",
                "He invented Dynamite",
                "Toyota",
                "Kit or Kitten",
                "Cigar",
                "Potato",
                "Puccini",
                "China",
                "Dr No"
        };

        for (int i = 0; i < 20; ++i) {
            ItemFactory.createItem(questionSamples[i], answerSamples[i]);
        }
    }

    // Declaring Your View and Variables
    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[] = {"Group1", "Group2", "Group3"};
    ArrayList<String> _titles;
    int Numboftabs = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);


        // Creating The Toolbar and setting it as the Toolbar for the activity

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        _titles = new ArrayList<>();
        _titles.add("AAA");
        _titles.add("BBB");
        _titles.add("CCC");


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), _titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        if (Numboftabs <= 3) {
            tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        }

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        // Setting

        final ImageButton letsGoButton = (ImageButton) findViewById(R.id.LetsGoButton);
        letsGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                // Codes to dynamically set current Tab
//                tabs.setCurrentTab(1);


//                // Codes to dynamically add new Tabs
//                adapter.addNewTab("New");
//                if (Numboftabs <= 5) {
//                    tabs.setDistributeEvenly(true);
//                } else {
//                    tabs.setDistributeEvenly(false);
//                }
//                tabs.setViewPager(pager);


                Intent intent = new Intent(TabActivity.this, CarouselActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, NewItemActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
