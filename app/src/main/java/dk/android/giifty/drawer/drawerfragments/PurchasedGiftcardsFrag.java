package dk.android.giifty.drawer.drawerfragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.giftcard.GiftcardAdapter1;
import dk.android.giifty.giftcard.GiftcardRepository;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;


/**
 * A simple {@link Fragment} subclass.
 */
public class PurchasedGiftcardsFrag extends DrawerFragment {


    private GiftcardRepository controller;
    private GiftcardAdapter1 adapter;
    private GiiftyPreferences myPrefs;
    private TextView emptyText;

    public PurchasedGiftcardsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_giftcards, container, false);
        controller = GiftcardRepository.getInstance();
        myPrefs = GiiftyPreferences.getInstance();

        emptyText = (TextView) root.findViewById(R.id.no_giftcards_text_id);

        int userId = -1;
        if (myPrefs.hasUser()) {
            userId = myPrefs.getUser().getUserId();
        }

        adapter = new GiftcardAdapter1(getActivity(), userId);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        GiiftyApplication.getBus().register(this);
        setToolbarTitle(getString(R.string.purchased_giftcard));
        if (!myPrefs.hasUser()) {
            new SignInDialogHandler().startDialog(getContext());
        } else {
            if (SignInHandler.getInstance().isTokenExpired()) {
                SignInHandler.getInstance().refreshTokenAsync();
            } else {
                setData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GiiftyApplication.getBus().unregister(this);
    }

    @Subscribe
    public void onSignedIn(SignedInEvent event) {
        if (event.isSuccessful) {
            setData();
        }
    }

    private void setData() {
        List<Giftcard> immutableList = controller.getMyGiftcardPurchased();
        if (immutableList.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(getText(R.string.msg_no_puchased_gc));
        } else {
            emptyText.setVisibility(View.INVISIBLE);
            adapter.updateData(immutableList);
        }
    }

}
