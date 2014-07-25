package brillianceanimationstudio.brandonward.baccalculator;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import brillianceanimationstudio.brandonward.baccalculator.domain.*;
import brillianceanimationstudio.brandonward.baccalculator.service.userInfoSIOImpl;

import com.google.android.gms.ads.*;


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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void plusOneDrinkPressed(userInfo userInfo) {
        if (userInfo != null){
            this.userInfo = userInfo;
        }
        else {
            userInfo = this.userInfo;
        }
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
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

    public userInfo getUserInfo() {
        return userInfo;
    }

    @Override
    public void saveStats(userInfo userInfo) {
        this.userInfo = userInfo;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user_state = getUserInfo().toPrefsString(getUserInfo());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_STATE, user_state).apply();
        userInfoSIOImpl impl = new userInfoSIOImpl();
        impl.updateUserInfo(getUserInfo());
        Toast.makeText(getApplicationContext(), "Stats Saved!", Toast.LENGTH_LONG).show();
        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        if (adCount % 3 == 2) {
            displayInterstitial();
            editor = prefs.edit();
            adCount++;
            editor.putInt("adCount", adCount).apply();
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_bac_start, container, false);


            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainBAC) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
