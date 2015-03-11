package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.view.ActionMode;
import android.view.Menu;
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
import com.gogocosmo.cosmoqiu.fire_sticker.Model.Group;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.GroupsTableHelper;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.ItemsTableHelper;

public class EditGroupActivity extends ActionBarActivity implements
        android.view.ActionMode.Callback {

    final private String TAG = "MEMORY-ACC";

    private Toolbar mToolbar;
    private ListView mListView;
    private GroupArrayAdapter mAdapter;

    private android.view.ActionMode mActionMode;
    private Menu mMenu;
    private int mSelectedIndex;

    private TextView mTotalStickerNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(ItemsTableHelper.getInstance(this));
        ItemFactory.setGroupsTableHelper(GroupsTableHelper.getInstance(this));

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.PURE_WHITE));

        mListView = (ListView) findViewById(R.id.listview);
        mAdapter = new GroupArrayAdapter(this, ItemFactory.getItemGroupObjectList());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // Important method: set the list item in this position activated.
                // No matter whether this view is recycled or not.
                mListView.setItemChecked(position, true);
                mSelectedIndex = position;
                startActionMode(EditGroupActivity.this);
                return true;
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedIndex = position;

                if (mActionMode != null) {
                    return;
                }

                startEditDialog();
                mListView.setItemChecked(mSelectedIndex, true);
            }
        });

        mTotalStickerNum = (TextView) findViewById(R.id.total_sticker_num);
        updateTotalStickerNum();
    }

    private void updateTotalStickerNum() {

        int totalGroupNum = ItemFactory.getItemGroupObjectList().size();
        int totalStickers = 0;

        for (int i = 0; i < totalGroupNum; ++i) {
            totalStickers += ItemFactory.getItemList(i).size();
        }

        mTotalStickerNum.setText(String.valueOf(totalStickers));
    }

    private void startEditDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_edit_group);

        dialog.setCanceledOnTouchOutside(false);

        ImageView cancelImage = (ImageView) dialog.findViewById(R.id.cancel_dialog);
        ImageView confirmImage = (ImageView) dialog.findViewById(R.id.confirm_dialog);
        final EditText groupName = (EditText) dialog.findViewById(R.id.editText_groupName);

        String originalTitle = ItemFactory.getItemGroupObjectList().get(mSelectedIndex).getGroupName();
        groupName.setText(originalTitle);

        // Set cursor at the end of the content
        int position = groupName.length();
        Editable etext = groupName.getText();
        Selection.setSelection(etext, position);

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
                Group updatedGroup =  ItemFactory.getItemGroupObjectList().get(mSelectedIndex);
                updatedGroup.setGroupName(newTitle);
                mAdapter.notifyDataSetChanged();
                updateTotalStickerNum();
                ItemFactory.notifyGroupUpdate(updatedGroup);
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
                mAdapter.notifyDataSetChanged();
                updateTotalStickerNum();
                mSelectedIndex = ItemFactory.getItemGroupObjectList().size() - 1;
                mListView.setItemChecked(ItemFactory.getItemGroupObjectList().size() - 1, true);

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

        String uuid = ItemFactory.getItemGroupObjectList().get(mSelectedIndex).getUuid();
        ItemFactory.getSelectedItemIndexesList().remove(mSelectedIndex);
        ItemFactory.getItemLists().remove(mSelectedIndex);
        ItemFactory.getItemGroupObjectList().remove(mSelectedIndex);
        mAdapter.notifyDataSetChanged();

        // Notify Database the Group Deletion
        ItemFactory.notifyGroupDeletion(uuid);

        updateTotalStickerNum();

        mActionMode.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_group, menu);
        mMenu = menu;

        mToolbar.setTitle("Group List");

        MenuItem itemAdd = mMenu.findItem(R.id.action_add);
        MenuItem itemDelete = mMenu.findItem(R.id.action_delete);
        MenuItem itemBack = mMenu.findItem(R.id.action_back);
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);

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

                if (mActionMode != null) {
                    mActionMode.finish();
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
                mListView.setItemChecked(mSelectedIndex, false);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        mToolbar.setTitle("Edit Mode");

        MenuItem itemAdd = mMenu.findItem(R.id.action_add);
        MenuItem itemDelete = mMenu.findItem(R.id.action_delete);
        MenuItem itemBack = mMenu.findItem(R.id.action_back);
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);

        itemAdd.setVisible(false);
        itemBack.setVisible(false);
        itemDelete.setVisible(true);
        itemSlash.setVisible(false);

        mActionMode = mode;

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

        MenuItem itemAdd = mMenu.findItem(R.id.action_add);
        MenuItem itemDelete = mMenu.findItem(R.id.action_delete);
        MenuItem itemBack = mMenu.findItem(R.id.action_back);
        MenuItem itemSlash = mMenu.findItem(R.id.action_blank);

        itemAdd.setVisible(true);
        itemBack.setVisible(false);
        itemDelete.setVisible(false);
        itemSlash.setVisible(false);

        mActionMode = null;

        if ((mSelectedIndex >= 0) && (mSelectedIndex < ItemFactory.getItemGroupObjectList().size())) {

            mListView.setItemChecked(mSelectedIndex, false);
        }

        mToolbar.setTitle("Group List");
    }

    @Override
    public void onBackPressed() {

        if (mActionMode != null) {

            mActionMode.finish();
        } else {

            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }
}




