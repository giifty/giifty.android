package android.giifty.dk.giifty;


import android.content.DialogInterface;
import android.giifty.dk.giifty.broadcastreceivers.MyBroadcastReceiver;
import android.giifty.dk.giifty.giftcard.GiftcardAdapter1;
import android.giifty.dk.giifty.giftcard.GiftcardRepository;
import android.giifty.dk.giifty.model.Giftcard;
import android.giifty.dk.giifty.web.SignInHandler;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasedGiftcardsFrag extends Fragment {


    private GiftcardRepository controller;
    private GiftcardAdapter1 adapter;
    private SignInHandler signInHandler;

    public PurchasedGiftcardsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_giftcards, container, false);
        controller = GiftcardRepository.getInstance();

        List<Giftcard> list = controller.getMyGiftcardPurchased();
        adapter = new GiftcardAdapter1(getActivity(), list);
        TextView emptyText = (TextView) root.findViewById(R.id.no_giftcards_text_id);

        if (list.isEmpty()) {
            emptyText.setText(getText(R.string.msg_no_puchased_gc));
        }

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        signInHandler = new SignInHandler();
        return root;
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
