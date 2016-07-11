package dk.android.giifty;

import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import dk.android.giifty.busevents.SignedInEvent;
import dk.android.giifty.busevents.UserUpdateEvent;
import dk.android.giifty.drawer.DrawerFragment;
import dk.android.giifty.model.User;
import dk.android.giifty.services.MyGiftcardService;
import dk.android.giifty.services.MyPurchasedGiftcardService;
import dk.android.giifty.signin.SignInDialogHandler;
import dk.android.giifty.signin.SignInHandler;
import dk.android.giifty.utils.ActivityStarter;
import dk.android.giifty.utils.Constants;
import dk.android.giifty.utils.GiiftyPreferences;
import dk.android.giifty.utils.Utils;

public class FrontPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, DrawerFragment.OnDrawerFragmentInteraction {

    private static final String FRONTPAGE_FRAGMENT = "frontpageFrag";
    private static final String TAG = FrontPageActivity.class.getSimpleName();
    private static final String EXTRA_CURRENT_FRAG = "current_fragment";
    private TextView naviHeaderName;
    private SignInHandler signInHandler;
    private View createUserHeader;
    private ImageView naviUserImage;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private GiiftyPreferences myPrefs;
    private int currentFrag;
    private ObservableBoolean hasAskedToSignIn = new ObservableBoolean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentFrag = savedInstanceState.getInt(EXTRA_CURRENT_FRAG, -1);
        } else {
            currentFrag = getIntent().getIntExtra(Constants.EKSTRA_FRAGMENT_ID, -1);
        }

        setContentView(R.layout.activity_front_page);
        myPrefs = GiiftyPreferences.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        createUserHeader = navigationView.getHeaderView(0);
        View signOutButton = createUserHeader.findViewById(R.id.sign_out_id);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
        naviHeaderName = (TextView) createUserHeader.findViewById(R.id.user_name_id);
        naviUserImage = (ImageView) createUserHeader.findViewById(R.id.user_image_id);
        naviHeaderName.setOnClickListener(this);
        naviUserImage.setOnClickListener(this);

        signInHandler = SignInHandler.getInstance();
    }

    private void signOut() {
        myPrefs.clearUserGiftcards();
        signInHandler.setServerToken(null);
        deleteUser();
    }

    public void deleteUser() {
        Log.d(TAG, "deleteUser()");
        GiiftyPreferences.getInstance().clearUser();
        GiiftyApplication.getBus().post(new UserUpdateEvent(null, true));
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNaviHeader();
        GiiftyApplication.getBus().register(this);
        if (currentFrag == -1) {
            showDefaultView();
        } else {
            showSpecificView(currentFrag);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_CURRENT_FRAG, currentFrag);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GiiftyApplication.getBus().unregister(this);
    }

    @Override
    public ObservableBoolean getHasAskedToSignIn(){
        return hasAskedToSignIn;
    }
    @Override
    public void showSpecificView(int id) {
        showFragment(id);
        navigationView.setCheckedItem(id);
    }

    private void showDefaultView() {
        showFragment(R.id.nav_buy_giftcards);
        navigationView.setCheckedItem(R.id.nav_buy_giftcards);
    }

    private void updateNaviHeader() {
        if (!signInHandler.isTokenExpired()) {
            User user = myPrefs.getUser();
            naviHeaderName.setText(user.getName());
            Utils.setUserImage(this, naviUserImage, user.getFacebookProfileImageUrl());
        } else {
            naviHeaderName.setText(getString(R.string.sign_in_text));
            signInHandler.refreshTokenAsync();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frontpage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.test_activity_id) {
            startActivity(new Intent(this, TestActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        item.setEnabled(true);
        //do we need to know what is pressed here?
        if (id == R.id.nav_buy_giftcards) {

        } else if (id == R.id.nav_create_giftcards) {

        } else if (id == R.id.nav_bought_giftcards) {

        } else if (id == R.id.nav_my_giftcards) {

        } else if (id == R.id.nav_nav_settings) {

        }
        showFragment(id);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    private void showFragment(int fragId) {
        currentFrag = fragId;
        Fragment fragment = FragmentFactory.createFragment(fragId, this);
        Log.d(TAG, "showFragment() name:" + fragment.getClass().getSimpleName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_id, fragment, FRONTPAGE_FRAGMENT)
                .addToBackStack(FRONTPAGE_FRAGMENT)
                .commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.user_name_id || id == R.id.user_image_id) {
            if (myPrefs.hasUser()) {
                ActivityStarter.startUpdateUserActivity(FrontPageActivity.this);
            } else {
                new SignInDialogHandler().startDialog(FrontPageActivity.this);
            }
        }
    }

    @Subscribe
    public void onUserUpdated(UserUpdateEvent event) {
        if (event.isSuccessful) {
            updateNaviHeader();
        }
    }

    @Subscribe
    public void onSignedIn(SignedInEvent event) {
        if (event.isSuccessful) {
            updateNaviHeader();
            MyPurchasedGiftcardService.fetchMyPurchasedGiftcards(this);
            MyGiftcardService.fetchMyGiftcards(this);
        }
    }
}
