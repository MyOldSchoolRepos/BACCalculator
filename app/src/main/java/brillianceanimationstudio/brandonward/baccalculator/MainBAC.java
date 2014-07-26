package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import brillianceanimationstudio.brandonward.baccalculator.domain.*;

import com.google.android.gms.ads.*;

import java.util.Calendar;


public class MainBAC extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, WelcomeScreenFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener, BldAlcCntntCalculation.OnFragmentInteractionListener {

    private InterstitialAd interstitial;
    /* Your ad unit id. Replace with your actual ad unit id. */
    private static final String AD_UNIT_ID = "ca-app-pub-1321769086734378/4674749647";
    private String USER_STATE;
    private String VISIBLE_FRAGMENT_TAG;
    // Count until next Full Screen Ad is shown //
    private int adCount;//persisted as adCount
    private AdRequest adRequest;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private userInfo userInfo = new userInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Show EULA for new users and updates //
        new SimpleEula(this).show();
        USER_STATE = getUserInfo().getPrefsKey();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String user_state = prefs.getString(USER_STATE, "");
        userInfo = getUserInfo().readPrefsString(user_state);
        Calendar runtime = Calendar.getInstance();
        if (userInfo.gettMinute()+ userInfo.gettHour()+ userInfo.getLastDay()+userInfo.getLastMonth()+userInfo.getLastYear()==0){
            userInfo.settMinute(runtime.get(Calendar.MINUTE));
            userInfo.settHour(runtime.get(Calendar.HOUR));
            userInfo.setLastDay(runtime.get(Calendar.DAY_OF_MONTH));
            userInfo.setLastMonth(runtime.get(Calendar.MONTH));
            userInfo.setLastYear(runtime.get(Calendar.YEAR));
        }
        else if (runtime.get(Calendar.YEAR) - userInfo.getLastYear() <= 1 ) {
            if (runtime.get(Calendar.MONTH) - userInfo.getLastMonth() <= 1) {
                if (!((runtime.get(Calendar.HOUR) + runtime.get(Calendar.DAY_OF_MONTH) * 24) - (userInfo.gettHour() + userInfo.getLastDay()*24) < 24)) {
                    userInfo.settMinute(runtime.get(Calendar.MINUTE));
                    userInfo.settHour(runtime.get(Calendar.HOUR));
                    userInfo.setLastDay(runtime.get(Calendar.DAY_OF_MONTH));
                    userInfo.setLastMonth(runtime.get(Calendar.MONTH));
                    userInfo.setLastYear(runtime.get(Calendar.YEAR));
                    userInfo.setDrinks(0.0);
                }
            }
        }
        setContentView(R.layout.activity_main_bac);

        // Create the interstitial //
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AD_UNIT_ID);

        // Create ad request //
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("EE05288C46F855BD6717EE11B4F13249")
                .build();
        this.adRequest = adRequest;

        // Begin loading interstitial
        interstitial.loadAd(adRequest);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, WelcomeScreenFragment.newInstance(getUserInfo()))
                .commit();


        adCount = prefs.getInt("adCount", 0);
        adCount++;
        if (adCount > 100) {
            adCount = 0;
        }
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("adCount", adCount).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment newFragment = new WelcomeScreenFragment();
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                newFragment = new WelcomeScreenFragment().newInstance(userInfo);
                break;
            case 1:
                newFragment = new StatsFragment().newInstance(userInfo);// TODO: Make all of these that need info use newInstance();
                break;
            case 2:
                newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
                break;

        }
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        if (adCount % 3 == 2) {
            displayInterstitial();
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            adCount++;
            editor.putInt("adCount", adCount).apply();
        }
    }

    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_bac, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void changeDrinkCntByBtn(userInfo userInfo) {
        if (userInfo != null){
            this.userInfo = userInfo;
        }
        else {
            userInfo = this.userInfo;
        }
        saveUserInfoToSharedPrefs(userInfo);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
    }

    @Override
    public void timeWasChanged(userInfo userInfo) {
        if (userInfo != null){
            this.userInfo = userInfo;
        }
        else {
            userInfo = this.userInfo;
        }
        saveUserInfoToSharedPrefs(userInfo);
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
    }

    public userInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void saveStats(userInfo userInfo) {
        if (userInfo != null){
            this.userInfo = userInfo;
        }
        else {
            userInfo = this.userInfo;
        }
        saveUserInfoToSharedPrefs(userInfo);
//        userInfoSIOImpl impl = new userInfoSIOImpl();
//        impl.updateUserInfo(getUserInfo());
        Toast.makeText(getApplicationContext(), "Stats Saved!", Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void CalculateBAC(userInfo userInfo) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (userInfo != null) {
        this.userInfo = userInfo;
        }
        else {
            userInfo = this.userInfo;
        }
            if (userInfo.getWeight() != 0) {
                fragmentTransaction.replace(R.id.container, BldAlcCntntCalculation.newInstance(userInfo))
                        .commit();
            } else {
                fragmentTransaction.replace(R.id.container, StatsFragment.newInstance(userInfo))
                        .commit();
            }

    }

    public void saveUserInfoToSharedPrefs(userInfo userInfo){
        this.userInfo = userInfo;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user_state = getUserInfo().toPrefsString(getUserInfo());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_STATE, user_state).apply();
        if (adCount % 3 == 2) {
            displayInterstitial();
            editor = prefs.edit();
            adCount++;
            editor.putInt("adCount", adCount).apply();
        }
    }
    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
