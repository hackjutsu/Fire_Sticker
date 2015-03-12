package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.gogocosmo.cosmoqiu.fire_sticker.R;
import com.gogocosmo.cosmoqiu.fire_sticker.Utils.CustomizedToast;

public class AboutAppActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        // Toolbar Configurations
        mToolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.PURE_WHITE));

        Button feedbackButton = (Button) findViewById(R.id.feedbackButton);

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
                v.startAnimation(buttonClick);
                sendFeedBackByMail();
            }
        });
    }

    protected void sendFeedBackByMail() {

        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "gogocosmo1989@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT, "My Mail");
        intent.putExtra(Intent.EXTRA_TEXT, "My Message");

        try {
            startActivity(Intent.createChooser(intent, "We value your feedback..."));
        } catch (android.content.ActivityNotFoundException ex) {
            CustomizedToast.showToast(AboutAppActivity.this,
                    "There are no email clients installed.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_app, menu);
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
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }

        return super.onOptionsItemSelected(item);
    }
}
