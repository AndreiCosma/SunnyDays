package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.async.weather.listener.IDeleteAllWeatherForecastEntriesListener;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by andre on 18.10.2017.
 */

public class DeleteAllWeatherForecastEntriesTask extends AsyncTask<Void, Void, Boolean> {
    public static final String TAG = DeleteAllWeatherForecastEntriesTask.class.getSimpleName();
    private WeakReference<IDeleteAllWeatherForecastEntriesListener> listener;
    private WeakReference<Context> context;

    public DeleteAllWeatherForecastEntriesTask(IDeleteAllWeatherForecastEntriesListener listener, Context context) {
        this.listener = new WeakReference<IDeleteAllWeatherForecastEntriesListener>(listener);
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().deleteAllEntries();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showDeleteAllWeatherForecastEntriesProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean bool) {
        if (listener.get() != null) {
            listener.get().dismissDeleteAllWeatherForecastEntriesProgressDialog();
            if (bool != null) {
                listener.get().onSuccessDeleteAllWeatherForecastEntries();
            } else {
                listener.get().onErrorDeleteAllWeatherForecastEntries();
            }
        }
        super.onPostExecute(bool);
    }
}
