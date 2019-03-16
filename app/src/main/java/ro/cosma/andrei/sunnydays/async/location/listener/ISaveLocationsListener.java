package ro.cosma.andrei.sunnydays.async.location.listener;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.LocationBean;

/*Created by Cosma Andrei
* 9/27/2017*/
public interface ISaveLocationsListener {
    void showSaveLocationsProgressDialog();

    void onSuccessSaveLocations(List<LocationBean> beanList);

    void dismissSaveLocationsProgressDialog();

    void onErrorSaveLocations();
}
