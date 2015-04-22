package com.gogocosmo.cosmoqiu.fire_sticker.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.gogocosmo.cosmoqiu.fire_sticker.R;

public class ProfilePicker {

    static public void showPickerDialog(final Context ctx) {

        final CharSequence[] items = {
                "Owls",
                "Fox",
                "Zebra",
                "Lion",
                "Panda",
                "Monkey",
                "Puppy",
                "Kitty",
                "Penguin",
                "Chicken",
                "Pig"
        };

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(ctx);
        builderSingle.setTitle("Pick up a profile:)");
        builderSingle.setNegativeButton("cancel",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                switch (item) {
                    case 0:
                        setProfile(ctx, R.drawable.profile_owl);
                        break;
                    case 1:
                        setProfile(ctx, R.drawable.profile_fox);
                        break;
                    case 2:
                        setProfile(ctx, R.drawable.profile_zebra);
                        break;
                    case 3:
                        setProfile(ctx, R.drawable.profile_lion);
                        break;
                    case 4:
                        setProfile(ctx, R.drawable.profile_panda);
                        break;
                    case 5:
                        setProfile(ctx, R.drawable.profile_monkey);
                        break;
                    case 6:
                        setProfile(ctx, R.drawable.profile_dog);
                        break;
                    case 7:
                        setProfile(ctx, R.drawable.profile_cat);
                        break;
                    case 8:
                        setProfile(ctx, R.drawable.profile_penguin);
                        break;
                    case 9:
                        setProfile(ctx, R.drawable.profile_chicken);
                        break;
                    case 10:
                        setProfile(ctx, R.drawable.profile_pig);
                        break;
                    default:
                        setProfile(ctx, R.drawable.profile_owl);
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    static public int getProfile(final Context ctx) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preference.getInt("PROFILE_IMAGE", R.drawable.profile_owl);
    }

    static private void setProfile(final Context ctx, final int profile) {

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(ctx);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt("PROFILE_IMAGE", profile);
        editor.commit();
    }
}
