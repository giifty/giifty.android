package dk.android.giifty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import dk.android.giifty.utils.ActivityStarter;

/**
 * Created by mak on 24-03-2016.
 */
public class MyDialogBuilder {

    public static AlertDialog createNoUserDialog(final Activity activity) {
        return new AlertDialog.Builder(activity)
                .setTitle(activity.getString(R.string.user_name_create_user))
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
                        activity.onBackPressed();
                    }
                })
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
