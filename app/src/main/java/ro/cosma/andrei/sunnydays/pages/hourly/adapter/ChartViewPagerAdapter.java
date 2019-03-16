package ro.cosma.andrei.sunnydays.pages.hourly.adapter;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.ChartEntryBean;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.hourly.ChartPageFragment;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.HUMIDITY_UNIT;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;

/*Created by Cosma Andrei
* 9/22/2017*/
@Deprecated
public class ChartViewPagerAdapter extends FragmentStatePagerAdapter {
    public static final String TAG = ChartViewPagerAdapter.class.getSimpleName();

    private Context context;

    private List<ChartEntryBean> tempBeans = new ArrayList<>();
    private ChartPageFragment temperatureFragment;

    private List<ChartEntryBean> pressureBeans = new ArrayList<>();
    private ChartPageFragment pressureFragment;

    private List<ChartEntryBean> humidityBeans = new ArrayList<>();
    private ChartPageFragment humidityFragment;

    private List<ChartEntryBean> windSpeedBeans = new ArrayList<>();
    private ChartPageFragment windSpeedFragment;

    List<WeatherBean> weatherBeen;

    public ChartViewPagerAdapter(Context context, FragmentManager fm, List<WeatherBean> weatherBeen) {
        super(fm);
        this.context = context;
        this.weatherBeen = weatherBeen;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getTemperatureFragment();
            case 1:
                return getPressureFragment();

            case 2:
                return getHumidityFragment();

            case 3:
                return getWindSpeedFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    public void setData(List<WeatherBean> data) {
        this.weatherBeen = data;

        setTempBeans();
        setPressureBeans();
        setHumidityBeans();
        setWindSpeedBeans();

        notifyDataSetChanged();
    }

    public ChartPageFragment getTemperatureFragment() {
        if (temperatureFragment == null) {
            temperatureFragment = ChartPageFragment.newInstance(tempBeans, context.getString(R.string.temperature), AppUtils.getFormattedTemperatureUnit(PreferenceManager.getDefaultSharedPreferences(context).getString(PREFERENCES_TEMPERATURE, TEMPERATURE_UNIT_METRIC)));
        }
        return temperatureFragment;
    }

    public ChartPageFragment getPressureFragment() {
        if (pressureFragment == null) {
            pressureFragment = ChartPageFragment.newInstance(pressureBeans, context.getString(R.string.pressure), PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.PREFERENCES_PRESSURE, AppConstants.PRESSURE_HPA));
        }
        return pressureFragment;
    }

    public ChartPageFragment getHumidityFragment() {
        if (humidityFragment == null) {
            humidityFragment = ChartPageFragment.newInstance(humidityBeans, context.getString(R.string.humidity), HUMIDITY_UNIT);
        }
        return humidityFragment;
    }

    public ChartPageFragment getWindSpeedFragment() {
        if (windSpeedFragment == null) {
            windSpeedFragment = ChartPageFragment.newInstance(windSpeedBeans, context.getString(R.string.wind_speed), PreferenceManager.getDefaultSharedPreferences(context).getString(AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_M_S));
        }
        return windSpeedFragment;
    }

    public void setTempBeans() {
        tempBeans.clear();
        for (WeatherBean bean : weatherBeen) {

            tempBeans.add(new ChartEntryBean(bean.getTemperature(), AppUtils.dateFormatterHourOnly(bean.getTime())));
        }
    }

    public void setPressureBeans() {
        pressureBeans.clear();
        for (WeatherBean bean : weatherBeen) {
            pressureBeans.add(new ChartEntryBean(bean.getPressure(), AppUtils.dateFormatterHourOnly(bean.getTime())));
        }
    }

    public void setHumidityBeans() {
        humidityBeans.clear();
        for (WeatherBean bean : weatherBeen) {
            humidityBeans.add(new ChartEntryBean(bean.getHumidity(), AppUtils.dateFormatterHourOnly(bean.getTime())));
        }
    }

    public void setWindSpeedBeans() {
        windSpeedBeans.clear();
        for (WeatherBean bean : weatherBeen) {
            windSpeedBeans.add(new ChartEntryBean(bean.getWindSpeed(), AppUtils.dateFormatterHourOnly(bean.getTime())));
        }
    }
}
