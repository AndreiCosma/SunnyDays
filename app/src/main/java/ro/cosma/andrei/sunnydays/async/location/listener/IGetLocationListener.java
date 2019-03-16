package ro.cosma.andrei.sunnydays.async.location.listener;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.LocationBean;

/*Created by Cosma Andrei
* 9/27/2017*/
public interface IGetLocationListener {
    void showGetLocationProgressDialog();

    void onSuccessGetLocation(List<LocationBean> bean);

    void dismissGetLocationProgressDialog();

    void onErrorGetLocation();
}
