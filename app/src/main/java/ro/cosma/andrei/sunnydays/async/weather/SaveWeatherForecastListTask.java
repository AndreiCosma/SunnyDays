package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.weather.listener.ISaveWeatherForecastListListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by andre on 18.10.2017.
 */

public class SaveWeatherForecastListTask extends AsyncTask<List<WeatherBean>, Void, List<WeatherBean>> {
    private int type;
    private String cityName;
    public static final String TAG = SaveWeatherForecastTask.class.getSimpleName();
    private WeakReference<ISaveWeatherForecastListListener> listener;
    private WeakReference<Context> context;

    public SaveWeatherForecastListTask(ISaveWeatherForecastListListener listener, Context context, int type, String cityName) {
        this.listener = new WeakReference<ISaveWeatherForecastListListener>(listener);
        this.context = new WeakReference<Context>(context);
        this.type = type;
        this.cityName = cityName;
    }

    @Override
    protected List<WeatherBean> doInBackground(List<WeatherBean>... params) {
        try {
            Log.i(TAG, "doInBackground: Forecast is beaing saved to the database.");
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().deleteByCity(cityName, type);
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().insertAll(params[0]);
            Log.i(TAG, "onPreExecute: Weather data saved successfully.");
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return params[0];
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showSaveWeatherForecastListProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<WeatherBean> weatherBeen) {
        if (listener.get() != null) {
            if (weatherBeen != null) {
                listener.get().onSuccessSaveWeatherForecastList(weatherBeen);
                listener.get().dismissSaveWeatherForecastListProgressDialog();
            } else {
                listener.get().onErrorSaveWeatherForecastList();
                listener.get().dismissSaveWeatherForecastListProgressDialog();
            }
        }
        super.onPostExecute(weatherBeen);
    }
}
