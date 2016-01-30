package android.giifty.dk.giifty;

import android.giifty.dk.giifty.Giftcard.Company;
import android.giifty.dk.giifty.Giftcard.GiftcardController;
import android.giifty.dk.giifty.user.User;
import android.giifty.dk.giifty.user.UserController;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

        final User testUser = new User(null, null, "mads_k", "mads", "madshgk@gmail.com", true, "40845645", false, "87483483783");


        Button loginButton = (Button) findViewById(R.id.login_button_id);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
                Log.d(TAG, "loginWithUser(), user:" + testUser);
                UserController.getInstance().loginWithUser();
            }
        });

        Button createUser = (Button) findViewById(R.id.create_user_id);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserController.getInstance().createUser(testUser);
            }
        });
    }
}
