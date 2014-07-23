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

import brillianceanimationstudio.brandonward.baccalculator.domain.*;

import com.google.android.gms.ads.*;



public class MainBAC extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, WelcomeScreenFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener, BldAlcCntntCalculation.OnFragmentInteractionListener{

    private InterstitialAd interstitial;
    /* Your ad unit id. Replace with your actual ad unit id. */
    private static final String AD_UNIT_ID = "ca-app-pub-6216655107273980/1892012754";
    private String USER_STATE;
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
        USER_STATE = userInfo.getPrefsKey();
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String user_state = prefs.getString(USER_STATE,"");
        userInfo = userInfo.readPrefsString(user_state);
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
        fragmentTransaction.replace(R.id.container, WelcomeScreenFragment.newInstance())
                .commit();


        adCount = prefs.getInt("adCount", 0);
        adCount++;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("adCount",adCount).commit();
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
                newFragment = new WelcomeScreenFragment();
                break;
            case 1:
                newFragment = new StatsFragment().newInstance(userInfo);// TODO: Make all of these that need info use newInstance();
                break;
            case 2:
                newFragment = new BldAlcCntntCalculation();
                break;

        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();
        if (adCount % 3 == 2){
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
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
        if (interstitial.isLoaded()){
            interstitial.show();
        }
    }
}
