package com.gogocosmo.cosmoqiu.fire_sticker.Utils;

import android.app.Activity;
import android.os.Build;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.gogocosmo.cosmoqiu.fire_sticker.Model.ItemFactory;
import com.gogocosmo.cosmoqiu.fire_sticker.R;

public class CustomizedShowcase {

    static public void loadDefaultDataForFirstInstallation() {

        ItemFactory.createGroup("Welcome!");
        ItemFactory.createGroup("Great Ideas");
        ItemFactory.createGroup("To-Do List");

        // Add default items to group "Welcome!"
        ItemFactory.createItem(0, "Bookmark the important notes", "", "Bookmark", 1, 0);
        ItemFactory.createItem(0, "Try to edit this note ^_^", "Stamp it when the task is finished", "Edit Notes", 0, 1);
        ItemFactory.createItem(0, "You can bookmark and stamp at the same time", "Awesome note", "Long Press to Delete", 1, 1);
        ItemFactory.createItem(0, "Welcome to Note it! Here, every note has two colorful sides.",
                "You can write down the notes on the front and add hint on the back. " +
                        "Or just write the question on the front, and solutions on the back",
                "Welcome to Note it!", 1, 0);

        // Add default items to group "Great ideas"
        ItemFactory.createItem(1, "Change the world!", "Less pollution.", "Awesome Idea", 1, 0);
        ItemFactory.createItem(1, "Do meditate. Don't stay up all night. ",
                "Studies show that those who meditate daily for at least 30 minutes" +
                        "have better focus", "Ready, Meditate", 0, 1);
        ItemFactory.createItem(1, "Do trust yourself. Don't go it alone.",
                "When people believe they can grow their brainpower, " +
                        "they become more curious and more open-minded.", "Ready, Trust", 1, 1);
        ItemFactory.createItem(1, "Do model the great. Don't be a sheep.",
                "Think, what are smart people doing, and what can that teach me?", "Ready, Model", 0, 0);
        ItemFactory.createItem(1, "Do pay attention. Don't just pass judgement.",
                "Listen closely. Be observant and informed. Be patient and in the moment", "Ready, Attentions", 0, 0);

        // Add default items to group "To-Do List"
        ItemFactory.createItem(2, "Read the Wiki about Scotland History in 19 century.", "Watch the documentary.", "Investigate Scotland History", 1, 1);
        ItemFactory.createItem(2, "Everyone is talking about it. It must be interesting.", "Order it online!", "Order The Lean Startup", 0, 0);
        ItemFactory.createItem(2, "Egg, Milk, Onions, Cheese", "Maybe some pens.", "Target Shopping", 0, 1);
        ItemFactory.createItem(2, "Just do it!", "Keep running!", "30 minutes' Running", 1, 0);
    }

    static public void displayLaunchActivtyShowcaseViewZero(final Activity activity) {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.CENTER_IN_PARENT);
        int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 8);

        } else {
            lps.setMargins(margin, margin, margin, margin * 8);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(activity)
                .setContentTitle(Html.fromHtml("<b>" + "Note it!" + "</b>"))
                .setContentText("A quick guide within 15 seconds.")
                .setTarget(Target.NONE)
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        displayLaunchActivtyShowcaseViewOne(activity);
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                }).build();
        sv.setButtonPosition(lps);
    }

    static private void displayLaunchActivtyShowcaseViewOne(final Activity activity) {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 5);

        } else {
            lps.setMargins(margin, margin, margin, margin);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(activity)
                .setContentTitle("New Note")
                .setContentText("Click here to create your note.")
                .setTarget(new ViewTarget(R.id.showCasePoint_Add, activity))
                .setStyle(R.style.CustomShowcaseTheme)
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        displayLaunchActivtyShowcaseViewTwo(activity);
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                }).build();
        sv.setButtonPosition(lps);
    }

    static private void displayLaunchActivtyShowcaseViewTwo(final Activity activity) {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 5);

        } else {
            lps.setMargins(margin, margin, margin, margin);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(activity)
                .setContentTitle("Group Navigation")
                .setContentText("Tap on the group name for navigation or ... Just swipe!")
                .setStyle(R.style.CustomShowcaseTheme)
                .setTarget(new ViewTarget(R.id.showCasePoint_Tab, activity))
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        displayLaunchActivtyShowcaseViewThree(activity);
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                })
                .build();
        sv.setButtonPosition(lps);
    }

    static private void displayLaunchActivtyShowcaseViewThree(Activity activity) {

        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (activity.getResources().getDisplayMetrics().density * 12)).intValue();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // only for lollipop and newer versions
            lps.setMargins(margin, margin, margin, margin * 5);

        } else {
            lps.setMargins(margin, margin, margin, margin);
        }

        ShowcaseView sv;
        sv = new ShowcaseView.Builder(activity)
                .setContentTitle("Quick Overview")
                .setContentText("Take a quick look through the notes in the selected group.")
                .setStyle(R.style.CustomShowcaseThemeEnd)
                .setTarget(new ViewTarget(R.id.FireButton, activity))
                .setShowcaseEventListener(new OnShowcaseEventListener() {

                    @Override
                    public void onShowcaseViewShow(final ShowcaseView scv) {
                    }

                    @Override
                    public void onShowcaseViewHide(final ShowcaseView scv) {
                        scv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onShowcaseViewDidHide(final ShowcaseView scv) {
                    }

                })
                .build();

        sv.setButtonPosition(lps);
    }
}
