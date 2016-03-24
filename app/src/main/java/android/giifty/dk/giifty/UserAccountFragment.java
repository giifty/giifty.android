package android.giifty.dk.giifty;


import android.app.Dialog;
import android.content.DialogInterface;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.Utils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccountFragment extends Fragment implements TextWatcher, DialogInterface.OnClickListener {


    private EditText account, reg, cardholderName;
    private boolean isTermsAccepted;
    private Button laterButton, saveAccountButton;
    private TextView userName;
    private User user;

    public UserAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.user_account_frag, container, false);

        userName = (TextView) root.findViewById(R.id.user_name_id);
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
                showTermsAndConditions();
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        setValues();
    }

    private void setValues() {
        user = UserController.getInstance().getUser();
        userName.setText(user.getName());
    }

    private void showTermsAndConditions() {
        MyDialogBuilder.createTermsAndConditionsDialog(getContext(), this).show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        isTermsAccepted = (which == Dialog.BUTTON_POSITIVE);
        saveAccountInfo();
    }

    private void saveAccountInfo() {
        String cardHolder = cardholderName.getText().toString(),
                regNr = reg.getText().toString(),
                accountNr = account.getText().toString();

        if (!cardHolder.isEmpty() && !regNr.isEmpty() && !accountNr.isEmpty()) {
            accountNr = regNr + accountNr;
            user.setAccountNumber(accountNr);
            user.setTermsAccepted(isTermsAccepted);
            // TODO add cardholderName?
        }else{
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


}
