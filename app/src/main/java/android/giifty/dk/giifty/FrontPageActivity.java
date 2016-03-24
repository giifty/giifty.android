package android.giifty.dk.giifty;

import android.content.Intent;
import android.giifty.dk.giifty.user.UserController;
import android.giifty.dk.giifty.utils.ActivityStarter;
import android.giifty.dk.giifty.web.SignInHandler;
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

import java.io.IOException;

import hugo.weaving.DebugLog;

public class FrontPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String FRONTPAGE_FRAGMENT = "frontpageFrag";
    private static final String TAG = FrontPageActivity.class.getSimpleName();
    private TextView naviHeaderName;
    private SignInHandler signInHandler;
    private View createUserHeader;
    private ImageView naviUserImage;
    private UserController userController;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
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
        naviHeaderName = (TextView) createUserHeader.findViewById(R.id.user_name_id);
        naviUserImage = (ImageView) createUserHeader.findViewById(R.id.user_image_id);
        naviHeaderName.setOnClickListener(this);
        naviUserImage.setOnClickListener(this);

        userController = UserController.getInstance();
        signInHandler = SignInHandler.getInstance();
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateNaviHeader();
        showDefaultView();
    }

    private void showDefaultView() {
        toolbar.setTitle(getString(R.string.buy_giftcard));
        showFragment(R.id.nav_buy_giftcards);
        navigationView.setCheckedItem(R.id.nav_buy_giftcards);
    }

    @DebugLog
    private void updateNaviHeader() {
        try {
            if (userController.hasUser()) {
                naviHeaderName.setText(userController.getUser().getName());
                //  Utils.setImage(this, naviUserImage, GlobalObserver.getUser().getFacebookProfileImageUrl());
                signInHandler.refreshTokenAsync();
            } else {
                naviHeaderName.setText(getString(R.string.user_name_create_user));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
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
        setToolbarTitle(item.getTitle().toString());
        showFragment(id);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    private void showFragment(int fragId) {
        Fragment fragment = FragmentFactory.createFragment(fragId, this);
        Log.d(TAG, "showFragment() name:" + fragment.getClass().getSimpleName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_id, fragment, FRONTPAGE_FRAGMENT)
                .addToBackStack(FRONTPAGE_FRAGMENT)
                .commit();
    }

    private void popFromBackstack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.user_name_id || id == R.id.user_image_id) {
            ActivityStarter.startCreateUserActivity(FrontPageActivity.this);
        }

    }
}
