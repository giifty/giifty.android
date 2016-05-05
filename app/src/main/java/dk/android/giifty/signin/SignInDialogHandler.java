package dk.android.giifty.signin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import dk.android.giifty.R;
import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.components.SignInDialogLayout;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Broadcasts;
import dk.android.giifty.utils.MyDialogBuilder;

/**
 * Created by mak on 30-04-2016.
 */
public class SignInDialogHandler extends MyBroadcastReceiver implements DialogInterface.OnCancelListener, View.OnClickListener {

    private AlertDialog dialog;
    private Context context;
    private SignInDialogLayout view;
    private int giftcardId;

    public void startDialog(Context context, int giftcardId) {
        this.context = context;
        this.giftcardId = giftcardId;
        show();
    }

    public void startDialog(Context context) {
        this.context = context;
        show();
    }

    private void show() {
        view = new SignInDialogLayout(context);
        view.setOnCreateUserListener(this);
        dialog = MyDialogBuilder.createSignInDialog(context, view);
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
            Toast.makeText(context, R.string.signin_error_msg, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
    }

    @Override
    public void onClick(View v) {
        if (giftcardId > 0) {
            ActivityStarter.startCreateUserActivityNoAni(context, giftcardId);
        } else {
            ActivityStarter.startCreateUserActivityNoAni(context);
        }
    }
}
