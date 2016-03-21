package android.giifty.dk.giifty;

import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.user.UserController;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;

public class CreateUserActivity extends AppCompatActivity implements TextWatcher {

    private EditText fullName, email, password, passwordRep, phone, reg, account;
    private Button createUserButton, getFacebookInfo;
    private ImageView userImage;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        fullName = (EditText) findViewById(R.id.name_id);
        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password_id);
        passwordRep = (EditText) findViewById(R.id.password_rep_id);
        phone = (EditText) findViewById(R.id.phone_id);
        account = (EditText) findViewById(R.id.account_id);
        reg = (EditText) findViewById(R.id.reg_id);
        reg.addTextChangedListener(this);

        createUserButton = (Button) findViewById(R.id.create_user_button_id);
        getFacebookInfo = (Button) findViewById(R.id.facebook_button_id);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUSer();
            }
        });

        userImage = (ImageView) findViewById(R.id.user_image_id);
        userController = UserController.getInstance();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initActivity() {
        User user;
        if(userController.hasUser()){
            user = userController.getUser();
            fullName.setText(user.getName());
        }

    }


    private void createUSer() {

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
                try {
                    userController.createUser(this, name, phoneNr, emailAdd, pass, acc);
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

    private void showUserMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean comparePasswords() {
        return !password.getText().toString().isEmpty() && password.getText().toString().trim().contentEquals(passwordRep.getText().toString().trim());
    }

    //TODO verify account?
    private boolean verifyAccount(){
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
        if(reg.hasFocus() && s.toString().length() == 4){
            account.requestFocus();
        }else if(phone.hasFocus() && s.toString().length() == 8 ){
            reg.requestFocus();
        }
    }
}
