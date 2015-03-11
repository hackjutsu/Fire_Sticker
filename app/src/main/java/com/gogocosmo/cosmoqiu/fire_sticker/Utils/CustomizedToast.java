package com.gogocosmo.cosmoqiu.fire_sticker.Utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by Citrixer on 3/10/15.
 */
public class CustomizedToast {

    static public void showToast(Context ctx, String msg) {

        final Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 650);
    }

    static public void showToast(Context ctx, String msg, int ms) {

        final Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_LONG);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, ms);
    }
}
