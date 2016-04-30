package dk.android.giifty.signin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import dk.android.giifty.MyApp;
import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.components.SignInDialogLayout;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Broadcasts;
import dk.android.giifty.utils.MyDialogBuilder;

/**
 * Created by mak on 30-04-2016.
 */
public class SignInDialogHandler extends MyBroadcastReceiver implements DialogInterface.OnCancelListener {

    private AlertDialog dialog;
    private Context context = MyApp.getMyApplicationContext();
    private SignInDialogLayout view;

    public void startDialog(Activity activity) {
        view = new SignInDialogLayout(activity);
        dialog = MyDialogBuilder.createSignInDialog(activity, view);
        dialog.setOnCancelListener(this);
        dialog.show();
        LocalBroadcastManager.getInstance(context).registerReceiver(this, new IntentFilter(Broadcasts.SIGN_IN_FILTER));
    }

    @Override
    public void onSignIn(boolean isSuccess) {
        if (isSuccess) {
            dialog.cancel();
            UserRepository.getInstance().fetchUserFromServer();
        } else {
            view.switchVisibility();
            Toast.makeText(context, "Du kunne ikke logges ind, er dine oplysninger rigtige?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }
}
