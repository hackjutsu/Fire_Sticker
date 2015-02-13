package com.gogocosmo.cosmoqiu.memoryaccelerator.Acitivty;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.gogocosmo.cosmoqiu.memoryaccelerator.Adapter.ItemArrayAdapter;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.Item;
import com.gogocosmo.cosmoqiu.memoryaccelerator.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.memoryaccelerator.R;

public class ListViewActivity extends ActionBarActivity
        implements ActionMode.Callback {

    final private String TAG = "MEMORY-ACC";

    protected Object mActionMode;
    public int selectedItem = -1;
    private ListView listView;
    private View _selectedView;

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
                "Dr No"
        };

        for (int i=0; i< 10; ++i) {
            ItemFactory.createItem(questionSamples[i], answerSamples[i]);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

//        // Setting up the _slidingTabsLayout
//        final ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//
//        // Create a tab listener that is called when the user changes _slidingTabsLayout.
//        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
//
//            @Override
//            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
//
//            }
//
//            @Override
//            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
//
//            }
//
//            @Override
//            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
//
//            }
//        };
//
//
//        // Add 3 _slidingTabsLayout, specifying the tab's text and TabListener
//        for (int i = 0; i < 3; i++) {
//            actionBar.addTab(
//                    actionBar.newTab()
//                            .setText("Tab " + (i + 1))
//                            .setTabListener(tabListener));
//        }




        //////////////////// Normal Codes /////////////////////////////

        listView = (ListView) findViewById(R.id.listview);

        final ItemArrayAdapter mySimpleArrayAdapter = new ItemArrayAdapter(this,
                ItemFactory.getItemList());
        listView.setAdapter(mySimpleArrayAdapter);

//        final Button letsGoButton = (Button) findViewById(R.id.LetsGoButton);
//        letsGoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.startAnimation(new AlphaAnimation(1.0f, 0.5f));
////                Intent intent = new Intent(ListViewActivity.this, CarouselActivity.class);
//                Intent intent = new Intent(ListViewActivity.this, LaunchActivity.class);
//                startActivity(intent);
//            }
//        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Item item = (Item) parent.getItemAtPosition(position);

                if (mActionMode != null) {
                    return false;
                }

                if (_selectedView != null) {
                    _selectedView.setBackgroundColor(Color.WHITE);
                }
                selectedItem = position;
                _selectedView = view;
//                view.setBackgroundColor(Color.rgb(227, 239, 209));

                // Start the CAB using the ActionMode.Callback defined above
                ListViewActivity.this.startActionMode(ListViewActivity.this);
                view.setSelected(true);

                Toast.makeText(ListViewActivity.this,
                        "Long Click " + item.getQuestion(),
                        Toast.LENGTH_SHORT).show();

                return true;
            }
        });
    }

    private void deleteItem() {

        _selectedView.animate().setDuration(1000).alpha(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        ItemArrayAdapter adater = (ItemArrayAdapter) listView.getAdapter();
                        adater.remove(adater.getItem(selectedItem));
                        adater.notifyDataSetChanged();
                        _selectedView.setAlpha(1);
                        _selectedView.setBackgroundColor(Color.parseColor("#FFFFFFFF"));
                        _selectedView = null;
                        selectedItem = -1;
                    }
                });
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "onCreateActionMode");
        // Inflate a menu resource providing context menu items
        MenuInflater inflater = mode.getMenuInflater();
        // Assumes that you have "contexual.xml" menu resources
        inflater.inflate(R.menu.rowselection, menu);

        if (_selectedView != null) {
            _selectedView.setBackgroundColor(Color.rgb(227, 239, 209));
        }
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.d(TAG, "onPrepareActionMode");

        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.d(TAG, "onActionItemClicked");

        switch (item.getItemId()) {
            case R.id.menuItemDelete:
                Toast.makeText(ListViewActivity.this,
                        "Action DELETE",
                        Toast.LENGTH_SHORT).show();
                // Action picked, so close the CAB
                deleteItem();
                mode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.d(TAG, "onDestroyActionMode");
        if (_selectedView != null) {
            _selectedView.setBackgroundColor(Color.WHITE);
        }
    }
}
