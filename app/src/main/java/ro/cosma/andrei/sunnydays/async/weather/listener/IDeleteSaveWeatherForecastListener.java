package ro.cosma.andrei.sunnydays.async.weather.listener;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/**
 * Created by andre on 18.10.2017.
 */

public interface IDeleteSaveWeatherForecastListener {
    void showDeleteSaveWeatherForecastProgressDialog();

    void onSuccessDeleteSaveWeatherForecastEntry(List<WeatherBean> been);

    void dismissDeleteSaveWeatherForecastProgressDialog();

    void onErrorDeleteSaveWeatherForecast();
}
