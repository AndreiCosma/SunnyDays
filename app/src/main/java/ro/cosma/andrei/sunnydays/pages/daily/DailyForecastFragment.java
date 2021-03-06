package ro.cosma.andrei.sunnydays.pages.daily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.async.weather.GetWeatherForecastTask;
import ro.cosma.andrei.sunnydays.async.weather.SaveWeatherForecastListTask;
import ro.cosma.andrei.sunnydays.async.weather.listener.IGetWeatherForecastListener;
import ro.cosma.andrei.sunnydays.async.weather.listener.ISaveWeatherForecastListListener;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.daily.adapter.DailyForecastViewPagerAdapter;
import ro.cosma.andrei.sunnydays.pages.daily.listener.IForecastRecyclerViewAdapterListener;
import ro.cosma.andrei.sunnydays.parser.JSonWeatherParser;
import ro.cosma.andrei.sunnydays.service.WeatherService;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static android.view.View.GONE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;

/*Created by Cosma Andrei
* 9/20/2017*/
public class DailyForecastFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, IForecastRecyclerViewAdapterListener {
    public static final String TAG = DailyForecastFragment.class.getSimpleName();

    private IMainActivityListener mActivityListener;

    private SwipeRefreshLayout swipeRefreshLayout;
    private View progressBar;
    private View errorMsgView;

    private View dataContainer;

    private DailyForecastViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    private List<WeatherBean> forecastList = new ArrayList<>();

    @Override
    public void onResume() {
        ((AppCompatActivity) getActivity()).findViewById(R.id.app_bar_layout).setElevation(0);
        mActivityListener.setCurrentFragmentTag(TAG);
        getActivity().setTitle(getString(R.string.daily_forecast));
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof IMainActivityListener) {
            mActivityListener = (IMainActivityListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        if (AppUtils.hasConnectionToInternet(getContext()) && forecastList.isEmpty()) {
            /*Daca are conexiune la internet si lista este goala ia datele de pe server.*/
            fetchData();
        } else {
            /*Ia datele din baza de date daca nu are conexiune la internet sau lista nu este goala (vine din pagina de detalii)*/
            Log.i(TAG, "onStart: Does not have connection or is  coming from details.");
            new GetWeatherForecastTask(new IGetWeatherForecastListener() {
                @Override
                public void showGetWeatherForecastProgressDialog() {

                }

                @Override
                public void onSuccessGetWeatherForecastEntry(List<WeatherBean> been) {
                    forecastList = been;
                    adapter.setData(been);
                    progressBar.setVisibility(GONE);
                    swipeRefreshLayout.setEnabled(true);
                    checkHasData();
                }

                @Override
                public void dismissGetWeatherForecastProgressDialog() {

                }

                @Override
                public void onErrorGetWeatherForecast() {
                    progressBar.setVisibility(GONE);
                    swipeRefreshLayout.setEnabled(true);
                    checkHasData();
                }
            }, getContext(), WeatherBean.TYPE_DAILY_FORECAST).execute(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREFERENCES_LOCATION, null));


        }
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_daily_forecast, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView;
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);

        progressBar = rootView.findViewById(R.id.progress);

        errorMsgView = rootView.findViewById(R.id.error_msg_layout);

        dataContainer = rootView.findViewById(R.id.data_container);


        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);


        adapter = new DailyForecastViewPagerAdapter(getChildFragmentManager(), forecastList);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.seven_days_forecast)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.fourteen_days_forecast)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        if (adapter == null) {
            adapter = new DailyForecastViewPagerAdapter(getChildFragmentManager(), new ArrayList<WeatherBean>());
        }
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(adapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        errorMsgView.setVisibility(GONE);

        return rootView;
    }

    private void checkHasData() {
        if (forecastList.size() == 0) {
            dataContainer.setVisibility(GONE);
            errorMsgView.setVisibility(View.VISIBLE);

        } else {
            errorMsgView.setVisibility(GONE);
            dataContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefresh() {
        if (AppUtils.hasConnectionToInternet(getContext())) {
            fetchData();
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        mActivityListener = null;
        super.onDestroy();
    }

    @Override
    public void onNavigateToDetails(WeatherBean bean) {
        mActivityListener.onNavigateToDetailsDailyForecast(bean);
    }

    private void fetchData() {
        try {
            WeatherService.getInstance().getDailyForecast(getContext(),
                    PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREFERENCES_LOCATION, null),
                    14,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                forecastList = JSonWeatherParser.getInstance().getDailyForecast(response);
                            } catch (JSONException e) {
                                Log.e(TAG, "onResponse: ", e);
                            }
                            adapter.setData(forecastList);
                            progressBar.setVisibility(GONE);
                            swipeRefreshLayout.setEnabled(true);
                            swipeRefreshLayout.setRefreshing(false);
                            checkHasData();
                            /*Daca datele au fost luate cu succes acestea vor fi salvate in baza de date.*/
                            new SaveWeatherForecastListTask(null, getContext(), WeatherBean.TYPE_DAILY_FORECAST, PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREFERENCES_LOCATION, null)).execute(forecastList);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            /*Daca datele nu au putu fi luate de pe internet se incearca incarcarea datelor anterioare din baza  de date.*/
                            new GetWeatherForecastTask(new IGetWeatherForecastListener() {
                                @Override
                                public void showGetWeatherForecastProgressDialog() {

                                }

                                @Override
                                public void onSuccessGetWeatherForecastEntry(List<WeatherBean> been) {
                                    forecastList = been;
                                    adapter.setData(been);
                                    progressBar.setVisibility(GONE);
                                    swipeRefreshLayout.setEnabled(true);
                                    swipeRefreshLayout.setRefreshing(false);
                                    checkHasData();
                                }

                                @Override
                                public void dismissGetWeatherForecastProgressDialog() {

                                }

                                @Override
                                public void onErrorGetWeatherForecast() {
                                    progressBar.setVisibility(GONE);
                                    swipeRefreshLayout.setEnabled(true);
                                    swipeRefreshLayout.setRefreshing(false);
                                    checkHasData();
                                }
                            }, getContext(), WeatherBean.TYPE_DAILY_FORECAST).execute(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(AppConstants.PREFERENCES_LOCATION, null));
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "openPopUpForReceiverNameSelection: ", e);
        }
    }
}
