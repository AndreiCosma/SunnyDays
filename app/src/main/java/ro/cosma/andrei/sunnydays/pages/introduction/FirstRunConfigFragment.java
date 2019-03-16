package ro.cosma.andrei.sunnydays.pages.introduction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.ISplashActivityListener;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

/*Created by Cosma Andrei
* 9/17/2017*/
public class FirstRunConfigFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = FirstRunConfigFragment.class.getSimpleName();

    private boolean locationNotFound;

    private ISplashActivityListener listener;
    private TextInputEditText locationEt;
    private FloatingActionButton submitFab;

    @Override
    public void onAttach(Context context) {
        if (context instanceof ISplashActivityListener) {
            listener = (ISplashActivityListener) context;
        }
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        if (locationNotFound) {
            if (isAdded()) {
                Toast.makeText(getActivity(), getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
            }
        }
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_first_run_config, container, false);

        locationEt = (TextInputEditText) rootView.findViewById(R.id.weather_location);
        submitFab = (FloatingActionButton) rootView.findViewById(R.id.submit);
        submitFab.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onDestroy() {
        listener = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if(AppUtils.hasConnectionToInternet(getContext())) {
            if (view.getId() == R.id.submit) {
                String location = locationEt.getText().toString();
                if (!location.isEmpty()) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                    preferences.edit().putString(AppConstants.PREFERENCES_MODE, AppConstants.MODE_METRIC).apply();

                    preferences.edit().putString(AppConstants.PREFERENCES_TEMPERATURE, AppConstants.TEMPERATURE_UNIT_METRIC).apply();
                    preferences.edit().putString(AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_KM_H).apply();
                    preferences.edit().putString(AppConstants.PREFERENCES_PRESSURE, AppConstants.PRESSURE_MMGH).apply();

                    preferences.edit().putString(AppConstants.CUSTOM_TEMPERATURE, AppConstants.TEMPERATURE_UNIT_METRIC).apply();
                    preferences.edit().putString(AppConstants.CUSTOM_WIND, AppConstants.WIND_FORMATTED_UNIT_KM_H).apply();
                    preferences.edit().putString(AppConstants.CUSTOM_PRESSURE, AppConstants.PRESSURE_MMGH).apply();

                    listener.onNavigateToSplashFragment(location);
                } else {
                    locationEt.setError(getString(R.string.field_empty));
                }
            }
        }else {
            Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }
        AppUtils.hideKeyboard(getContext());
    }

    public static FirstRunConfigFragment newInstance(boolean locationNotFound) {
        FirstRunConfigFragment fragment = new FirstRunConfigFragment();
        fragment.locationNotFound = locationNotFound;
        return fragment;
    }
}
