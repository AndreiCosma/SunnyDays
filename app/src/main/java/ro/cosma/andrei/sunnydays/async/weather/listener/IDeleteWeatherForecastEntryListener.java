package ro.cosma.andrei.sunnydays.async.weather.listener;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/**
 * Created by andre on 18.10.2017.
 */

public interface IDeleteWeatherForecastEntryListener {

    void showDeleteWeatherForecastEntryProgressDialog();

    void onSuccessDeleteWeatherForecastEntry();

    void dismissDeleteWeatherForecastEntryProgressDialog();

    void onErrorDeleteWeatherForecastEntry();

}
