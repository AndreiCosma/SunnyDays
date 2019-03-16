package ro.cosma.andrei.sunnydays.async.weather.listener;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/*Created by Cosma Andrei
* 9/27/2017*/
public interface ISaveWeatherForecastListener {
    void showSaveWeatherForecastProgressDialog();

    void onSuccessSaveWeatherForecast(WeatherBean bean);

    void dismissSaveWeatherForecastProgressDialog();

    void onErrorSaveWeatherForecast();
}
