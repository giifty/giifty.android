package android.giifty.dk.giifty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.giifty.dk.giifty.utils.ActivityStarter;

/**
 * Created by mak on 24-03-2016.
 */
public class MyDialogBuilder {

    public static AlertDialog createNoUserDialog(final Activity activity){
        return new AlertDialog.Builder(activity)
                .setTitle("Opret en bruger")
                .setMessage("Opret en bruger så du kan sælge og købe gavekort")
                .setPositiveButton("Opret bruger", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityStarter.startCreateUserActivity(activity);
                    }
                })
                .create();
    }
    public static AlertDialog createTermsAndConditionsDialog(final Context context, DialogInterface.OnClickListener clickListener){
        return new AlertDialog.Builder(context)
                .setTitle(R.string.terms_conditions_title)
                .setMessage(R.string.terms_conditions_body)
                .setPositiveButton(R.string.accept, clickListener)
                .setNegativeButton(R.string.decline, clickListener)
                .create();
    }

}
