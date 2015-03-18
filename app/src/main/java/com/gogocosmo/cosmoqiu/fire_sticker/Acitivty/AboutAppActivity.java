package com.gogocosmo.cosmoqiu.fire_sticker.Acitivty;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

        // displaying the app version
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;

        TextView appVersion = (TextView) findViewById(R.id.app_version);
        appVersion.setText("version " + versionName);

        // feedback button
        Button feedbackButton = (Button) findViewById(R.id.feedbackButton);

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
                v.startAnimation(buttonClick);
                sendFeedBackByMail();
            }
        });

        // rateApp button
        final ImageButton rateAppButton = (ImageButton) findViewById(R.id.rateButton);

        rateAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.5F);
                v.startAnimation(buttonClick);
                rateMyApp(true);
            }
        });

        Animation fadeOut = new AlphaAnimation(1.0f, 0.1f);
        fadeOut.setDuration(700);
        fadeOut.setStartOffset(500);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation fadeIn = new AlphaAnimation(0.1f, 1.0f);
                fadeIn.setDuration(700);
                rateAppButton.startAnimation(fadeIn);
            }
        });

        rateAppButton.startAnimation(fadeOut);

        // Rate app link
        TextView rateAppLink = (TextView) findViewById(R.id.rateAppLink);
        rateAppLink.setText(Html.fromHtml("<u>Rate this app?</u>"));
        rateAppLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateMyApp(true);
            }
        });
    }

    protected void sendFeedBackByMail() {

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String versionName = pInfo.versionName;

        Intent intent = new Intent(Intent.ACTION_SENDTO,
                Uri.fromParts("mailto", "owlscoffeehouse@gmail.com", null));
        intent.putExtra(Intent.EXTRA_SUBJECT,
                "Note it! " + " FEEDBACK");

        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String deviceInfo = "Android SDK: " + sdkVersion + " (" + release + ")";

        intent.putExtra(Intent.EXTRA_TEXT,
                deviceInfo + "\n" +
                        "App verion: " + versionName + "\n\n");

        try {
            startActivity(Intent.createChooser(intent, "We value your feedback..."));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void rateMyApp(boolean googlePlay) {
        //true if Google Play, false if Amazone Store
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse((googlePlay ? "market://details?id=" : "amzn://apps/android?p=")
                            + getPackageName())));
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        } catch (ActivityNotFoundException e1) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse((googlePlay ? "http://play.google.com/store/apps/details?id=" :
                                "http://www.amazon.com/gp/mas/dl/android?p=") + getPackageName())));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } catch (ActivityNotFoundException e2) {
                CustomizedToast.showToast(this, "You don't have any app that can open this link");
            }
        }
    }
}
