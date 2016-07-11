package dk.android.giifty.drawer.drawerfragments;


import android.content.DialogInterface;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.components.DividerItemDecoration;
import dk.android.giifty.components.TextViewAdapter;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.model.User;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.GiiftyPreferences;

public class CreateNewGiftcardFrag extends DrawerFragment {

    private static final String TAG = CreateNewGiftcardFrag.class.getSimpleName();
    private SignInHandler signInHandler;
    private GiiftyPreferences myPrefs;
    private TextViewAdapter adapter;
    private ObservableBoolean isSignedIn = new ObservableBoolean();

    public CreateNewGiftcardFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_create_new_giftcard, container, false);

        adapter = new TextViewAdapter(this, isSignedIn);

        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_id);
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
        setToolbarTitle(getString(R.string.create_giftcard));
        if (!myPrefs.hasUser() && !getHasAskedToSignIn().get()) {
            getHasAskedToSignIn().set(true);
            new SignInDialogHandler().startDialog(getContext());
        } else {
            if (signInHandler.isTokenExpired()) {
                signInHandler.refreshTokenAsync();
            }

            User user = GiiftyPreferences.getInstance().getUser();

//            if (user.getAccountNumber() == null) {
//                createDialog();
//            }
        }
    }

    private void createDialog() {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setMessage("Du skal tilføje et betalingskort før du kan tilføje et gavekort")
                .setNeutralButton("Tilføj kort", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityStarter.startUpdateUserActivity(getActivity());
                    }
                }).setCancelable(false)
                .create();

        dialog.show();
    }

    @Override
    public void onPause() {
        super.onPause();
        GiiftyApplication.getBus().unregister(this);
    }

    @Subscribe
    public void onSignedIn(SignedInEvent event) {
        isSignedIn.set(event.isSuccessful);
    }
}
