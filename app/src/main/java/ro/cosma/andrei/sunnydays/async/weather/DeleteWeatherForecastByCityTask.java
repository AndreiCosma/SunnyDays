package ro.cosma.andrei.sunnydays.async.weather;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;
import ro.cosma.andrei.sunnydays.pages.details.daily.DailyForecastDetailsFragment;

/**
 * Created by andre on 18.10.2017.
 */

public class DeleteWeatherForecastByCityTask extends AsyncTask<String, Void, Void> {
    //TODO : de aranjat clasa
    private WeakReference<Context> context;

    public DeleteWeatherForecastByCityTask(Context context) {
        this.context = new WeakReference<Context>(context);
    }

    @Override
    protected Void doInBackground(String... params) {
        DatabaseHelper.getInstance().getDatabase(context.get()).weatherDao().deleteByCity(params[0]);
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}
