package info.cameronlund.scout;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import info.cameronlund.scout.layout.EventListFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navDrawer;
    private Class homeFragmentClass = EventInfoFragment.class;
    private Toolbar toolbar;
    private ScoutUser user;
    private UserInfoListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO If there's already a logged in user, pass in instance and reload object
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startLogin();
            return;
        } else {
            listener = new UserInfoListener() {
                @Override
                public void onUserInfoChanged(ScoutUser user) {
                    notifyFragmentUser(getRunningFragment());
                    //navDrawer.getMenu().setGroupVisible(R.id.admin_tools, user.isAdmin());
                }
            };

            user = new ScoutUser(FirebaseAuth.getInstance().getCurrentUser(), listener);
        }

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                check:
                if (firebaseAuth.getCurrentUser() == null) {
                    ensureDeadUser();
                    startLogin();
                } else {
                    if (user != null)
                        if (user.getFirebaseUser().getUid()
                                .equalsIgnoreCase(firebaseAuth.getCurrentUser().getUid()))
                            break check;
                    user = new ScoutUser(firebaseAuth.getCurrentUser(), listener);
                }
            }
        });

        // -- Set up toolbar
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        /*if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowTitleEnabled(false);*/
        setTitle(getString(R.string.app_name));

        // -- Set up navigation drawer
        drawer = (DrawerLayout) findViewById(R.id.main_nav_layout);
        drawerToggle = setupDrawerToggle();
        drawer.addDrawerListener(drawerToggle);
        navDrawer = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navDrawer);

        // -- Set up the current fragment if there isn't one
        if (getRunningFragment() == null) {
            Fragment fragment = null;
            try {
                fragment = createFragment(homeFragmentClass);
            } catch (Exception e) {
                Log.e("Partbase", "Failed to load fragment", e);
                // TODO Figure out what to do here... we have no content in this case
            }

            if (fragment != null)
                setMainFragment(fragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!drawerToggle.onOptionsItemSelected(item)) {
            if (item.getItemId() == R.id.logout) {
                FirebaseAuth.getInstance().signOut();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    private Fragment createFragment(Class fragmentClass) throws IllegalAccessException,
            InstantiationException {
        try {
            return (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            Log.e("Test", "Failed to create a fragment!", e);
            throw e;
        }
    }

    private Fragment getRunningFragment() {
        FragmentManager fragManager = getSupportFragmentManager();
        return fragManager.findFragmentById(R.id.main_fragment_content);
    }

    private void setMainFragment(Fragment fragment) {
        notifyFragmentUser(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_content, fragment).commit();
    }

    public void notifyFragmentUser(Fragment fragment) {
        if (fragment instanceof Userable)
            ((Userable) fragment).updateCurrentUser(user);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.menu_nav_home:
                fragmentClass = homeFragmentClass;
                break;
            case R.id.menu_nav_teams:
                fragmentClass = EventListFragment.class;
                break;
            default:
                fragmentClass = homeFragmentClass;
        }

        try {
            fragment = createFragment(fragmentClass);
        } catch (Exception e) {
            Log.e("Partbase", "Failed to load fragment", e);
            // TODO Figure out what to do here... we have no content in this case
        }

        setMainFragment(fragment);
        menuItem.setChecked(true);

        if (menuItem.getItemId() == R.id.menu_nav_home)
            setTitle(getString(R.string.app_name));
        else
            setTitle(menuItem.getTitle());

        drawer.closeDrawers();
    }

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) { // Login
            if (resultCode == RESULT_OK) {
                if (data.getIntExtra("result", 1) != 0)
                    finish();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        ensureDeadUser();
    }

    private void ensureDeadUser() {
        if (user != null) {
            user.destroy();
            user = null;
        }
    }

    public void eventCardClicked(View view) {
        if (getRunningFragment() instanceof EventClickable)
            ((EventClickable) getRunningFragment()).eventCardClicked(view);
    }
}
