package android.giifty.dk.giifty;

import android.content.Intent;
import android.giifty.dk.giifty.utils.GlobalObserver;
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
import android.widget.Toast;

import java.io.IOException;

import hugo.weaving.DebugLog;

public class FrontPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRONTPAGE_FRAGMENT = "frontpageFrag";
    private static final String TAG = FrontPageActivity.class.getSimpleName();
    private TextView naviHeaderName;
    private SignInHandler signInHandler;
    private View createUserHeader;
    private ImageView naviUserImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_buy_giftcards);
        createUserHeader = navigationView.getHeaderView(0);
        naviHeaderName = (TextView) createUserHeader.findViewById(R.id.user_name_id);
        naviUserImage = (ImageView)createUserHeader.findViewById(R.id.user_image_id);
        showFragment(R.id.nav_buy_giftcards);
        signInHandler = SignInHandler.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNaviHeader();
    }

    @DebugLog
    private void updateNaviHeader() {
        try {
            if (GlobalObserver.hasCurrentUser()) {
                naviHeaderName.setText(GlobalObserver.getCurrentUser().getName());
            //    Utils.setImage(this, naviUserImage, GlobalObserver.getCurrentUser().getFacebookProfileImageUrl());
                if (GlobalObserver.getCurrentUser().isAutoSignIn()) {
                    signInHandler.signInUser();
                }
            } else {
                naviHeaderName.setText(getString(R.string.user_name_create_user));
                createUserHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startCreateUserActivity();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startCreateUserActivity() {
        Toast.makeText(this, "TODO createUseractivity", Toast.LENGTH_SHORT).show();
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
        showFragment(id);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int fragId) {
        Fragment fragment = FragmentFactory.createFragment(fragId);
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

}
