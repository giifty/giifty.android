package dk.android.giifty.components;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import dk.android.giifty.R;
import dk.android.giifty.utils.ActivityStarter;

/**
 * Created by mak on 24-04-2016.
 */
public class Test extends View {

    private EditText email, password;
    private TextView create_new;
    private Button signIn;

    public Test(Context context) {
        super(context);
    }

    public Test(Activity activity) {
        super(activity);
        initView(activity);
    }


    public void initView(final Activity activity) {
        inflate(activity, R.layout.signin_user_dialog_layout, null);
        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password_id);
        signIn = (Button) findViewById(R.id.signin_button_id);
        signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        create_new = (TextView) findViewById(R.id.create_user_id);
        create_new.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityStarter.startCreateUserActivity(activity);
            }
        });
    }

    private void signin(){

    }

    private String getEmail() {
        return email.getText().toString();
    }

    private String getPassword() {
        return password.getText().toString();
    }


}
