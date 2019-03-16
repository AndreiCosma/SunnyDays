package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.async.weather.listener.IDeleteAllWeatherForecastEntriesListener;
import ro.cosma.andrei.sunnydays.async.weather.listener.IDeleteWeatherForecastEntryListener;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by andre on 18.10.2017.
 */

public class DeleteWeatherForecastEntryWithTypeTask extends AsyncTask<String, Void, Boolean> {
    public static final String TAG = DeleteAllWeatherForecastEntriesTask.class.getSimpleName();
    private int type;
    private WeakReference<IDeleteWeatherForecastEntryListener> listener;
    private WeakReference<Context> context;

    private DeleteWeatherForecastEntryWithTypeTask(){}

    public DeleteWeatherForecastEntryWithTypeTask(IDeleteWeatherForecastEntryListener listener, Context context, int type) {
        this.listener = new WeakReference<IDeleteWeatherForecastEntryListener>(listener);
        this.context = new WeakReference<Context>(context);
        this.type = type;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().deleteByCity(params[0],type);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showDeleteWeatherForecastEntryProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (listener.get() != null) {
            listener.get().dismissDeleteWeatherForecastEntryProgressDialog();
            if (bool != null) {
                listener.get().onSuccessDeleteWeatherForecastEntry();
            } else {
                listener.get().onErrorDeleteWeatherForecastEntry();
            }
        }
        super.onPostExecute(bool);
    }
}
