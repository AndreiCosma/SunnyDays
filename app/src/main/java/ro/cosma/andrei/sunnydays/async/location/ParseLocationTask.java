package ro.cosma.andrei.sunnydays.async.location;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.async.location.listener.IParseLocationListener;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.parser.JSonWeatherParser;

/*Created by Cosma Andrei
* 10/2/2017*/
public class ParseLocationTask extends AsyncTask<JSONObject, Void, List<LocationBean>> {
    public static final String TAG = ParseLocationTask.class.getSimpleName();
    private WeakReference<IParseLocationListener> listener;

    public ParseLocationTask(IParseLocationListener listener) {
        this.listener = new WeakReference<IParseLocationListener>(listener);
    }

    @Override
    protected List<LocationBean> doInBackground(JSONObject... jsonObjects) {
        List<LocationBean> beans;
        try {
            beans = JSonWeatherParser.getInstance().getLocations(jsonObjects[0]);
        } catch (JSONException e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        return beans;
    }

    @Override
    protected void onPreExecute() {
        if (listener.get() != null) {
            listener.get().showParseLocationProgressDialog();
        }
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<LocationBean> beanList) {
        if (listener.get() != null) {
            if (beanList != null) {
                listener.get().onSuccessParseLocation(beanList);
                listener.get().dismissParseLocationProgressDialog();
            } else {
                listener.get().dismissParseLocationProgressDialog();
                listener.get().onErrorParseLocation();
            }
        }
        super.onPostExecute(beanList);
    }
}
