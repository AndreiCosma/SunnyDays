package ro.cosma.andrei.sunnydays.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.current.CurrentWeatherFragment;
import ro.cosma.andrei.sunnydays.pages.details.daily.DailyForecastDetailsFragment;
import ro.cosma.andrei.sunnydays.pages.daily.DailyForecastFragment;
import ro.cosma.andrei.sunnydays.pages.details.hours.ForecastDetailsFragment;
import ro.cosma.andrei.sunnydays.pages.hourly.HourlyForecastFragment;
import ro.cosma.andrei.sunnydays.pages.location.LocationFragment;
import ro.cosma.andrei.sunnydays.pages.settings.SettingsFragment;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

/*Created by Cosma Andrei
* 9/17/2017*/
public class MainActivity extends AppCompatActivity implements IMainActivityListener, DrawerLayout.DrawerListener, NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = MainActivity.class.getSimpleName();

    private String currentFragmentTag = "";

    private WeatherBean mCurrentWeatherBean;

    private DrawerLayout drawerLayout;

    private Toolbar toolbar;

    private int appBarElevation;

    private Toast exitMSGToast;

    private NavigationView navigationView;

    private ActionBarDrawerToggle mActionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        appBarElevation = AppUtils.dpToPixel(4, this);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        mActionBarDrawerToggle.syncState();

        if (savedInstanceState != null) {
            mCurrentWeatherBean = (WeatherBean) savedInstanceState.getSerializable(AppConstants.CURRENT_WEATHER_FORECAST);
        }
        if (getIntent().getExtras() != null) {
            mCurrentWeatherBean = (WeatherBean) getIntent().getExtras().getSerializable(AppConstants.CURRENT_WEATHER_FORECAST);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, CurrentWeatherFragment.newInstance(mCurrentWeatherBean), CurrentWeatherFragment.TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (DailyForecastDetailsFragment.TAG.equals(currentFragmentTag) || ForecastDetailsFragment.TAG.equals(currentFragmentTag)) {
            setToolbarBackDisableAndUnlockMenu();
        }
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Log.d(TAG, "onBackPressed: ");
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                if (exitMSGToast == null) {
                    exitMSGToast = Toast.makeText(this, getString(R.string.exit), Toast.LENGTH_LONG);
                    exitMSGToast.show();
                } else {
                    if (exitMSGToast.getView().isShown()) {
                        super.onBackPressed();
                    } else {
                        exitMSGToast.show();
                    }
                }
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mCurrentWeatherBean != null) {
            outState.putSerializable(AppConstants.CURRENT_WEATHER_SAVE, mCurrentWeatherBean);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onNavigateToDetailsDailyForecast(WeatherBean bean) {
        setToolbarBackEnableAndLockMenu();
        replaceFragment(R.id.container, DailyForecastDetailsFragment.newInstance(bean), DailyForecastDetailsFragment.TAG, true);
    }

    @Override
    public void setCurrentFragmentTag(String tag) {
        this.currentFragmentTag = tag;
    }

    @Override
    public void onNavigateToDetailsForecast(WeatherBean bean) {
        setToolbarBackEnableAndLockMenu();
        replaceFragment(R.id.container, ForecastDetailsFragment.newInstance(bean), ForecastDetailsFragment.TAG, true);
    }

    @Override
    public void onNavigateToCurrentWeather() {
        navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(true);
        navigationView.getMenu().getItem(1).setChecked(false);
        replaceFragment(R.id.container, new CurrentWeatherFragment(), "", false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch (id) {
            case R.id.action_current_weather:
                toolbar.setElevation(appBarElevation);
                replaceFragment(R.id.container, new CurrentWeatherFragment(), "", false);
                break;

            case R.id.action_forecast:
                navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
                replaceFragment(R.id.container, new HourlyForecastFragment(), "", false);
                break;

            case R.id.action_daily_forecast:
                navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
                replaceFragment(R.id.container, new DailyForecastFragment(), "", false);
                break;
            case R.id.action_location:
                navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
                navigationView.getMenu().getItem(1).setChecked(true);
                toolbar.setElevation(appBarElevation);
                replaceFragment(R.id.container, new LocationFragment(), "", false);
                break;
            case R.id.action_settings:
                navigationView.getMenu().getItem(0).getSubMenu().getItem(0).setChecked(false);
                toolbar.setElevation(appBarElevation);
                replaceFragment(R.id.container, new SettingsFragment(), "", false);
                break;
        }
        return true;
    }

    public void replaceFragment(int containerViewId, Fragment fragment, String tag, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.replace(containerViewId, fragment, tag);
            fragmentTransaction.addToBackStack(tag);
        } else {
            fragmentTransaction.replace(containerViewId, fragment);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }


    public void setToolbarBackEnableAndLockMenu() {
        Log.d(TAG, "setToolbarBackEnableAndLockMenu: lock menu and display back arrow.");
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mActionBarDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToolbarBackDisableAndUnlockMenu();
                getSupportFragmentManager().popBackStack();
            }
        });
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void setToolbarBackDisableAndUnlockMenu() {
        Log.d(TAG, "setToolbarBackDisableAndUnlockMenu: Unlock menu and display menu indicator.");
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActionBarDrawerToggle.setToolbarNavigationClickListener(mActionBarDrawerToggle.getToolbarNavigationClickListener());
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
