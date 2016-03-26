package dk.android.giifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import dk.android.giifty.user.UserRepository;
import dk.android.giifty.utils.Utils;

public class CreateUserActivity extends AppCompatActivity implements UserInfoFragment.OnFragmentInteractionListener {

    private ImageView userImage;
    private UserRepository userRepository;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        View closeButton = findViewById(R.id.close_id);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userName = (TextView) findViewById(R.id.user_name_id);
        userImage = (ImageView) findViewById(R.id.user_image_id);
        userRepository = UserRepository.getInstance();
        showFragment(new UserInfoFragment());

    }

    @Override
    public void onFacebookProfileFetched(String facebookImageUrl) {

        if (facebookImageUrl != null) {
            Utils.setUserImage(this, userImage, facebookImageUrl);
        }
    }

    @Override
    public void onShowAccountFragment() {
        userName.setText(userRepository.getUser().getName());
        Utils.setUserImage(this, userImage, userRepository.getUser().getFacebookProfileImageUrl());
        showFragment(new UserAccountFragment());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id, fragment).commit();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.fade_in, R.animator.slide_out_right);
    }
}
