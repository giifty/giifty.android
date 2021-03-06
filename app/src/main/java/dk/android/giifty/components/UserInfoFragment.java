package dk.android.giifty.components;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import dk.android.giifty.GiiftyApplication;
import dk.android.giifty.R;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.services.UserService;
import dk.android.giifty.utils.FacebookSignInHandler;
import dk.android.giifty.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class UserInfoFragment extends Fragment implements TextWatcher, FacebookCallback<LoginResult>, GraphJSONObjectCallback {

    private OnFragmentInteractionListener parent;
    private CallbackManager callbackManager;
    private FacebookSignInHandler facebookSignInHandler;
    private String facebookImageUrl;
    private EditText fullName, email, password, passwordRep, phone;
    private User user;
    private static final String TAG = UserInfoFragment.class.getSimpleName();

    public UserInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.user_info_frag, container, false);
        Utils.initFacebookSdk();

        fullName = (EditText) root.findViewById(R.id.name_id);
        email = (EditText) root.findViewById(R.id.email_id);
        password = (EditText) root.findViewById(R.id.password_id);
        passwordRep = (EditText) root.findViewById(R.id.password_rep_id);
        phone = (EditText) root.findViewById(R.id.phone_id);
        phone.addTextChangedListener(this);
        Button createUserButton = (Button) root.findViewById(R.id.create_user_button_id);
        Button getFacebookInfo = (Button) root.findViewById(R.id.facebook_button_id);
        facebookSignInHandler = new FacebookSignInHandler();
        getFacebookInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = facebookSignInHandler.signInWithFb(UserInfoFragment.this, UserInfoFragment.this);
            }
        });

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUSer();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
                user.setFacebookProfileImageUrl(facebookImageUrl);

                UserService.updateUser(getContext(), user);
            } else {
                Utils.makeToast(getString(R.string.msg_password_mismatch));
            }
        } else {
            Utils.makeToast(getString(R.string.msg_all_fields_be_filled));
        }
    }

    private boolean comparePasswords() {
        return (!password.getText().toString().isEmpty() &&
                !passwordRep.getText().toString().isEmpty() &&
                password.getText().toString().trim().contentEquals(passwordRep.getText().toString().trim()));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            parent = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
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

    @Override
    public void onSuccess(LoginResult loginResult) {
        facebookSignInHandler.fetchUserData(loginResult.getAccessToken(), this);
    }

    @Override
    public void onCancel() {
        Utils.makeToast(getString(R.string.msg_facebook_error));
    }

    @Override
    public void onError(FacebookException error) {
        Utils.makeToast(getString(R.string.msg_facebook_error));
    }

    @Override
    public void onCompleted(JSONObject object, GraphResponse response) {
        Log.d("Graph request:", object.toString());
        setValues(object);

    }

    private void setValues(JSONObject profile) {

        try {
            facebookImageUrl = Profile.getCurrentProfile().getProfilePictureUri(80, 90).toString();
            parent.onFacebookProfileFetched(facebookImageUrl);
            fullName.setText(profile.getString("name"));
            email.setText(profile.getString("email"));
            //  facebookId = profile.getString("id");

        } catch (JSONException e) {
            Utils.makeToast(getString(R.string.msg_facebook_error));
            e.printStackTrace();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFacebookProfileFetched(String facebookImageUrl);

        void onShowAccountFragment();
    }

    @Subscribe
    public void onUserUpdated(UserUpdateEvent event) {
        if (event.isSuccessful) {
            parent.onShowAccountFragment();
        } else {
            Snackbar.make(email, R.string.msg_create_user_error, Snackbar.LENGTH_LONG).show();
        }
    }

}
