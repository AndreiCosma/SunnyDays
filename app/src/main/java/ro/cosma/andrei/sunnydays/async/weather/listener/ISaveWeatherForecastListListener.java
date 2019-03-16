package ro.cosma.andrei.sunnydays.async.weather.listener;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/**
 * Created by andre on 18.10.2017.
 */

public interface ISaveWeatherForecastListListener {
    void showSaveWeatherForecastListProgressDialog();

    void onSuccessSaveWeatherForecastList(List<WeatherBean> been);

    void dismissSaveWeatherForecastListProgressDialog();

    void onErrorSaveWeatherForecastList();
}
