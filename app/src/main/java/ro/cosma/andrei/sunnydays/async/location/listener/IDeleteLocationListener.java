package ro.cosma.andrei.sunnydays.async.location.listener;

/**
 * Created by andre on 17.10.2017.
 */

public interface IDeleteLocationListener {
    void showDeleteLocationProgressDialog();

    void onSuccessDeleteLocation(int positionInAdapter);

    void dismissDeleteLocationProgressDialog();

    void onErrorDeleteLocation();
}
