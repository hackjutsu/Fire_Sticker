package com.gogocosmo.cosmoqiu.fire_sticker.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.gogocosmo.cosmoqiu.fire_sticker.R;


public class PasswordManager {

    static public void showCreatePasswordDialog(final Context context) {

        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_create_password);

        dialog.setCanceledOnTouchOutside(false);

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_dialog_create_password);
        TextView confirmButton = (TextView) dialog.findViewById(R.id.confirm_dialog_create_password);
        final EditText passwordBoxNew = (EditText) dialog.findViewById(R.id.editText_password_new);
        final EditText passwordBoxConfirm = (EditText) dialog.findViewById(R.id.editText_password_confirm);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword = passwordBoxNew.getText().toString();
                String confirmPassword = passwordBoxConfirm.getText().toString();

                if (newPassword.equals(confirmPassword)) {
                    SharedPreferences.Editor editor = preference.edit();

                    editor.putString("Credentials", newPassword);
                    editor.putBoolean("PASSWORD_PROCTECTION", true);
                    editor.commit();
                    dialog.dismiss();
                    CustomizedToast.showToast(context, "New password set up");
                } else {
                    CustomizedToast.showToast(context,
                            "Confirmation must match new password.");
                }
            }
        });

        dialog.show();
    }


    static public void showResetPasswordDialog(final Context context) {

        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(context);

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_reset_password);

        dialog.setCanceledOnTouchOutside(false);

        TextView cancelButton = (TextView) dialog.findViewById(R.id.cancel_dialog_password_reset);
        TextView confirmButton = (TextView) dialog.findViewById(R.id.confirm_dialog_password_reset);
        final EditText passwordBoxOld = (EditText) dialog.findViewById(R.id.editText_password_old_reset);
        final EditText passwordBoxNew = (EditText) dialog.findViewById(R.id.editText_password_new_reset);
        final EditText passwordBoxConfirm = (EditText) dialog.findViewById(R.id.editText_password_confirm_reset);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = passwordBoxOld.getText().toString();
                String newPassword = passwordBoxNew.getText().toString();
                String confirmPassword = passwordBoxConfirm.getText().toString();

                if (oldPassword.equals(preference.getString("Credentials", "1989")) &&
                        newPassword.equals(confirmPassword)) {
                    SharedPreferences.Editor editor = preference.edit();

                    editor.putString("Credentials", newPassword);
                    editor.commit();
                    dialog.dismiss();
                    CustomizedToast.showToast(context, "New password set up");
                } else {
                    CustomizedToast.showToast(context, "Please check again :)");
                }
            }
        });

        dialog.show();
    }

}
