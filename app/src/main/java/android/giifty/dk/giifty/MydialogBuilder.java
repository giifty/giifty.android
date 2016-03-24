package android.giifty.dk.giifty;

import android.app.Activity;
import android.app.AlertDialog;
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
}
