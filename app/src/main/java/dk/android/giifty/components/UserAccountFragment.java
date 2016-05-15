package dk.android.giifty.components;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.squareup.otto.Subscribe;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.services.UserService;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.MyDialogBuilder;
import dk.android.giifty.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccountFragment extends Fragment implements TextWatcher, DialogInterface.OnClickListener {

    private static final String TAG = UserAccountFragment.class.getSimpleName();
    private EditText account, reg, cardholderName;
    private Button laterButton, saveAccountButton;
    private User user;

    public UserAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_account_frag, container, false);

        account = (EditText) root.findViewById(R.id.account_id);
        reg = (EditText) root.findViewById(R.id.reg_id);
        reg.addTextChangedListener(this);
        cardholderName = (EditText) root.findViewById(R.id.cardholder_name_id);
        laterButton = (Button) root.findViewById(R.id.later_button_id);
        laterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        saveAccountButton = (Button) root.findViewById(R.id.add_account_id);
        saveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAccountInfo();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        GiiftyApplication.getBus().unregister(this);
    }

    private void showTermsAndConditions() {
        MyDialogBuilder.createTermsAndConditionsDialog(getContext(), this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        user.setTermsAccepted((which == Dialog.BUTTON_POSITIVE));
        UserService.updateUser(getContext(), user);
    }

    private void saveAccountInfo() {
        user = GiiftyPreferences.getInstance().getUser();
        String cardHolder = cardholderName.getText().toString(),
                regNr = reg.getText().toString(),
                accountNr = account.getText().toString();

        if (!cardHolder.isEmpty() && !regNr.isEmpty() && !accountNr.isEmpty()) {
            accountNr = regNr + accountNr;
            user.setAccountNumber(accountNr);
            // TODO add cardholderName?
            showTermsAndConditions();
        } else {
            Utils.makeToast(getString(R.string.msg_all_fields_be_filled));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (reg.hasFocus() && s.toString().length() == 4) {
            cardholderName.requestFocus();
        }
    }

    @Subscribe
    public void onUserUpdated(UserUpdateEvent event) {
        if (event.isSuccessful) {
            getActivity().finish();
        }
    }
}
