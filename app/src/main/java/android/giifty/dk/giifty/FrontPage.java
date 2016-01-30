package android.giifty.dk.giifty;

import android.content.Intent;
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

public class FrontPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String FRONTPAGE_FRAGMENT = "frontpageFrag";
    private static final String TAG = FrontPage.class.getSimpleName();

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
        showFragment(R.id.nav_buy_giftcards);
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

        //do we need to know what is pressed here?
        if (id == R.id.nav_buy_giftcards) {
            // Handle the camera action

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
        Fragment fragment = FragmentFactory.getFragmentByID(fragId);
        Log.d(TAG, "showFragment() name:" + fragment.getClass().getSimpleName());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container_id, fragment, FRONTPAGE_FRAGMENT)
                .addToBackStack(FRONTPAGE_FRAGMENT)
                .commit();
    }

    private void popFromBackstack(){
        getSupportFragmentManager().popBackStack();
         }

}
