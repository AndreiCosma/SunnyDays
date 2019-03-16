package ro.cosma.andrei.sunnydays.pages.details.daily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static android.text.TextUtils.concat;
import static ro.cosma.andrei.sunnydays.R.string.pressure;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.FORECAST_BEAN;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_MILES_H;

/*Created by Cosma Andrei
* 9/21/2017*/
public class DailyForecastDetailsFragment extends Fragment {
    public static final String TAG = DailyForecastDetailsFragment.class.getSimpleName();

    private SharedPreferences preferences;
    private String tempUnit = "";
    private String windUnit = "";
    private String pressureUnit = "";


    private IMainActivityListener mActivityListener;

    private TextView dateTv;

    private TextView minTv;

    private TextView maxTv;

    private TextView descriptionTv;

    private ImageView iconIv;

    private TextView eveTv;

    private TextView morningTv;

    private TextView dayTv;

    private TextView nightTv;

    private TextView windSpeedTv;

    private TextView windOrientation;

    private TextView pressureTv;

    private TextView humidityTv;

    private TextView cloudinessTv;


    private WeatherBean bean;

    @Override
    public void onAttach(Context context) {
        if (context instanceof IMainActivityListener) {
            mActivityListener = (IMainActivityListener) context;
        }
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily_forecast_details, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
        windUnit = preferences.getString(PREFERENCES_WIND, WIND_FORMATTED_UNIT_KM_H);
        pressureUnit = preferences.getString(PREFERENCES_PRESSURE, PRESSURE_MMGH);


        if (savedInstanceState != null) {
            bean = (WeatherBean) savedInstanceState.getSerializable(FORECAST_BEAN);
        }

        cloudinessTv = (TextView) rootView.findViewById(R.id.cloudiness_value);

        humidityTv = (TextView) rootView.findViewById(R.id.humidity_value);

        pressureTv = (TextView) rootView.findViewById(R.id.pressure_value);

        windOrientation = (TextView) rootView.findViewById(R.id.wind_orientation_value);

        windSpeedTv = (TextView) rootView.findViewById(R.id.wind_speed_value);

        dateTv = (TextView) rootView.findViewById(R.id.day_date);

        maxTv = (TextView) rootView.findViewById(R.id.temp_max);

        minTv = (TextView) rootView.findViewById(R.id.min_temp);

        descriptionTv = (TextView) rootView.findViewById(R.id.weather_description);

        iconIv = (ImageView) rootView.findViewById(R.id.weather_icon);

        eveTv = (TextView) rootView.findViewById(R.id.temp_evening_value);

        morningTv = (TextView) rootView.findViewById(R.id.temp_morning_value);

        dayTv = (TextView) rootView.findViewById(R.id.temp_day_value);

        nightTv = (TextView) rootView.findViewById(R.id.temp_night_value);

        initLayout();

        return rootView;
    }

    public static DailyForecastDetailsFragment newInstance(WeatherBean bean) {
        DailyForecastDetailsFragment fragment = new DailyForecastDetailsFragment();
        fragment.bean = bean;
        return fragment;
    }

    @Override
    public void onResume() {
        mActivityListener.setCurrentFragmentTag(TAG);
        getActivity().setTitle(getString(R.string.details));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        mActivityListener = null;
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FORECAST_BEAN, bean);
        super.onSaveInstanceState(outState);
    }

    public void initLayout() {

        String tempFormatterUnit = AppUtils.getFormattedTemperatureUnit(tempUnit);

        int formattedMinTemp = 0;
        int formattedMaxTemp = 0;

        int formattedMorningTemp = 0;
        int formattedDayTemperature = 0;
        int formattedEveningTemp = 0;
        int formattedNightTemp = 0;

        if (TEMPERATURE_UNIT_METRIC.equals(tempUnit)) {
            formattedMinTemp = AppUtils.kelvinToCelsius(bean.getMinTemp());
            formattedMaxTemp = AppUtils.kelvinToCelsius(bean.getMaxTemp());

            formattedMorningTemp = AppUtils.kelvinToCelsius(bean.getMorningTemperature());
            formattedDayTemperature = AppUtils.kelvinToCelsius(bean.getTemperature());
            formattedEveningTemp = AppUtils.kelvinToCelsius(bean.getEveningTemperature());
            formattedNightTemp = AppUtils.kelvinToCelsius(bean.getNightTemperature());
        } else if (TEMPERATURE_UNIT_IMPERIAL.equals(tempUnit)) {
            formattedMinTemp = AppUtils.kelvinToFahrenheit(bean.getMinTemp());
            formattedMaxTemp = AppUtils.kelvinToFahrenheit(bean.getMaxTemp());

            formattedMorningTemp = AppUtils.kelvinToFahrenheit(bean.getMorningTemperature());
            formattedDayTemperature = AppUtils.kelvinToFahrenheit(bean.getTemperature());
            formattedEveningTemp = AppUtils.kelvinToFahrenheit(bean.getEveningTemperature());
            formattedNightTemp = AppUtils.kelvinToFahrenheit(bean.getNightTemperature());
        }

        minTv.setText(String.valueOf(formattedMinTemp).concat(tempFormatterUnit));

        maxTv.setText(String.valueOf(formattedMaxTemp).concat(tempFormatterUnit));

        eveTv.setText(String.valueOf(formattedEveningTemp).concat(tempFormatterUnit));

        morningTv.setText(String.valueOf(formattedMorningTemp).concat(tempFormatterUnit));

        dayTv.setText(String.valueOf(formattedDayTemperature).concat(tempFormatterUnit));

        nightTv.setText(String.valueOf(formattedNightTemp).concat(tempFormatterUnit));

        double formattedWindSpeed = bean.getWindSpeed();

        if (WIND_FORMATTED_UNIT_MILES_H.equals(windUnit)) {
            formattedWindSpeed = AppUtils.metersPerSecondToMilesPerHour(bean.getWindSpeed());
        } else if (WIND_FORMATTED_UNIT_KM_H.equals(windUnit)) {
            formattedWindSpeed = AppUtils.metersPerSecondToKmPerHour(bean.getWindSpeed());
        }

        windSpeedTv.setText(String.valueOf(formattedWindSpeed).concat(" ").concat(windUnit));

        windOrientation.setText(String.valueOf(bean.getWindDirection()).concat("°"));


        float formattedPressure = bean.getPressure();

        if (PRESSURE_MMGH.equals(pressureUnit)) {
            formattedPressure = AppUtils.hPaToMmHG((int) formattedPressure);
        }

        pressureTv.setText(String.valueOf((int) formattedPressure).concat(" ").concat(pressureUnit));

        humidityTv.setText(String.valueOf(bean.getHumidity()).concat(" %"));

        cloudinessTv.setText(String.valueOf(bean.getCloudiness()).concat(" %"));

        dateTv.setText(bean.getHumanReadableDate());

        descriptionTv.setText(bean.getDescription());

        Picasso.with(getContext()).load(bean.getIconId()).into(iconIv);
    }
}
