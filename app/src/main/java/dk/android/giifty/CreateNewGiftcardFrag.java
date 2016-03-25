package dk.android.giifty;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.web.SignInHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewGiftcardFrag extends Fragment {

    private SignInHandler signInHandler;

    public CreateNewGiftcardFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_new_giftcard, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        f();
        try {
            if (signInHandler.refreshTokenAsync()) {
                // Create user dialog
            } else {
                //create spinner running signIn
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    private void f() {
        new AlertDialog.Builder(getContext())
                .setTitle("fhjdsf")
                .setCancelable(false)
                .setNeutralButton("43", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    class MyReceiver extends MyBroadcastReceiver {


        @Override
        public void onSignIn() {
            if (signInHandler.isTokenExpired()) {

            }

        }
    }

}
