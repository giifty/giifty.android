package dk.android.giifty;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.squareup.otto.Subscribe;

import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.model.User;
import dk.android.giifty.services.UserService;
import dk.android.giifty.utils.FacebookSignInHandler;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.Utils;

public class UpdateUserActivity extends AppCompatActivity implements TextWatcher, FacebookCallback<LoginResult> {

    private EditText fullName, email, password, passwordRep, phone, reg, account, cardHolderName;
    private ImageView userImage;
    private User user;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        GiiftyApplication.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setValues();
    }

    private void setValues() {
        User user = GiiftyPreferences.getInstance().getUser();
        if (user != null) {
            Utils.setUserImage(this, userImage, user.getFacebookProfileImageUrl());
            fullName.setText(user.getName());
            email.setText(user.getEmail());
            phone.setText(user.getPhone());
            password.setText(user.getPassword());
            passwordRep.setText(user.getPassword());

            if (user.getAccountNumber() != null) {
                String accountNr = user.getAccountNumber();
                reg.setText(accountNr.substring(0, 4));
                account.setText(accountNr.substring(4, accountNr.length()));
                cardHolderName.setText(user.getCardholderName());
            }
        }
    }

    private void createUser() {
        String name, phoneNr, emailAdd, acc, regNr, cardHolder, pass, passRep;
        name = fullName.getText().toString();
        emailAdd = email.getText().toString();
        phoneNr = phone.getText().toString();
        pass = password.getText().toString();
        passRep = passwordRep.getText().toString();
        acc = account.getText().toString();
        regNr = reg.getText().toString();
        cardHolder = cardHolderName.getText().toString();
        if (validateFields()) {

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
                user.setCardholderName(cardHolder);
                UserService.updateUser(this, user);
            } else {
                Utils.makeToast(getString(R.string.msg_password_mismatch));
            }
        } else {
            Utils.makeToast(getString(R.string.msg_all_fields_be_filled));
        }
    }

    private boolean validateFields() {
        boolean isValid = validateIsNonEmpty(fullName)
                && validateIsNonEmpty(password)
                && validateIsNonEmpty(passwordRep)
                && validateEmail(email);

       if(!isValid){
           return false;
       }

        if (!comparePasswords()) {
            return false;
        }

        boolean accountValid = true;

        if (!TextUtils.isEmpty(account.getText())) {
            accountValid = validateIsNonEmpty(reg)
                    && validateIsNonEmpty(cardHolderName);
        }

        return accountValid;
    }

    private boolean validateEmail(TextView view) {
        boolean valid = android.util.Patterns.EMAIL_ADDRESS.matcher(view.getText()).matches();
        if (!valid) {
            view.requestFocus();
            setErrorOnField(view, getString(R.string.not_valid_email));
        }
        return valid;
    }

    private boolean validateIsNonEmpty(TextView view) {
        boolean valid = !TextUtils.isEmpty(view.getText());
        if (!valid) {
            view.requestFocus();
            setErrorOnField(view, getString(R.string.missing_input));
        }
        return valid;
    }

    private void setErrorOnField(TextView view, String msg) {
        view.setError(msg);
    }

    private boolean comparePasswords() {
        boolean valid = password.getText().toString().trim().contentEquals(passwordRep.getText().toString().trim());
        if (!valid) {
            setErrorOnField(passwordRep, getString(R.string.msg_password_mismatch));
        }
        return valid;
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

    @Subscribe
    public void onUserUpdated(UserUpdateEvent event) {
        if (event.isSuccessful) {
            GiiftyPreferences.getInstance().persistUser(event.user);
            finish();
        } else {
            Snackbar.make(account, R.string.user_update_error_msg, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.fade_in, R.animator.slide_out_right);
    }

}
