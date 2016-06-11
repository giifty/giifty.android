package dk.android.giifty.drawer.drawerfragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.List;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.components.DividerItemDecoration;
import dk.android.giifty.components.TextViewAdapter;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.model.Giftcard;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.MyDialogBuilder;
import dk.android.giifty.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateNewGiftcardFrag extends DrawerFragment {

    private static final String TAG = CreateNewGiftcardFrag.class.getSimpleName();
    private SignInHandler signInHandler;
    private ProgressDialog mProgressDialog;
    private GiiftyPreferences myPrefs;

    public CreateNewGiftcardFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_new_giftcard, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
        TextViewAdapter adapter = new TextViewAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(adapter);
        myPrefs = GiiftyPreferences.getInstance();
        signInHandler = SignInHandler.getInstance();
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
            if (signInHandler.isTokenExpired()) {
                signInHandler.refreshTokenAsync();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }
}
