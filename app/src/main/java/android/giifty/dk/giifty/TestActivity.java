package android.giifty.dk.giifty;


import android.giifty.dk.giifty.giftcard.GiftcardController;
import android.giifty.dk.giifty.model.Company;
import android.giifty.dk.giifty.model.User;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.Utils;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;

import java.util.List;

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
                Log.d(TAG, Utils.createAuthenticationHeader("akhil@test.dk:Akhil"));
                Log.d(TAG, Utils.createAuthenticationHeader("APP:so8Zorro"));
            }
        });
        Button getMain = (Button) findViewById(R.id.get_main_id);
        getMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Company> l = GiftcardController.getInstance().getMainView();
                Log.d(TAG, l.size() + " ");
            }
        });

        Button loginWithUser = (Button) findViewById(R.id.login_button_id);
        loginWithUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "loginWithUser()");
                UserController.getInstance().loginWithUser();
            }
        });

        Button createUser = (Button) findViewById(R.id.create_user_id);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    UserController.getInstance().createUser(testUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
