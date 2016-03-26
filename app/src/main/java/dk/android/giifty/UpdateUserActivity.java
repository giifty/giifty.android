package dk.android.giifty;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;

import org.json.JSONException;

import dk.android.giifty.broadcastreceivers.MyBroadcastReceiver;
import dk.android.giifty.model.User;
import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Broadcasts;
import dk.android.giifty.utils.FacebookSignInHandler;
import dk.android.giifty.utils.Utils;

public class UpdateUserActivity extends AppCompatActivity implements TextWatcher, FacebookCallback<LoginResult> {

    private EditText fullName, email, password, passwordRep, phone, reg, account, cardHolderName;
    private ImageView userImage;
    private UserRepository userRepository;
    private User user;
    private MyReceiver myReceiver;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Utils.initFacebookSdk();

        fullName = (EditText) findViewById(R.id.name_id);
        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password_id);
        passwordRep = (EditText) findViewById(R.id.password_rep_id);
        phone = (EditText) findViewById(R.id.phone_id);
        phone.addTextChangedListener(this);
        account = (EditText) findViewById(R.id.account_id);
        reg = (EditText) findViewById(R.id.reg_id);
        reg.addTextChangedListener(this);
        //TODO cardholder related stuff
        cardHolderName = (EditText) findViewById(R.id.cardholder_name_id);
        Button createUserButton = (Button) findViewById(R.id.create_user_button_id);
        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        Button getFacebookInfo = (Button) findViewById(R.id.facebook_button_id);
        getFacebookInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackManager = new FacebookSignInHandler().signInWithFb(UpdateUserActivity.this, UpdateUserActivity.this);
            }
        });

        View closeButton = findViewById(R.id.close_id);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userImage = (ImageView) findViewById(R.id.user_image_id);
        userRepository = UserRepository.getInstance();
        myReceiver = new MyReceiver();
        registerReceiver(myReceiver, new IntentFilter(Broadcasts.USER_UPDATED_FILTER));
    }


    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    private void setValues() {
        if (userRepository.hasUser()) {
            Utils.setUserImage(this, userImage, userRepository.getUser().getFacebookProfileImageUrl());
            user = userRepository.getUser();
            fullName.setText(user.getName());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            password.setText(user.getPassword());
            passwordRep.setText(user.getPassword());

            if (user.getAccountNumber() != null) {
                String[] splitString = user.getAccountNumber().split(" ");
                reg.setText(splitString[0]);
                account.setText(splitString[0]);
            }
        }
    }


    private void createUser() {

        String name, phoneNr, emailAdd, acc, regNr, pass, passRep;

        name = fullName.getText().toString();
        emailAdd = email.getText().toString();
        phoneNr = phone.getText().toString();
        pass = password.getText().toString();
        passRep = passwordRep.getText().toString();
        acc = account.getText().toString();
        regNr = reg.getText().toString();

        if (!name.isEmpty() || !emailAdd.isEmpty() || !phoneNr.isEmpty() ||
                !pass.isEmpty() || !passRep.isEmpty() || !acc.isEmpty() || !regNr.isEmpty()) {

            if (comparePasswords()) {
                acc = regNr + acc;
                if (user == null) {
                    user = new User();
                }
                user.setName(name);
                user.setEmail(emailAdd);
                user.setPhone(phoneNr);
                user.setAccountNumber(acc);
                user.setPassword(pass);

                try {
                    userRepository.updateUser(this, user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Utils.makeToast(getString(R.string.msg_password_mismatch));
            }
        } else {
            Utils.makeToast(getString(R.string.msg_all_fields_be_filled));
        }

    }

    private boolean comparePasswords() {
        return !password.getText().toString().isEmpty() && password.getText().toString().trim().contentEquals(passwordRep.getText().toString().trim());
    }

    //TODO verify account?
    private boolean verifyAccount() {
        return true;
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
            account.requestFocus();
        } else if (phone.hasFocus() && s.toString().length() == 8) {
            reg.requestFocus();
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        Profile.fetchProfileForCurrentAccessToken();
        Profile.getCurrentProfile();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onError(FacebookException error) {
          Utils.makeToast(getString(R.string.msg_facebook_error));
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onUserUpdated() {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.fade_in, R.animator.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }
}
