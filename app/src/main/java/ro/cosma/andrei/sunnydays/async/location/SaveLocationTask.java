package ro.cosma.andrei.sunnydays.async.location;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.async.location.listener.ISaveLocationListener;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/*Created by Cosma Andrei
* 9/27/2017*/
public class SaveLocationTask extends AsyncTask<LocationBean, Void, LocationBean> {

    public static final String TAG = SaveLocationTask.class.getSimpleName();
    private WeakReference<ISaveLocationListener> listener;
    private Context context;

    public SaveLocationTask(ISaveLocationListener listener, Context context) {
        this.listener = new WeakReference<ISaveLocationListener>(listener);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showSaveLocationProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected LocationBean doInBackground(LocationBean... locationBeen) {

        try {
            DatabaseHelper.getInstance().getDatabase(context).locationDao().insertAll(locationBeen);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        //return DatabaseHelper.getInstance().getDatabase(context).locationDao().get(locationBeen[0].getId()).get(0);
        return locationBeen[0];
    }

    @Override
    protected void onPostExecute(LocationBean bean) {
        if (listener.get() != null) {
            if (bean != null) {
                try {
                    listener.get().onSuccessSaveLocation(bean);
                } catch (JSONException e) {
                    Log.e(TAG, "onPostExecute: ", e);
                }
                listener.get().dismissSaveLocationProgressDialog();
            } else {
                listener.get().dismissSaveLocationProgressDialog();
                listener.get().onErrorSaveLocation();
            }
        }

        super.onPostExecute(bean);
    }
}
