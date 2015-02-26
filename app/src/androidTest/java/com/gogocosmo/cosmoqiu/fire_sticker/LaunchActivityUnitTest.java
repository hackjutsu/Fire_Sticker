package com.gogocosmo.cosmoqiu.fire_sticker;

import android.content.ComponentName;
import android.content.Intent;
import android.view.ContextThemeWrapper;

import com.gogocosmo.cosmoqiu.fire_sticker.Acitivty.LaunchActivity;

public class LaunchActivityUnitTest extends
        android.test.ActivityUnitTestCase<LaunchActivity> {

    private LaunchActivity activity;

    public LaunchActivityUnitTest() {
        super(LaunchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // Solving the bug with error messages: "java.lang.IllegalStateException: You need to use a
        // Theme.AppCompat theme (or descendant) with this activity." ActivityUnitTestCase cannot
        // start an ActionBarActivity successfully without using a ContextThemeWrapper.
        // http://stackoverflow.com/questions/22364433/activityunittestcase-and-startactivity-with-actionbaractivity
        ContextThemeWrapper context = new ContextThemeWrapper(
                getInstrumentation().getTargetContext(),
                R.style.AppTheme);
        setActivityContext(context);

//        ComponentName cn = new ComponentName(getInstrumentation().getTargetContext(), LaunchActivity.class);

        // Start the Activity
        Intent intent = new Intent(getInstrumentation().getTargetContext(),
                LaunchActivity.class);
//        setActivity(launchActivity(cn, LaunchActivity.class, null));
        startActivity(intent, null, null);
        activity = getActivity();
    }
//
//    public void test001() {
//
//        assertEquals(true, true);
//    }
//
//    public void test002() {
//
//        assertEquals(false, true);
//    }
//
//    public void test003() {
//
//        assertEquals(false, true);
//    }
}