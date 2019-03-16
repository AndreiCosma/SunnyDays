package ro.cosma.andrei.sunnydays.pages.daily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.daily.adapter.ForecastRecyclerViewAdapter;
import ro.cosma.andrei.sunnydays.pages.daily.listener.IForecastRecyclerViewAdapterListener;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;


/*Created by Cosma Andrei
* 9/24/2017*/
public class PageDailyForecastFragment extends Fragment implements IForecastRecyclerViewAdapterListener {
    private List<WeatherBean> list;

    private SharedPreferences preferences;
    private String tempUnit = "";
    private String windUnit = "";
    private String pressureUnit = "";

    private IMainActivityListener mActivityListener;

    private RecyclerView recyclerView;

    private ForecastRecyclerViewAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page_daily_forecast, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
        windUnit = preferences.getString(PREFERENCES_WIND, WIND_FORMATTED_UNIT_KM_H);
        pressureUnit = preferences.getString(PREFERENCES_PRESSURE, PRESSURE_MMGH);

        if (adapter == null) {
            adapter = new ForecastRecyclerViewAdapter(this, list, getContext());
        }

        recyclerView = (RecyclerView) rootView.findViewById(R.id.day_forecast_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

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
    public void onNavigateToDetails(WeatherBean bean) {
        mActivityListener.onNavigateToDetailsDailyForecast(bean);

    }

    @Override
    public void onDestroy() {
        mActivityListener = null;
        super.onDestroy();
    }

    public static PageDailyForecastFragment newInstance(List<WeatherBean> list) {
        PageDailyForecastFragment fragment = new PageDailyForecastFragment();
        fragment.list = list;
        return fragment;
    }
}
