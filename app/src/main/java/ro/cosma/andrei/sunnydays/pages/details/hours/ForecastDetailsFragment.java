package ro.cosma.andrei.sunnydays.pages.details.hours;

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
* 9/25/2017*/
public class ForecastDetailsFragment extends Fragment {

    public static final String TAG = ForecastDetailsFragment.class.getSimpleName();

    private SharedPreferences preferences;
    private String tempUnit = "";
    private String windUnit = "";
    private String pressureUnit = "";

    private IMainActivityListener mActivityListener;

    private WeatherBean bean;

    private TextView dateTv;

    private TextView descriptionTv;

    private TextView tempTv;
    private TextView minTempTv;
    private TextView maxTempTv;

    private TextView rainVolumeTv;
    private TextView snowVolumeTv;

    private TextView windSpeedValueTv;
    private TextView windDirectionTv;

    private TextView pressureTv;
    private TextView humidityTv;
    private TextView cloudinessTv;

    private ImageView iconTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast_details, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
        windUnit = preferences.getString(PREFERENCES_WIND, WIND_FORMATTED_UNIT_KM_H);
        pressureUnit = preferences.getString(PREFERENCES_PRESSURE, PRESSURE_MMGH);


        if (savedInstanceState != null) {
            bean = (WeatherBean) savedInstanceState.getSerializable(AppConstants.WEATHER_FORECAST);
        }

        dateTv = (TextView) rootView.findViewById(R.id.day_date);

        descriptionTv = (TextView) rootView.findViewById(R.id.weather_description);

        tempTv = (TextView) rootView.findViewById(R.id.temp);
        minTempTv = (TextView) rootView.findViewById(R.id.min_temp_value);
        maxTempTv = (TextView) rootView.findViewById(R.id.max_temp_value);

        rainVolumeTv = (TextView) rootView.findViewById(R.id.rain_volume_value);
        snowVolumeTv = (TextView) rootView.findViewById(R.id.snow_volume_value);

        windSpeedValueTv = (TextView) rootView.findViewById(R.id.wind_speed_value);
        windDirectionTv = (TextView) rootView.findViewById(R.id.wind_orientation_value);

        pressureTv = (TextView) rootView.findViewById(R.id.pressure_value);
        humidityTv = (TextView) rootView.findViewById(R.id.humidity_value);
        cloudinessTv = (TextView) rootView.findViewById(R.id.cloudiness_value);

        iconTv = (ImageView) rootView.findViewById(R.id.weather_icon);

        initUi();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof IMainActivityListener) {
            mActivityListener = (IMainActivityListener) context;
        }
        super.onAttach(context);
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
        outState.putSerializable(AppConstants.WEATHER_FORECAST, bean);
        super.onSaveInstanceState(outState);
    }

    public static ForecastDetailsFragment newInstance(WeatherBean bean) {
        ForecastDetailsFragment fragment = new ForecastDetailsFragment();
        fragment.bean = bean;
        return fragment;
    }

    private void initUi() {
        String tempFormatterUnit = AppUtils.getFormattedTemperatureUnit(tempUnit);

        int formattedTemp = 0;
        int formattedMinTemp = 0;
        int formattedMaxTemp = 0;

        if (TEMPERATURE_UNIT_METRIC.equals(tempUnit)) {
            formattedMinTemp = AppUtils.kelvinToCelsius(bean.getMinTemp());
            formattedMaxTemp = AppUtils.kelvinToCelsius(bean.getMaxTemp());

            formattedTemp = AppUtils.kelvinToCelsius(bean.getTemperature());
        } else if (TEMPERATURE_UNIT_IMPERIAL.equals(tempUnit)) {
            formattedMinTemp = AppUtils.kelvinToFahrenheit(bean.getMinTemp());
            formattedMaxTemp = AppUtils.kelvinToFahrenheit(bean.getMaxTemp());

            formattedTemp = AppUtils.kelvinToFahrenheit(bean.getTemperature());
        }


        tempTv.setText(String.valueOf(formattedTemp).concat(tempFormatterUnit));
        minTempTv.setText(String.valueOf(formattedMinTemp).concat(tempFormatterUnit));
        maxTempTv.setText(String.valueOf(formattedMaxTemp).concat(tempFormatterUnit));

        double formattedWindSpeed = bean.getWindSpeed();

        if (WIND_FORMATTED_UNIT_MILES_H.equals(windUnit)) {
            formattedWindSpeed = AppUtils.metersPerSecondToMilesPerHour(bean.getWindSpeed());
        } else if (WIND_FORMATTED_UNIT_KM_H.equals(windUnit)) {
            formattedWindSpeed = AppUtils.metersPerSecondToKmPerHour(bean.getWindSpeed());
        }

        windSpeedValueTv.setText(String.valueOf(formattedWindSpeed).concat(" ").concat(windUnit));
        windDirectionTv.setText(String.valueOf(bean.getWindDirection()).concat("°"));

        float formattedPressure = bean.getPressure();

        if (PRESSURE_MMGH.equals(pressureUnit)) {
            formattedPressure = AppUtils.hPaToMmHG((int) formattedPressure);
        }

        pressureTv.setText(String.valueOf((int) formattedPressure).concat(" ").concat(pressureUnit));


        dateTv.setText(bean.getHumanReadableDate());
        descriptionTv.setText(bean.getDescription());

        rainVolumeTv.setText(String.valueOf(bean.getRainVolume()).concat(" mm"));
        snowVolumeTv.setText(String.valueOf(bean.getSnowVolume()).concat(" mm"));

        humidityTv.setText(String.valueOf(bean.getHumidity()).concat("%"));
        cloudinessTv.setText(String.valueOf(bean.getCloudiness()).concat("%"));

        Picasso.with(getContext()).load(bean.getIconId()).into(iconTv);
    }
}
