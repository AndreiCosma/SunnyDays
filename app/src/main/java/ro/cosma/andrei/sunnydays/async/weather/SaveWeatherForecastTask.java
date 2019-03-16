package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.async.weather.listener.ISaveWeatherForecastListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/*Created by Cosma Andrei
* 9/27/2017*/
public class SaveWeatherForecastTask extends AsyncTask<WeatherBean, Void, WeatherBean> {
    public static final String TAG = SaveWeatherForecastTask.class.getSimpleName();
    private WeakReference<ISaveWeatherForecastListener> listener;
    private WeakReference<Context> context;

    public SaveWeatherForecastTask(ISaveWeatherForecastListener listener, Context context) {

        this.listener = new WeakReference<ISaveWeatherForecastListener>(listener);
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected WeatherBean doInBackground(WeatherBean... params) {
        try {
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().insert(params[0]);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return params[0];
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showSaveWeatherForecastProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(WeatherBean weatherBean) {
        if (listener.get() != null) {
            if (weatherBean != null) {
                listener.get().onSuccessSaveWeatherForecast(weatherBean);
                listener.get().dismissSaveWeatherForecastProgressDialog();
            } else {
                listener.get().dismissSaveWeatherForecastProgressDialog();
                listener.get().onErrorSaveWeatherForecast();
            }
        }
        super.onPostExecute(weatherBean);
    }
}
