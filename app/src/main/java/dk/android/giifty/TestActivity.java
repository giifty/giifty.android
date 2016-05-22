package dk.android.giifty;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import dk.android.giifty.model.User;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.Utils;

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
            }
        });

        Button loginWithUser = (Button) findViewById(R.id.login_button_id);
        loginWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "refreshTokenAsync()");
                    SignInHandler.getInstance().refreshTokenAsync();

            }
        });

        Button createUser = (Button) findViewById(R.id.create_user_id);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button deleteUser = (Button) findViewById(R.id.delete_user_id);
        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        Button startAct = (Button) findViewById(R.id.start_activity_id);
        startAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              startActivity(new Intent(TestActivity.this, PriceAndDescriptionActivity.class));
            }
        });
    }
}
