package ro.cosma.andrei.sunnydays.activity.listener;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/*Created by Cosma Andrei
* 9/17/2017*/
public interface ISplashActivityListener {
    void onNavigateToMainActivity(WeatherBean currentWeatherBean);

    void onNavigateToSplashFragment(String location);

    void onNavigateToLocationConfigFragment();

}
