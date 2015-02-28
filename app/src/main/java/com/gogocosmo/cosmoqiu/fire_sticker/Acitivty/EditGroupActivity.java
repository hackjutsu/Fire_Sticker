package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Adapter.GroupArrayAdapter;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

public class EditGroupActivity extends ActionBarActivity implements
        android.view.ActionMode.Callback {

    final private String TAG = "MEMORY-ACC";

    private Toolbar _toolbar;
    private ListView _listView;
    private GroupArrayAdapter _adapter;

    private android.view.ActionMode _actionMode;
    private Menu _menu;
    private int _selectedIndex;

    private TextView _totalStickerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        _toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _toolbar.setTitleTextColor(getResources().getColor(R.color.PURE_WHITE));

        _listView = (ListView) findViewById(R.id.listview);
        _adapter = new GroupArrayAdapter(this, ItemFactory.getItemGroupObjectList());
        _listView.setAdapter(_adapter);
        _listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Important method: set the list item in this position activated.
                // No matter whether this view is recycled or not.
                _listView.setItemChecked(position, true);
                _selectedIndex = position;
                startActionMode(EditGroupActivity.this);
                return true;
            }
        });

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _selectedIndex = position;

                if (_actionMode != null) {
                    return;
                }

                startEditDialog();
                _listView.setItemChecked(_selectedIndex, true);
            }
        });

        _totalStickerNum = (TextView) findViewById(R.id.total_sticker_num);
        updateTotalStickerNum();
    }

    private void updateTotalStickerNum() {

        int totalGroupNum = ItemFactory.getItemGroupObjectList().size();
        int totalStickers = 0;

        for (int i = 0; i < totalGroupNum; ++i) {
            totalStickers += ItemFactory.getItemList(i).size();
        }

        _totalStickerNum.setText(String.valueOf(totalStickers));
    }

    private void startEditDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_group);

        dialog.setCanceledOnTouchOutside(false);

        ImageView cancelImage = (ImageView) dialog.findViewById(R.id.cancel_dialog);
        ImageView confirmImage = (ImageView) dialog.findViewById(R.id.confirm_dialog);
        final EditText groupName = (EditText) dialog.findViewById(R.id.editText_groupName);

        String originalTitle = ItemFactory.getItemGroupObjectList().get(_selectedIndex).getGroupName();
        groupName.setText(originalTitle);

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = groupName.getText().toString();
//                ItemFactory.getItemGroupList().set(_selectedIndex, newTitle);
                ItemFactory.getItemGroupObjectList().get(_selectedIndex).setGroupName(newTitle);
                _adapter.notifyDataSetChanged();
                updateTotalStickerNum();
                dialog.dismiss();
            }
        });

        dialog.show();

        WindowManager.LayoutParams paramsWindow = dialog.getWindow().getAttributes();
        paramsWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        paramsWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(paramsWindow);
    }

    private void startNewItemDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_group);

        dialog.setCanceledOnTouchOutside(false);

        ImageView cancelImage = (ImageView) dialog.findViewById(R.id.cancel_dialog);
        ImageView confirmImage = (ImageView) dialog.findViewById(R.id.confirm_dialog);
        final EditText groupName = (EditText) dialog.findViewById(R.id.editText_groupName);

        groupName.setText("");

        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        confirmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newTitle = groupName.getText().toString();

                if (newTitle.isEmpty()) {

                    newTitle = "NEW GROUP";
                }

                ItemFactory.createGroup(newTitle);
                _adapter.notifyDataSetChanged();
                updateTotalStickerNum();
                _selectedIndex = ItemFactory.getItemGroupObjectList().size() - 1;
                _listView.setItemChecked(ItemFactory.getItemGroupObjectList().size() - 1, true);

                dialog.dismiss();
            }
        });

        dialog.show();

        WindowManager.LayoutParams paramsWindow = dialog.getWindow().getAttributes();
        paramsWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        paramsWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(paramsWindow);
    }

    private void deleteGroup() {

        ItemFactory.getItemLists().remove(_selectedIndex);
        ItemFactory.getItemGroupObjectList().remove(_selectedIndex);
        ItemFactory.getSelectedItemIndexesList().remove(_selectedIndex);
        _adapter.notifyDataSetChanged();

        // Notify Database the Group Deletion
        ItemFactory.notifyGroupDeletion(_selectedIndex);

        _actionMode.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_group, menu);
        _menu = menu;

        _toolbar.setTitle("Group List");

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemSlash.setVisible(false);

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

                if (_actionMode != null) {
                    _actionMode.finish();
                } else {
                    Intent returnIntent = new Intent();
                    setResult(RESULT_OK, returnIntent);
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
                return true;
            case R.id.action_add:

                startNewItemDialog();
                return true;
            case R.id.action_delete:

                deleteGroup();
                _listView.setItemChecked(_selectedIndex, false);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        _toolbar.setTitle("Edit Mode");

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

        itemAdd.setVisible(false);
        itemBack.setVisible(false);
        itemDelete.setVisible(true);
        itemSlash.setVisible(false);

        _actionMode = mode;

        // Return false since we are using a fake Action Mode with a toolbar
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        MenuItem itemAdd = _menu.findItem(R.id.action_add);
        MenuItem itemDelete = _menu.findItem(R.id.action_delete);
        MenuItem itemBack = _menu.findItem(R.id.action_back);
        MenuItem itemSlash = _menu.findItem(R.id.action_blank);

        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemSlash.setVisible(false);

        _actionMode = null;

        if ((_selectedIndex >= 0) && (_selectedIndex < ItemFactory.getItemGroupObjectList().size())) {

            _listView.setItemChecked(_selectedIndex, false);
        }

        _toolbar.setTitle("Group List");
    }

    @Override
    public void onBackPressed() {

        if (_actionMode != null) {

            _actionMode.finish();
        } else {

            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}




