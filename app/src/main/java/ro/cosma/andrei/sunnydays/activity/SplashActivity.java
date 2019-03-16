package ro.cosma.andrei.sunnydays.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.ISplashActivityListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.introduction.FirstRunConfigFragment;
import ro.cosma.andrei.sunnydays.pages.splash.SplashFragment;
import ro.cosma.andrei.sunnydays.utils.AppConstants;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.CURRENT_WEATHER_FORECAST;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_LOCATION;


/*Created by Cosma Andrei
* 9/17/2017*/
public class SplashActivity extends AppCompatActivity implements ISplashActivityListener {
    public static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_layout);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();

        String location = PreferenceManager.getDefaultSharedPreferences(this).getString(PREFERENCES_LOCATION, null);

        if (location != null) {
            transaction.replace(R.id.container, new SplashFragment(), SplashFragment.TAG);
        } else {
            transaction.replace(R.id.container, new FirstRunConfigFragment(), FirstRunConfigFragment.TAG);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onNavigateToMainActivity(WeatherBean currentWeatherBean) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        if (currentWeatherBean != null) {
            bundle.putSerializable(CURRENT_WEATHER_FORECAST, currentWeatherBean);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onNavigateToSplashFragment(String location) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, SplashFragment.newInstance(location), SplashFragment.TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    @Override
    public void onNavigateToLocationConfigFragment() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().remove(AppConstants.PREFERENCES_LOCATION).apply();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, FirstRunConfigFragment.newInstance(true), FirstRunConfigFragment.TAG);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }
}
