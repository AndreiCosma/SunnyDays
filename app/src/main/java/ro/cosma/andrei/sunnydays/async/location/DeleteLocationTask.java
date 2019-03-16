package ro.cosma.andrei.sunnydays.async.location;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;

import ro.cosma.andrei.sunnydays.async.location.listener.IDeleteLocationListener;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by Cosma Andrei on 17.10.2017.
 */

public class DeleteLocationTask extends AsyncTask<Integer, Void, Integer> {
    public static final String TAG = DeleteLocationTask.class.getSimpleName();
    private WeakReference<IDeleteLocationListener> listener;
    private WeakReference<Context> context;


    public DeleteLocationTask(IDeleteLocationListener listener, Context context) {
        this.listener = new WeakReference<IDeleteLocationListener>(listener);
        this.context = new WeakReference<Context>(context);
    }

    /*Primul parametru este id-ul intrarii in baza de date iar al doilea parametru este pozitia sa in adaptor*/
    @Override
    protected Integer doInBackground(Integer... params) {
        try {
            DatabaseHelper.getInstance().getDatabase(context.get()).locationDao().deleteById(params[0]);
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
        return params[1];
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showDeleteLocationProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer integer) {

        if (listener.get() != null) {
            if (integer != null) {
                listener.get().onSuccessDeleteLocation(integer);
                listener.get().dismissDeleteLocationProgressDialog();
            } else {
                listener.get().dismissDeleteLocationProgressDialog();
                listener.get().onErrorDeleteLocation();
            }

        }

        super.onPostExecute(integer);
    }
}
