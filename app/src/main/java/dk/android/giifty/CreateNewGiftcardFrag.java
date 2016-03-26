package dk.android.giifty;


import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.components.TextViewAdapter;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Broadcasts;
import dk.android.giifty.utils.Utils;
import dk.android.giifty.web.SignInHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewGiftcardFrag extends Fragment {

    private SignInHandler signInHandler;
    private ProgressDialog mProgressDialog;
    private MyReceiver myReceiver;

    public CreateNewGiftcardFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_new_giftcard, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        TextViewAdapter adapter = new TextViewAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        signInHandler = SignInHandler.getInstance();
        myReceiver = new MyReceiver();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver,
                new IntentFilter(Broadcasts.ON_SIGNED_IN_FILTER));
        try {
            if (signInHandler.isTokenExpired()) {
                if (UserRepository.getInstance().hasUser()) {
                    signInHandler.refreshTokenAsync();
                    showProgressDialog();
                } else {
                    showNoUserMsg();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
    }

    private void showProgressDialog() {
        //TODO customize that spinner damnit
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.msg_loading));
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void dismissDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    private void showNoUserMsg() {
        MyDialogBuilder.createNoUserDialog(getActivity()).show();
    }

    class MyReceiver extends MyBroadcastReceiver {
        @Override
        public void onSignIn() {
            dismissDialog();
            if (signInHandler.isTokenExpired()) {
                Utils.makeToast(getString(R.string.msg_failed_login));
            }
        }
    }

}
