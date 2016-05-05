package dk.android.giifty.components;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

import dk.android.giifty.R;
import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.model.User;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Broadcasts;
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
    private UserRepository usercontroller;
    private MyReceiver myReceiver;

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
        usercontroller = UserRepository.getInstance();
        myReceiver = new MyReceiver();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
         LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, new IntentFilter(Broadcasts.USER_UPDATED_FILTER));
    }

    private void showTermsAndConditions() {
        MyDialogBuilder.createTermsAndConditionsDialog(getContext(), this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        user.setTermsAccepted((which == Dialog.BUTTON_POSITIVE));
        try {
            usercontroller.updateUser(getContext(), user);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveAccountInfo() {
        user = usercontroller.getUser();
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

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onUserUpdated() {
            Log.d(TAG, "onUserUpdated()");
            //TODO should this just close, or do we need some feedback in case of failed reqeust
            getActivity().finish();
        }
    }
}
