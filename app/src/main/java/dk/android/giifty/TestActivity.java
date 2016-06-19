package dk.android.giifty;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.joda.time.DateTime;

import dk.android.giifty.model.Company;
import dk.android.giifty.model.GiftcardRequest;
import dk.android.giifty.model.User;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.ActivityStarter;
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
                ActivityStarter.startCreateGiftcardActivity(TestActivity.this, new Company());
            }
        });

        Button startAct = (Button) findViewById(R.id.start_activity_id);
        startAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GiftcardRequest request = new GiftcardRequest();
                request.setGcImagePath("imagePath:/storage/emulated/0/Pictures/JPEG_2016_05_30_171118_-1987239166.jpg");
                request.getProperties().companyId = 1;
                request.getProperties().description = "beskrivelse er super";
                request.getProperties().expirationDateUtc = new DateTime().plusDays(376);
                request.getProperties().setPrice("240");
                request.getProperties().setValue("540");
                ActivityStarter.startPriceAndDescriptionActivity(TestActivity.this, request);
            }
        });
    }
}
