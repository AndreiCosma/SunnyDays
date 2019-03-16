package ro.cosma.andrei.sunnydays.pages.splash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.ISplashActivityListener;
import ro.cosma.andrei.sunnydays.async.location.SaveLocationTask;
import ro.cosma.andrei.sunnydays.async.location.listener.ISaveLocationListener;
import ro.cosma.andrei.sunnydays.async.weather.DeleteSaveWeatherForecastTask;
import ro.cosma.andrei.sunnydays.async.weather.GetWeatherForecastTask;
import ro.cosma.andrei.sunnydays.async.weather.listener.IDeleteSaveWeatherForecastListener;
import ro.cosma.andrei.sunnydays.async.weather.listener.IGetWeatherForecastListener;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.parser.JSonWeatherParser;
import ro.cosma.andrei.sunnydays.service.WeatherService;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_LOCATION;


/*Created by Cosma Andrei
* 9/17/2017*/
public class SplashFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, ISaveLocationListener {
    public static final String TAG = SplashFragment.class.getSimpleName();

    private SharedPreferences preferences;
    private ISplashActivityListener listener;
    private String location = null;
    private JSONObject response;


    @Override
    public void onAttach(Context context) {
        if (context instanceof ISplashActivityListener) {
            listener = (ISplashActivityListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onStart() {

        if (AppUtils.hasConnectionToInternet(getContext())) {
            try {
                WeatherService.getInstance().getCurrentWeather(getContext(),
                        location,
                        this,
                        this);
            } catch (Exception e) {
                Log.e(TAG, "onStart: ", e);
            }
        } else {
            /*Fetch the data from database by location name*/
            new GetWeatherForecastTask(new IGetWeatherForecastListener() {
                @Override
                public void showGetWeatherForecastProgressDialog() {

                }

                @Override
                public void onSuccessGetWeatherForecastEntry(List<WeatherBean> been) {
                    if (been.size() > 0) {
                        listener.onNavigateToMainActivity(been.get(0));
                    }
                }

                @Override
                public void dismissGetWeatherForecastProgressDialog() {

                }

                @Override
                public void onErrorGetWeatherForecast() {
                    listener.onNavigateToMainActivity(null);
                }
            }, getContext(),
                    WeatherBean.TYPE_CURRENT).execute(location);

        }
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
        AppUtils.mapIcons();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (location == null) {
            location = preferences.getString(PREFERENCES_LOCATION, "CRAIOVA");
        }
        return rootView;
    }


    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }


    @Override
    public void onResponse(final JSONObject response) {
        try {
            if (listener != null) {
                this.response = response;
                new SaveLocationTask(this, getContext()).execute(JSonWeatherParser.getInstance().getLocation(response));
            }
        } catch (JSONException e) {
            //TODO : de rezolvat cazul in care pica parsarea
            Log.e(TAG, "onResponse: ", e);
            listener.onNavigateToMainActivity(null);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        //TODO: de luat din baza de date in caza ca parsarea pica
        listener.onNavigateToLocationConfigFragment();
    }

    @Override
    public void showSaveLocationProgressDialog() {

    }

    @Override
    public void onSuccessSaveLocation(final LocationBean bean) throws JSONException {
        location = bean.getCity();
        preferences.edit().putString(AppConstants.PREFERENCES_LOCATION, location).apply();
        List<WeatherBean> weatherForecastList = new ArrayList<>();
        weatherForecastList.add(JSonWeatherParser.getInstance().getCurrentWeather(this.response));

        new DeleteSaveWeatherForecastTask(new IDeleteSaveWeatherForecastListener() {
            @Override
            public void showDeleteSaveWeatherForecastProgressDialog() {

            }

            @Override
            public void onSuccessDeleteSaveWeatherForecastEntry(List<WeatherBean> been) {
                listener.onNavigateToMainActivity(been.get(0));
            }

            @Override
            public void dismissDeleteSaveWeatherForecastProgressDialog() {

            }

            @Override
            public void onErrorDeleteSaveWeatherForecast() {
                listener.onNavigateToMainActivity(null);
            }
        }, getContext(), WeatherBean.TYPE_CURRENT).execute(weatherForecastList);


    }

    @Override
    public void dismissSaveLocationProgressDialog() {


    }

    @Override
    public void onErrorSaveLocation() {

    }

    public static SplashFragment newInstance(String location) {
        SplashFragment fragment = new SplashFragment();
        fragment.location = location;
        return fragment;
    }
}
