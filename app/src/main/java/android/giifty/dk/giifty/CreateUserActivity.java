package android.giifty.dk.giifty;

import android.content.BroadcastReceiver;
import android.giifty.dk.giifty.broadcastreceivers.MyBroadcastReceiver;
import android.giifty.dk.giifty.user.UserController;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class CreateUserActivity extends AppCompatActivity implements UserInfoFragment.OnFragmentInteractionListener {

    private BroadcastReceiver myReceiver;
    private ImageView userImage;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        userImage = (ImageView) findViewById(R.id.user_image_id);
        userController = UserController.getInstance();
        showFragment(new UserInfoFragment());

    }

    @Override
    public void onFragmentInteraction() {
        showFragment(new UserAccountFragment());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_id, fragment).commit();
    }

    class MyReceiver extends MyBroadcastReceiver {

        @Override
        public void onUserUpdated() {

        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.animator.fade_in, R.animator.slide_out_right);
    }
}
