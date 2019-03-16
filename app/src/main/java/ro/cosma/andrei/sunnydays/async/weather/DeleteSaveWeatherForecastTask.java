package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.weather.listener.IDeleteSaveWeatherForecastListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by andre on 18.10.2017.
 */

public class DeleteSaveWeatherForecastTask extends AsyncTask<List<WeatherBean>, Void, List<WeatherBean>> {
    public static final String TAG = DeleteAllWeatherForecastEntriesTask.class.getSimpleName();
    private int type;
    private WeakReference<IDeleteSaveWeatherForecastListener> listener;
    private WeakReference<Context> context;

    private DeleteSaveWeatherForecastTask() {
    }

    public DeleteSaveWeatherForecastTask(IDeleteSaveWeatherForecastListener listener, Context context, int type) {
        this.listener = new WeakReference<IDeleteSaveWeatherForecastListener>(listener);
        this.context = new WeakReference<Context>(context);
        this.type = type;
    }

    @Override
    protected List<WeatherBean> doInBackground(List<WeatherBean>... params) {
        try {
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().deleteByCity(params[0].get(0).getCity(), type);
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().insertAll(params[0]);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return params[0];
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showDeleteSaveWeatherForecastProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<WeatherBean> been) {
        if (listener.get() != null) {
            listener.get().dismissDeleteSaveWeatherForecastProgressDialog();

            if (been != null) {
                listener.get().onSuccessDeleteSaveWeatherForecastEntry(been);
            } else {
                listener.get().onErrorDeleteSaveWeatherForecast();
            }

        }
        super.onPostExecute(been);
    }
}
