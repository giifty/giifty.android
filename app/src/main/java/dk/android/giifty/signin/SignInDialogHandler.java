package dk.android.giifty.signin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.squareup.otto.Subscribe;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.MyDialogBuilder;

public class SignInDialogHandler implements DialogInterface.OnCancelListener, View.OnClickListener {

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
        GiiftyApplication.getBus().register(this);
        view = new SignInDialogLayout(context);
        view.setOnCreateUserListener(this);
        dialog = MyDialogBuilder.createSignInDialog(context, view);
        dialog.setOnCancelListener(this);
        dialog.show();
    }

    @Subscribe
    public void onSignedIn(SignedInEvent event) {
        if (event.isSuccessful) {
            dialog.cancel();
        } else {
            view.switchVisibility();
            if(event.code == 401){
                showSnackBar(R.string.signin_error_msg);
            }else {
                showSnackBar(R.string.generel_error_msg);
            }
        }
    }

    private void showSnackBar(int id){
        Snackbar.make(view, context.getString(id), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        GiiftyApplication.getBus().unregister(this);
    }

    @Override
    public void onClick(View v) {
        dialog.cancel();
        if (giftcardId > 0) {
            ActivityStarter.startCreateUserActivityNoAni(context, giftcardId);
        } else {
            ActivityStarter.startCreateUserActivityNoAni(context);
        }
    }
}
