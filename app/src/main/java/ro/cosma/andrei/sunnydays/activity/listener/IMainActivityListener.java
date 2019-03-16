package ro.cosma.andrei.sunnydays.activity.listener;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/*Created by Cosma Andrei
* 9/17/2017*/
public interface IMainActivityListener {

    void onNavigateToDetailsDailyForecast(WeatherBean bean);

    void setCurrentFragmentTag(String tag);

    void onNavigateToDetailsForecast(WeatherBean bean);

    void onNavigateToCurrentWeather();

}
