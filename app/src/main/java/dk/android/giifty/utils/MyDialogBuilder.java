package dk.android.giifty.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import dk.android.giifty.R;

public class MyDialogBuilder {

    public static AlertDialog createNoUserDialog(final Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.sign_in_text))
                .setMessage(activity.getString(R.string.msg_create_user))
                .setCancelable(false)
                .setPositiveButton(activity.getString(R.string.create_user), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityStarter.startCreateUserActivity(activity);
                    }
                })
                .setNegativeButton(activity.getString(R.string.later), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //  activity.onBackPressed();
                    }
                })
                .create();
    }

    public static AlertDialog createSignInDialog(Context context, View v) {
        return new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(v)
                .create();
    }

    public static AlertDialog createTermsAndConditionsDialog(final Context context, DialogInterface.OnClickListener clickListener) {
        return new AlertDialog.Builder(context)
                .setTitle(R.string.terms_conditions_title)
                .setMessage(R.string.msg_terms_conditions_body)
                .setPositiveButton(R.string.accept, clickListener)
                .setNegativeButton(R.string.decline, clickListener)
                .create();
    }
}
