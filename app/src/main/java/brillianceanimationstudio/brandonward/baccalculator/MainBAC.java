package brillianceanimationstudio.brandonward.baccalculator;
import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import brillianceanimationstudio.brandonward.baccalculator.domain.*;
import brillianceanimationstudio.brandonward.baccalculator.engine.calculateBAC;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.android.gms.ads.*;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class MainBAC extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, WelcomeScreenFragment.OnFragmentInteractionListener, StatsFragment.OnFragmentInteractionListener, BldAlcCntntCalculation.OnFragmentInteractionListener, SettingsFragment.OnFragmentInteractionListener {

    private InterstitialAd interstitial;
    /* Your ad unit id. Replace with your actual ad unit id. */
    private static final String AD_UNIT_ID = "ca-app-pub-1321769086734378/4674749647";
    private static final int NOTIFICATION_ID = 711711;
    private static final String NOTIFICATION_STATE = "showNotifications";
    private static final String NOTIFICATION_ONGOING = "ongoingNotifications";
    private boolean showNotifications;
    private boolean ongoingNotifications;
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
        showNotifications = prefs.getBoolean(NOTIFICATION_STATE, true);
        ongoingNotifications = prefs.getBoolean(NOTIFICATION_ONGOING, true);
        String user_state = prefs.getString(USER_STATE, "");
        userInfo = getUserInfo().readPrefsString(user_state);
        Calendar runtime = Calendar.getInstance();
        //Determine if user has been here before, and if not, set stats to right now.
        if (userInfo.gettMinute()+ userInfo.gettHour()+ userInfo.getLastDay()+userInfo.getLastMonth()+userInfo.getLastYear()==0){
            userInfo.settMinute(runtime.get(Calendar.MINUTE));
            userInfo.settHour(runtime.get(Calendar.HOUR_OF_DAY));
            userInfo.setLastDay(runtime.get(Calendar.DAY_OF_MONTH));
            userInfo.setLastMonth(runtime.get(Calendar.MONTH));
            userInfo.setLastYear(runtime.get(Calendar.YEAR));
        }
        //Determine user's previous BAC (if any) and reset the application runtime stats and drink count if 0.
        double testBAC = new calculateBAC(userInfo).calculateBloodAlcoholContent();
        if (testBAC < 0){
            userInfo.settMinute(runtime.get(Calendar.MINUTE));
            userInfo.settHour(runtime.get(Calendar.HOUR_OF_DAY));
            userInfo.setLastDay(runtime.get(Calendar.DAY_OF_MONTH));
            userInfo.setLastMonth(runtime.get(Calendar.MONTH));
            userInfo.setLastYear(runtime.get(Calendar.YEAR));
            userInfo.setDrinks(0.0);
            removeNotification();
        }
        else if (showNotifications){
            addNotification();
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
    public void onStart() {
        super.onStart();
        // TODO: setDryRun(true) for development, setDryRun(false) for production.
        EasyTracker.getInstance(this).activityStart(this);  // Add this method.
        GoogleAnalytics.getInstance(this).setDryRun(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);  // Add this method.
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
                newFragment = new StatsFragment().newInstance(userInfo);
                break;
            case 2:
                newFragment = new BldAlcCntntCalculation().newInstance(userInfo);
                break;
            case 3:
                newFragment = new SettingsFragment().newInstance(showNotifications,ongoingNotifications);
                break;
        }
        onSectionAttached(position+1);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        if (showNotifications){
            addNotification();
        }
        if (adCount % 3 == 2) {
            displayInterstitial();
            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            adCount++;
            editor.putInt("adCount", adCount).apply();
        }
    }

    @Override
    public void actionBarCalculate() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (userInfo.getWeight() != 0) {
            fragmentTransaction.replace(R.id.container, BldAlcCntntCalculation.newInstance(userInfo))
                    .commit();
            onSectionAttached(3);
        } else {
            fragmentTransaction.replace(R.id.container, StatsFragment.newInstance(userInfo))
                    .commit();
            onSectionAttached(2);
        }
        if (showNotifications){
            addNotification();
        }
    }

    public void onSectionAttached(int number) {

        switch (number) {
            case 1:
                mTitle = getString(R.string.app_name);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_sectionSettings);
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
        onSectionAttached(3);
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        if (showNotifications){
            addNotification();
        }
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
        onSectionAttached(3);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_NONE)
                .commit();
        if (showNotifications){
            addNotification();
        }
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
        onSectionAttached(3);
        VISIBLE_FRAGMENT_TAG = newFragment.toString();
        fragmentManager.beginTransaction()
                .replace(R.id.container, newFragment, VISIBLE_FRAGMENT_TAG)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
        if (showNotifications){
            addNotification();
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
                onSectionAttached(3);
                fragmentTransaction.replace(R.id.container, BldAlcCntntCalculation.newInstance(userInfo))
                        .commit();
            } else {
                onSectionAttached(2);
                fragmentTransaction.replace(R.id.container, StatsFragment.newInstance(userInfo))
                        .commit();
            }
        if (showNotifications){
            addNotification();
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
    private void addNotification() {
        double BAC = new calculateBAC(userInfo).calculateBloodAlcoholContent();
        if (BAC > 0 && showNotifications) { // DO NOT SHOW NOTIFICATION IF BAC IS 0 or less && only if they want notifications
            int hour = userInfo.gettHour();
            int minute = userInfo.gettMinute();
            String AM_PM;
            if (hour < 12) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }
            hour = hour % 12;
            final DecimalFormat BACFormat = new DecimalFormat("0.####");
            final DecimalFormat minuteFormat = new DecimalFormat("00.#");
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setLargeIcon(bm)
                            .setContentTitle("Blood Alcohol: " + BACFormat.format(BAC))
                            .setContentText("First drink: " + hour + ":" + minuteFormat.format(minute) + " " + AM_PM + ". Tap to Update!")
                            .setOngoing(ongoingNotifications)//Base clearing on user settings
                            .setOnlyAlertOnce(true)
                            ;//DO NOT HOG STATUS BAR

            Intent notificationIntent = new Intent(this, MainBAC.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());

            // Schedule for 5 minutes out
            TimedNotifications newNotify = new TimedNotifications();
            Timer notificationTimer = new Timer();

            // After 5 minutes
            notificationTimer.schedule(newNotify, 5*60*1000);
        }
        else if (showNotifications && userInfo.getDrinks() != 0){
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_stat_name)
                            .setLargeIcon(bm)
                            .setContentTitle("Blood Alcohol: 0.000")
                            .setContentText("Drink plenty of water!")
                            .setOnlyAlertOnce(true);//DO NOT HOG STATUS BAR

            Intent notificationIntent = new Intent(this, MainBAC.class);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);

            // Add as notification
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        }
        else{
            removeNotification();
        }
    }

    // Remove notification
    private void removeNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(NOTIFICATION_ID);
    }

    @Override
    public void toShowNotifications(boolean showNotifications) {
        this.showNotifications = showNotifications;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(NOTIFICATION_STATE,showNotifications).apply();
        addNotification();
    }

    @Override
    public void toShowOngoing(boolean showOngoing) {
        this.ongoingNotifications = showOngoing;
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(NOTIFICATION_ONGOING,ongoingNotifications).apply();
        addNotification();
    }

    class TimedNotifications extends TimerTask{
        public void run() {
            generateNotification();
        }
        public void generateNotification(){
            addNotification();
        }
    }
}
