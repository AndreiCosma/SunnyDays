package ro.cosma.andrei.sunnydays.async.location;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.location.listener.IGetLocationIdListListener;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/**
 * Created by Cosma Andrei on 17.10.2017.
 */

public class GetLocationIdListTask extends AsyncTask<Void, Void, List<Integer>> {

    public static final String TAG = GetLocationIdListTask.class.getSimpleName();
    private WeakReference<IGetLocationIdListListener> listener;
    private WeakReference<Context> context;

    public GetLocationIdListTask(IGetLocationIdListListener listener, Context context) {
        this.context = new WeakReference<Context>(context);
        this.listener = new WeakReference<>(listener);
    }

    @Override
    protected List<Integer> doInBackground(Void... params) {
        try {
            return DatabaseHelper.getInstance().getDatabase(context.get()).locationDao().getIdList();
        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showGetLocationIdListProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Integer> integers) {
        if (listener.get() != null) {
            if (integers != null) {
                listener.get().onSuccessGetLocationIdList(integers);
                listener.get().dismissGetLocationIdListProgressDialog();
            } else {
                listener.get().dismissGetLocationIdListProgressDialog();
                listener.get().onErrorGetLocationIdList();
            }
        }
        super.onPostExecute(integers);
    }
}
