package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;
import com.gogocosmo.cosmoqiu.fire_sticker.sqlite.DatabaseHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SettingsActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private SharedPreferences mPreference;
    private Spinner mViewModeSpinner;
    private LinearLayout mPokerPanel;
    private android.support.v7.widget.SwitchCompat mPokerSwitch;
    private TextView mRestore;
    private TextView mBackup;
    private FrameLayout mLayoutDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mPreference = PreferenceManager.getDefaultSharedPreferences(this);

        // Set the status bar color
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.STATUS_BAR_BACKGROUND));
        }

        // DataBase Configurations
        ItemFactory.setItemsTableHelper(DatabaseHelper.getInstance(this));

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(Color.WHITE);

        // Set up Layout Panel
        mLayoutDivider = (FrameLayout) findViewById(R.id.layout_divider);

        mPokerPanel = (LinearLayout) findViewById(R.id.pokerPanel);
        mPokerSwitch = (android.support.v7.widget.SwitchCompat) findViewById(R.id.pokerSwitch);
        Boolean pokerPattern = mPreference.getBoolean("POKER", true);
        if (pokerPattern) {
            mPokerSwitch.setChecked(true);
        } else {
            mPokerSwitch.setChecked(false);
        }


        int ViewMode = mPreference.getInt("VIEWMODE", 1);
        ArrayList<String> viewList = new ArrayList<>();
        viewList.add("Grid");
        viewList.add("List");
        mViewModeSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                R.layout.settings_spinner_rowlayout, viewList);
        dataAdapter.setDropDownViewResource(R.layout.settings_spinner_rowlayout);
        mViewModeSpinner.setAdapter(dataAdapter);
        mViewModeSpinner.setSelection(ViewMode - 1);
        mViewModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor editor = mPreference.edit();

                if (position == 0) {
                    mLayoutDivider.setVisibility(View.VISIBLE);
                    mPokerPanel.setVisibility(View.VISIBLE);
                    editor.putInt("VIEWMODE", 1);
                } else {
                    mLayoutDivider.setVisibility(View.GONE);
                    mPokerPanel.setVisibility(View.GONE);
                    editor.putInt("VIEWMODE", 2);
                }
                editor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPokerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = mPreference.edit();

                if (isChecked) {
                    editor.putBoolean("POKER", true);
                } else {
                    editor.putBoolean("POKER", false);
                }
                editor.commit();
            }
        });

        if (ViewMode == 1) {
            mLayoutDivider.setVisibility(View.VISIBLE);
            mPokerPanel.setVisibility(View.VISIBLE);
        }


        // Set up Backup/Restore Panel
        mRestore = (TextView) findViewById(R.id.Restore);
        mRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                restoreFromBackupDialog();
            }
        });

        mBackup = (TextView) findViewById(R.id.Backup);
        mBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backupDialog();
            }
        });

    }

    private void restoreFromBackupDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Restore from backup?");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        restore();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void restore() {

        final String backUpFilePath = Environment.getExternalStorageDirectory() + "/NoteItBackUp/ItemDB.db";
        File backUpDB = new File(backUpFilePath);
        if (backUpDB.exists()) {
            String destPath = SettingsActivity.this.getDatabasePath("ItemDB").getPath();

            try {
                FileInputStream fis = new FileInputStream(backUpDB);
                OutputStream output = new FileOutputStream(destPath);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                output.close();
                fis.close();

                ItemFactory.notifyDBRestore();
                CustomizedToast.showToast(SettingsActivity.this, "Restored from backup!", 1500);
            } catch (Exception e) {
                CustomizedToast.showToast(SettingsActivity.this,
                        "Failed to restored from backup.", 1500);
            }

        } else {
            CustomizedToast.showToast(SettingsActivity.this, "No backup found.", 1500);
        }
    }

    private void backupDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("Backup the notes?");

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        backup();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void backup() {

        final File dbFile = SettingsActivity.this.getDatabasePath("ItemDB");

        if (dbFile.exists()) {

            try {
                String BackUpPath = Environment.getExternalStorageDirectory() + "/NoteItBackUp/";

                File backUpFolder = new File(BackUpPath);
                if (!(backUpFolder.exists())) {
                    backUpFolder.mkdir();
                }

                FileInputStream fis = new FileInputStream(dbFile);

                String outFileName = BackUpPath + "ItemDB.db";

                // Open the empty db as the output stream
                OutputStream output = new FileOutputStream(outFileName);

                // Transfer bytes from the inputfile to the outputfile
                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    output.write(buffer, 0, length);
                }

                // Close the streams
                output.flush();
                output.close();
                fis.close();
                CustomizedToast.showToast(SettingsActivity.this, "The back up is saved to "
                        + outFileName, 1500);
            } catch (Exception e) {
                e.printStackTrace();
                CustomizedToast.showToast(SettingsActivity.this, "Back up failed...");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finshSettings();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finshSettings();
    }

    private void finshSettings() {

        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
