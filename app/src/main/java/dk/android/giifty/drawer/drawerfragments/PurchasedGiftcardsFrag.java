package dk.android.giifty.drawer.drawerfragments;


import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.PurchasedGiftcardsFetchedEvent;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.databinding.FragmentMyGiftcardsBinding;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.giftcard.GiftcardAdapter1;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;

public class PurchasedGiftcardsFrag extends DrawerFragment {

    private GiftcardAdapter1 adapter;
    private GiiftyPreferences myPrefs;
    private TextView emptyText;
    private ObservableBoolean hasContent = new ObservableBoolean();

    public PurchasedGiftcardsFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentMyGiftcardsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_giftcards, container, false);

        myPrefs = GiiftyPreferences.getInstance();

        int userId = -1;
        if (myPrefs.hasUser()) {
            userId = myPrefs.getUser().getUserId();
        }

        adapter = new GiftcardAdapter1(getActivity(), userId);
        binding.recyclerViewId.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false));
        binding.recyclerViewId.setHasFixedSize(true);
        binding.recyclerViewId.setAdapter(adapter);

        binding.setHasContent(hasContent);

        return binding.getRoot();
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

    @Subscribe
    public void onSignedIn(PurchasedGiftcardsFetchedEvent event) {
        if (event.isSuccessFul) {
            setData();
        }
    }

    private void setData() {
        List<Giftcard> gcList = myPrefs.getPurchasedGiftcards();
        hasContent.set(gcList != null && !gcList.isEmpty());
        if (gcList != null) {
            adapter.updateData(gcList);
        }
    }
}
