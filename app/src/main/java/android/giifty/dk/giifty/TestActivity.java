package android.giifty.dk.giifty;


import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.user.UpdatedUser;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.Utils;
import android.giifty.dk.giifty.web.SignInHandler;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by mak on 16-01-2016.
 */
public class TestActivity extends AppCompatActivity {

    private static final String TAG = TestActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        final User testUser = Utils.createFakeUser();

        Button loginButton = (Button) findViewById(R.id.test_stuff);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        Button getMain = (Button) findViewById(R.id.update_user_id);
        getMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdatedUser u = new UpdatedUser();
                u.name = "Mads Mads";
                u.phone ="33333333";
                try {
                    UserController.getInstance().updateUser(TestActivity.this, u);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Button loginWithUser = (Button) findViewById(R.id.login_button_id);
        loginWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "refreshTokenAsync()");
                try {
                    SignInHandler.getInstance().refreshTokenAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Button createUser = (Button) findViewById(R.id.create_user_id);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    UserController.getInstance().createUser(TestActivity.this, testUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
