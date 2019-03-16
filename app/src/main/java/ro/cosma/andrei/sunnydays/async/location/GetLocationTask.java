package ro.cosma.andrei.sunnydays.async.location;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.location.listener.IGetLocationListener;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/*Created by Cosma Andrei
* 9/27/2017*/
public class GetLocationTask extends AsyncTask<Void, Void, List<LocationBean>> {
    public static final String TAG = GetLocationTask.class.getSimpleName();

    private WeakReference<IGetLocationListener> listener;
    private Context context;

    public GetLocationTask(IGetLocationListener listener, Context context) {
        this.listener = new WeakReference<IGetLocationListener>(listener);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showGetLocationProgressDialog();

        }
        super.onPreExecute();
    }

    @Override
    protected List<LocationBean> doInBackground(Void... voids) {
        return DatabaseHelper.getInstance().getDatabase(context).locationDao().getAll();
    }

    @Override
    protected void onPostExecute(List<LocationBean> bean) {
        if (listener.get() != null) {
            if (bean != null) {
                listener.get().onSuccessGetLocation(bean);
                listener.get().dismissGetLocationProgressDialog();
            } else {
                /*Error*/
                listener.get().dismissGetLocationProgressDialog();
                listener.get().onErrorGetLocation();

            }
        }
        super.onPostExecute(bean);
    }
}
