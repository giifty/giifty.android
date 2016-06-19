package dk.android.giifty.signin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import dk.android.giifty.R;

/**
 * Created by mak on 24-04-2016.
 */
public class SignInDialogLayout extends RelativeLayout {

    private EditText email, password;
    private TextView create_new;
    private Button signIn;
    private ProgressBar progressbar;

    public SignInDialogLayout(Context context) {
        super(context);
        initView();
    }

    public SignInDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SignInDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.signin_user_dialog_layout, this);
        email = (EditText) findViewById(R.id.email_id);
        password = (EditText) findViewById(R.id.password_id);
        progressbar = (ProgressBar) findViewById(R.id.progressBar_id);
        signIn = (Button) findViewById(R.id.signin_button_id);
        signIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        create_new = (TextView) findViewById(R.id.create_user_id);
    }

    public void switchVisibility() {
        progressbar.setVisibility(progressbar.getVisibility() == VISIBLE ? INVISIBLE : VISIBLE);
        signIn.setVisibility(signIn.getVisibility() == VISIBLE ? INVISIBLE : VISIBLE);
    }

    private void signIn() {
        switchVisibility();
        SignInHandler.getInstance().refreshWithParams(getEmail(), getPassword());
    }

    private String getEmail() {
        return email.getText().toString();
    }

    private String getPassword() {
        return password.getText().toString();
    }

    private void animateButton() {
    }

    public void setOnCreateUserListener(View.OnClickListener onClickListener) {
        create_new.setOnClickListener(onClickListener);
    }
}
