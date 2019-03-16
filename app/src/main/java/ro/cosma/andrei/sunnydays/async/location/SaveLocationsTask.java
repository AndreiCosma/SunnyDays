package ro.cosma.andrei.sunnydays.async.location;

import android.content.Context;
import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.location.listener.ISaveLocationsListener;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.database.DatabaseHelper;

/*Created by Cosma Andrei
* 9/27/2017*/
public class SaveLocationsTask extends AsyncTask<List<LocationBean>, Void, List<LocationBean>> {

    private WeakReference<ISaveLocationsListener> listener;
    private Context context;

    public SaveLocationsTask(ISaveLocationsListener listener, Context context) {
        this.listener = new WeakReference<ISaveLocationsListener>(listener);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showSaveLocationsProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected List<LocationBean> doInBackground(List<LocationBean>... locationBeen) {

        LocationBean[] locations = new LocationBean[locationBeen[0].size()];
        for (int i = 0; i < locationBeen[0].size(); i++) {
            locations[i] = locationBeen[0].get(i);
        }

        DatabaseHelper.getInstance().getDatabase(context).locationDao().insertAll(locations);

        return locationBeen[0];
    }

    @Override
    protected void onPostExecute(List<LocationBean> bean) {
        if (listener.get() != null) {
            if (bean != null) {
                listener.get().onSuccessSaveLocations(bean);
                listener.get().dismissSaveLocationsProgressDialog();
            } else {
                listener.get().dismissSaveLocationsProgressDialog();
                listener.get().onErrorSaveLocations();
            }
        }

        super.onPostExecute(bean);
    }
}
