package ro.cosma.andrei.sunnydays.pages.current;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.async.weather.DeleteSaveWeatherForecastTask;
import ro.cosma.andrei.sunnydays.async.weather.GetWeatherForecastTask;
import ro.cosma.andrei.sunnydays.async.weather.SaveWeatherForecastTask;
import ro.cosma.andrei.sunnydays.async.weather.listener.IGetWeatherForecastListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.parser.JSonWeatherParser;
import ro.cosma.andrei.sunnydays.service.WeatherService;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static android.view.View.GONE;
import static ro.cosma.andrei.sunnydays.R.id.temp;
import static ro.cosma.andrei.sunnydays.R.id.weather_description;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_LOCATION;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_MILES_H;

/*Created by Cosma Andrei
* 9/17/2017*/
public class CurrentWeatherFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG = CurrentWeatherFragment.class.getSimpleName();

    private SharedPreferences preferences;
    private String tempUnit = "";
    private String windUnit = "";
    private String location = "";

    private IMainActivityListener mActivityListener;

    private ProgressBar progressBar;

    private WeatherBean mCurrentWeatherBean;

    private SwipeRefreshLayout swipeRefreshLayout;

    private View weatherDataContainer;
    private View errorView;

    private TextView pageHeadlineTv;
    private TextView subheadingTv;
    private TextView temperatureTv;
    private TextView temperatureUnitTv;
    private TextView windSpeedValueTV;
    private TextView windSpeedUnitTv;
    private TextView humidityValueTv;
    private TextView humidityUnitTv;
    private TextView navigateToTodayReportTv;

    private ImageView weatherIcon;


    @Override
    public void onAttach(Context context) {
        if (context instanceof IMainActivityListener) {
            mActivityListener = (IMainActivityListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.dashboard_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_weather, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
        windUnit = preferences.getString(PREFERENCES_WIND, WIND_FORMATTED_UNIT_KM_H);
        location = preferences.getString(PREFERENCES_LOCATION, " ");
        progressBar = rootView.findViewById(R.id.progress);


        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresher_view);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);

        weatherDataContainer = rootView.findViewById(R.id.container);
        errorView = rootView.findViewById(R.id.error_msg_layout);

        pageHeadlineTv = (TextView) rootView.findViewById(R.id.page_headline);
        subheadingTv = (TextView) rootView.findViewById(R.id.subheading);
        temperatureTv = (TextView) rootView.findViewById(R.id.temperature);
        temperatureUnitTv = (TextView) rootView.findViewById(R.id.unit);
        windSpeedValueTV = (TextView) rootView.findViewById(R.id.wind_speed_value);
        windSpeedUnitTv = (TextView) rootView.findViewById(R.id.wind_speed_unit);
        humidityValueTv = (TextView) rootView.findViewById(R.id.precipitation_value);
        humidityUnitTv = (TextView) rootView.findViewById(R.id.precipitation_unit);


        weatherIcon = (ImageView) rootView.findViewById(R.id.weather_icon);

        initLayoutData();

        setHasOptionsMenu(true);
        return rootView;
    }

    public static CurrentWeatherFragment newInstance(WeatherBean currentWeatherBean) {
        CurrentWeatherFragment fragment = new CurrentWeatherFragment();
        fragment.mCurrentWeatherBean = currentWeatherBean;
        return fragment;
    }

    @Override
    public void onResume() {
        mActivityListener.setCurrentFragmentTag(TAG);
        getActivity().setTitle(getString(R.string.current_weather));
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mActivityListener = null;
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {
        if (AppUtils.hasConnectionToInternet(getContext())) {
            fetchData();
        } else {
            if (isAdded()) {
                Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
        Toast.makeText(getActivity(), getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            mCurrentWeatherBean = JSonWeatherParser.getInstance().getCurrentWeather(response);
            List<WeatherBean> list = new ArrayList<>();
            list.add(mCurrentWeatherBean);
            new DeleteSaveWeatherForecastTask(null, getContext(), WeatherBean.TYPE_CURRENT).execute(list);
            initLayoutData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
    }


    @Override
    public void onStart() {
        if (AppUtils.hasConnectionToInternet(getContext()) && mCurrentWeatherBean == null) {
            fetchData();
        } else if (mCurrentWeatherBean == null) {
            new GetWeatherForecastTask(new IGetWeatherForecastListener() {
                @Override
                public void showGetWeatherForecastProgressDialog() {

                }

                @Override
                public void onSuccessGetWeatherForecastEntry(List<WeatherBean> been) {
                    if (been != null && been.size() > 0) {
                        mCurrentWeatherBean = been.get(0);
                        initLayoutData();
                    }
                    progressBar.setVisibility(GONE);
                    swipeRefreshLayout.setEnabled(true);
                }

                @Override
                public void dismissGetWeatherForecastProgressDialog() {

                }

                @Override
                public void onErrorGetWeatherForecast() {
                    progressBar.setVisibility(GONE);
                    swipeRefreshLayout.setEnabled(true);
                }
            },
                    getContext(),
                    WeatherBean.TYPE_CURRENT).execute(location);

        } else {
            progressBar.setVisibility(GONE);
            swipeRefreshLayout.setEnabled(true);
        }
        super.onStart();
    }

    public void initLayoutData() {

        if (mCurrentWeatherBean != null) {
            int formattedTemperature = 0;
            double formattedWindSpeed = mCurrentWeatherBean.getWindSpeed();

            if (TEMPERATURE_UNIT_METRIC.equals(tempUnit)) {
                formattedTemperature = AppUtils.kelvinToCelsius(mCurrentWeatherBean.getTemperature());
            } else if (TEMPERATURE_UNIT_IMPERIAL.equals(tempUnit)) {
                formattedTemperature = AppUtils.kelvinToFahrenheit(mCurrentWeatherBean.getTemperature());
            }

            if (WIND_FORMATTED_UNIT_MILES_H.equals(windUnit)) {
                formattedWindSpeed = AppUtils.metersPerSecondToMilesPerHour(mCurrentWeatherBean.getWindSpeed());
            } else if (WIND_FORMATTED_UNIT_KM_H.equals(windUnit)) {
                formattedWindSpeed = AppUtils.metersPerSecondToKmPerHour(mCurrentWeatherBean.getWindSpeed());
            }

            temperatureTv.setText(String.valueOf(formattedTemperature));
            temperatureUnitTv.setText(AppUtils.getFormattedTemperatureUnit(tempUnit));

            windSpeedValueTV.setText(String.valueOf(formattedWindSpeed));
            windSpeedUnitTv.setText(windUnit);


            pageHeadlineTv.setText(mCurrentWeatherBean.getCity().concat(" - ").concat(mCurrentWeatherBean.getCountry()));
            subheadingTv.setText(AppUtils.dateFormatter(new Date()).concat(" - ").concat(mCurrentWeatherBean.getDescription()));
            humidityValueTv.setText(String.valueOf(mCurrentWeatherBean.getHumidity()));
            Picasso.with(getContext()).load(mCurrentWeatherBean.getIconId()).into(weatherIcon);
        }
        checkHasData();
    }

    private void checkHasData() {
        if (mCurrentWeatherBean != null) {
            progressBar.setVisibility(GONE);
            errorView.setVisibility(GONE);
            weatherDataContainer.setVisibility(View.VISIBLE);
        } else {
            weatherDataContainer.setVisibility(GONE);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    private void fetchData() {
        try {
            WeatherService.getInstance().getCurrentWeather(getContext(),
                    PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREFERENCES_LOCATION, null),
                    this,
                    this);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
