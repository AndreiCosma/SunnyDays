package ro.cosma.andrei.sunnydays.async.location.listener;

import org.json.JSONException;

import ro.cosma.andrei.sunnydays.bean.LocationBean;

/*Created by Cosma Andrei
* 9/27/2017*/
public interface ISaveLocationListener {
    void showSaveLocationProgressDialog();

    void onSuccessSaveLocation(LocationBean bean) throws JSONException;

    void dismissSaveLocationProgressDialog();

    void onErrorSaveLocation();
}
