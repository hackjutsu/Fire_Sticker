package com.gogocosmo.cosmoqiu.fire_sticker.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gogocosmo.cosmoqiu.fire_sticker.R;

public class ProfilePicker {

    static private CharSequence[] mProfileList = {
            "Owl",
            "Tiger",
            "Fox",
            "Zebra",
            "Lion",
            "Panda",
            "Monkey",
            "Puppy",
            "Kitty",
            "Penguin",
            "Chicken",
            "Pig"};

    static public CharSequence[] getProfileItems() {

        return mProfileList;
    }

    static public void setProfileImage(Context ctx, String item) {

        switch (item) {
            case "Owl":
                setProfile(ctx, R.drawable.profile_owl);
                break;
            case "Fox":
                setProfile(ctx, R.drawable.profile_fox);
                break;
            case "Zebra":
                setProfile(ctx, R.drawable.profile_zebra);
                break;
            case "Lion":
                setProfile(ctx, R.drawable.profile_lion);
                break;
            case "Panda":
                setProfile(ctx, R.drawable.profile_panda);
                break;
            case "Monkey":
                setProfile(ctx, R.drawable.profile_monkey);
                break;
            case "Puppy":
                setProfile(ctx, R.drawable.profile_dog);
                break;
            case "Kitty":
                setProfile(ctx, R.drawable.profile_cat);
                break;
            case "Penguin":
                setProfile(ctx, R.drawable.profile_penguin);
                break;
            case "Chicken":
                setProfile(ctx, R.drawable.profile_chicken);
                break;
            case "Pig":
                setProfile(ctx, R.drawable.profile_pig);
                break;
            case "Tiger":
                setProfile(ctx, R.drawable.profile_tiger);
                break;
            default:
                setProfile(ctx, R.drawable.profile_tiger);
        }
    }

    static public int getProfile(final Context ctx) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt("PROFILE_IMAGE", R.drawable.profile_tiger);
    }

    static private void setProfile(final Context ctx, final int profile) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("PROFILE_IMAGE", profile);
        editor.commit();
    }
}
