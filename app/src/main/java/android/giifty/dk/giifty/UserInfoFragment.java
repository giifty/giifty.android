package android.giifty.dk.giifty;

import android.content.Context;
import android.content.IntentFilter;
import android.giifty.dk.giifty.broadcastreceivers.MyBroadcastReceiver;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.Broadcasts;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class UserInfoFragment extends Fragment implements TextWatcher {

    private OnFragmentInteractionListener mListener;
    private MyReceiver myReceiver;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    private EditText fullName, email, password, passwordRep, phone;
    private Button createUserButton, getFacebookInfo;
    private ImageView userImage;
    private UserController userController;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.user_info_frag, container, false);

        fullName = (EditText) root.findViewById(R.id.name_id);
        email = (EditText) root.findViewById(R.id.email_id);
        password = (EditText) root.findViewById(R.id.password_id);
        passwordRep = (EditText) root.findViewById(R.id.password_rep_id);
        phone = (EditText) root.findViewById(R.id.phone_id);
        phone.addTextChangedListener(this);
        createUserButton = (Button) root.findViewById(R.id.create_user_button_id);
        getFacebookInfo = (Button) root.findViewById(R.id.facebook_button_id);
        getFacebookInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUSer();
            }
        });
        userController = UserController.getInstance();
        myReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, new IntentFilter(Broadcasts.USER_UPDATED_FILTER));
        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
    }

    private void createUSer() {

        String name, phoneNr, emailAdd, pass, passRep;

        name = fullName.getText().toString();
        emailAdd = email.getText().toString();
        phoneNr = phone.getText().toString();
        pass = password.getText().toString();
        passRep = passwordRep.getText().toString();


        if (!name.isEmpty() || !emailAdd.isEmpty() || !phoneNr.isEmpty() ||
                !pass.isEmpty() || !passRep.isEmpty()) {

            if (comparePasswords()) {

                if (user == null) {
                    user = new User();
                }
                user.setName(name);
                user.setEmail(emailAdd);
                user.setPhone(phoneNr);
                user.setPassword(pass);

                try {
                    userController.updateUser(getContext(), user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showUserMsg("Dine passwords matcher ikke");
            }
        } else {
            showUserMsg("Alle felter skal udfyldes");
        }

    }

    private boolean comparePasswords() {
        return (!password.getText().toString().isEmpty() && password.getText().toString().trim().contentEquals(passwordRep.getText().toString().trim()));
    }

    private void showUserMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (phone.hasFocus() && s.toString().length() == 8) {
            password.requestFocus();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    public void notifyActivity() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onUserUpdated() {
            if (userController.hasUser()){
                notifyActivity();
            }
        }
    }
}
