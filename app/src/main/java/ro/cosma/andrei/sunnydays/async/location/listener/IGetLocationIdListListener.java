package ro.cosma.andrei.sunnydays.async.location.listener;

import java.util.List;

/**
 * Created by Cosma Andrei on 17.10.2017.
 */

public interface IGetLocationIdListListener {
    void showGetLocationIdListProgressDialog();

    void onSuccessGetLocationIdList(List<Integer> idList);

    void dismissGetLocationIdListProgressDialog();

    void onErrorGetLocationIdList();
}
