package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.weather.listener.IGetWeatherForecastListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by andre on 18.10.2017.
 */

public class GetWeatherForecastTask extends AsyncTask<String, Void, List<WeatherBean>> {
    public static final String TAG = GetWeatherForecastTask.class.getSimpleName();
    private int type;
    private WeakReference<IGetWeatherForecastListener> listener;
    private WeakReference<Context> context;

    public GetWeatherForecastTask(IGetWeatherForecastListener listener, Context context, int type) {
        this.listener = new WeakReference<IGetWeatherForecastListener>(listener);
        this.context = new WeakReference<Context>(context);
        this.type = type;
    }

    @Override
    protected List<WeatherBean> doInBackground(String... params) {
        try {
            Log.i(TAG, "doInBackground: Fetching weather data from database.");
            return DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().getByCity(params[0], type);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showGetWeatherForecastProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<WeatherBean> been) {
        if (listener.get() != null) {
            listener.get().dismissGetWeatherForecastProgressDialog();
            if (been != null) {
                Log.i(TAG, "onPostExecute: Data fetched successfully and has length of:" + been.size());
                listener.get().onSuccessGetWeatherForecastEntry(been);
            } else {
                Log.i(TAG, "onPostExecute: Data fetched is null.");
                listener.get().onErrorGetWeatherForecast();
            }
        }
        super.onPostExecute(been);
    }
}
