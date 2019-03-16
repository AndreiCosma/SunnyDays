package ro.cosma.andrei.sunnydays.async.location.listener;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.LocationBean;

/*Created by Cosma Andrei
* 10/2/2017*/
public interface IParseLocationListener {
    void showParseLocationProgressDialog();

    void onSuccessParseLocation(List<LocationBean> bean);

    void dismissParseLocationProgressDialog();

    void onErrorParseLocation();
}
