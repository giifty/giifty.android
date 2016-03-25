package dk.android.giifty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import dk.android.giifty.user.UserController;
import dk.android.giifty.utils.Utils;

public class CreateUserActivity extends AppCompatActivity implements UserInfoFragment.OnFragmentInteractionListener {

    private ImageView userImage;
    private UserController userController;
    private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        userName = (TextView) findViewById(R.id.user_name_id);
        userImage = (ImageView) findViewById(R.id.user_image_id);
        userController = UserController.getInstance();
        showFragment(new UserInfoFragment());

    }

    @Override
    public void onFragmentInteraction(String facebookImageUrl) {

        if (facebookImageUrl != null) {
            Utils.setUserImage(this, userImage, facebookImageUrl);
        } else if (userController.hasUser()) {
            userName.setText(userController.getUser().getName());
            Utils.setUserImage(this, userImage, userController.getUser().getFacebookProfileImageUrl());
            showFragment(new UserAccountFragment());
        }
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
