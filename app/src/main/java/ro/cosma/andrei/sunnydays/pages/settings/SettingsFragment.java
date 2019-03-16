package ro.cosma.andrei.sunnydays.pages.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.activity.listener.IMainActivityListener;
import ro.cosma.andrei.sunnydays.components.CustomRadioButtonGroup;
import ro.cosma.andrei.sunnydays.components.listener.ICustomRadioButtonGroupListener;
import ro.cosma.andrei.sunnydays.utils.AppConstants;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.CUSTOM_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.CUSTOM_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.CUSTOM_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_HPA;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_MILES_H;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_M_S;

/*Created by Cosma Andrei
* 9/25/2017*/
public class SettingsFragment extends Fragment implements ICustomRadioButtonGroupListener {

    private SharedPreferences preferences;

    public static final String TAG = SettingsFragment.class.getSimpleName();

    private IMainActivityListener mActivityListener;

    private View temperatureHolder;
    private View windHolder;
    private View pressureHolder;

    private CustomRadioButtonGroup groupMode;
    private final byte GROUP_MODE = 1;

    private CustomRadioButtonGroup groupTemperature;
    private final byte GROUP_TEMPERATURE = 2;

    private CustomRadioButtonGroup groupWind;
    private final byte GROUP_WIND = 3;

    private CustomRadioButtonGroup groupPressure;
    private final byte GROUP_PRESSURE = 4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        temperatureHolder = rootView.findViewById(R.id.temperature_holder);
        windHolder = rootView.findViewById(R.id.wind_holder);
        pressureHolder = rootView.findViewById(R.id.pressure_holder);


        groupMode = new CustomRadioButtonGroup(this, GROUP_MODE);
        groupMode.addRadioButton((RadioButton) rootView.findViewById(R.id.units_mode_metric));
        groupMode.addRadioButton((RadioButton) rootView.findViewById(R.id.units_mode_imperial));
        groupMode.addRadioButton((RadioButton) rootView.findViewById(R.id.units_mode_custom));

        groupTemperature = new CustomRadioButtonGroup(this, GROUP_TEMPERATURE);
        groupTemperature.addRadioButton((RadioButton) rootView.findViewById(R.id.temp_metric_radio_button));
        groupTemperature.addRadioButton((RadioButton) rootView.findViewById(R.id.temp_imperial_radio_button));

        groupWind = new CustomRadioButtonGroup(this, GROUP_WIND);
        groupWind.addRadioButton((RadioButton) rootView.findViewById(R.id.wind_miles_h_radio_button));
        groupWind.addRadioButton((RadioButton) rootView.findViewById(R.id.wind_km_h_radio_button));
        groupWind.addRadioButton((RadioButton) rootView.findViewById(R.id.wind_m_s_radio_button));

        groupPressure = new CustomRadioButtonGroup(this, GROUP_PRESSURE);
        groupPressure.addRadioButton((RadioButton) rootView.findViewById(R.id.pressure_mmhg_radio_button));
        groupPressure.addRadioButton((RadioButton) rootView.findViewById(R.id.pressure_hpa_radio_button));

        initModeFields();

        return rootView;
    }

    @Override
    public void onResume() {
        mActivityListener.setCurrentFragmentTag(TAG);
        getActivity().setTitle(getString(R.string.settings));
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
    public void onDestroy() {
        mActivityListener = null;
        super.onDestroy();
    }

    @Override
    public void onRadioButtonPressed(final int id, byte groupId) {
        switch (groupId) {
            case GROUP_MODE:
                switch (id) {
                    case R.id.units_mode_metric:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_TEMPERATURE, AppConstants.TEMPERATURE_UNIT_METRIC).apply();
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_KM_H).apply();
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_PRESSURE, AppConstants.PRESSURE_MMGH).apply();
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_MODE, AppConstants.MODE_METRIC).apply();
                /*Close selection UI if visible*/
                        hideSelectionUI();
                        break;

                    case R.id.units_mode_imperial:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_TEMPERATURE, AppConstants.TEMPERATURE_UNIT_IMPERIAL).apply();
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_MILES_H).apply();
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_PRESSURE, AppConstants.PRESSURE_MMGH).apply();
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_MODE, AppConstants.MODE_IMPERIAL).apply();
                /*Close selection UI if visible*/
                        hideSelectionUI();
                        break;
                    case R.id.units_mode_custom:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_MODE, AppConstants.MODE_CUSTOM).apply();
                /*Show selction UI*/
                        changePreferencesToCustomSelection();
                        initCustomFields();
                        showSelectionUI();
                        break;
                }
                break;

            case GROUP_PRESSURE:
                switch (id) {
                    case R.id.pressure_mmhg_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_PRESSURE, AppConstants.PRESSURE_MMGH).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_PRESSURE, AppConstants.PRESSURE_MMGH).apply();
                        break;

                    case R.id.pressure_hpa_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_PRESSURE, AppConstants.PRESSURE_HPA).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_PRESSURE, AppConstants.PRESSURE_HPA).apply();
                        break;
                }

                break;

            case GROUP_TEMPERATURE:
                switch (id) {
                    case R.id.temp_metric_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_TEMPERATURE, TEMPERATURE_UNIT_METRIC).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_TEMPERATURE, TEMPERATURE_UNIT_METRIC).apply();
                        break;

                    case R.id.temp_imperial_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_TEMPERATURE, AppConstants.TEMPERATURE_UNIT_IMPERIAL).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_TEMPERATURE, AppConstants.TEMPERATURE_UNIT_IMPERIAL).apply();
                        break;
                }
                break;

            case GROUP_WIND:
                switch (id) {
                    case R.id.wind_miles_h_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_MILES_H).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_WIND, AppConstants.WIND_FORMATTED_UNIT_MILES_H).apply();
                        break;

                    case R.id.wind_km_h_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_KM_H).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_WIND, AppConstants.WIND_FORMATTED_UNIT_KM_H).apply();
                        break;

                    case R.id.wind_m_s_radio_button:
                        preferences.edit().putString(
                                AppConstants.PREFERENCES_WIND, AppConstants.WIND_FORMATTED_UNIT_M_S).apply();
                        preferences.edit().putString(
                                AppConstants.CUSTOM_WIND, AppConstants.WIND_FORMATTED_UNIT_M_S).apply();
                        break;
                }
                break;
        }
    }

    private void initModeFields() {
        /*Set selection mode*/
        switch (preferences.getString(AppConstants.PREFERENCES_MODE, " ")) {
            case AppConstants.MODE_METRIC:
                groupMode.setChecked(R.id.units_mode_metric);
                break;
            case AppConstants.MODE_IMPERIAL:
                groupMode.setChecked(R.id.units_mode_imperial);
                break;
            case AppConstants.MODE_CUSTOM:
                groupMode.setChecked(R.id.units_mode_custom);
                initCustomFields();
                showSelectionUI();
                break;
        }
    }

    private void initCustomFields() {
        /*Temperature radio button initialisation.*/
        switch (preferences.getString(CUSTOM_TEMPERATURE, " ")) {
            case TEMPERATURE_UNIT_METRIC:
                groupTemperature.setChecked(R.id.temp_metric_radio_button);
                break;
            case TEMPERATURE_UNIT_IMPERIAL:
                groupTemperature.setChecked(R.id.temp_imperial_radio_button);
                break;
        }

		/*Wind radio button initialisation.*/
        switch (preferences.getString(CUSTOM_WIND, " ")) {
            case WIND_FORMATTED_UNIT_MILES_H:
                groupWind.setChecked(R.id.wind_miles_h_radio_button);
                break;
            case WIND_FORMATTED_UNIT_KM_H:
                groupWind.setChecked(R.id.wind_km_h_radio_button);
                break;
            case WIND_FORMATTED_UNIT_M_S:
                groupWind.setChecked(R.id.wind_m_s_radio_button);
                break;
        }

		/*Pressure radio button initialisation.*/
        switch (preferences.getString(CUSTOM_PRESSURE, " ")) {
            case PRESSURE_MMGH:
                groupPressure.setChecked(R.id.pressure_mmhg_radio_button);
                break;
            case PRESSURE_HPA:
                groupPressure.setChecked(R.id.pressure_hpa_radio_button);
                break;
        }
    }

    private void changePreferencesToCustomSelection() {
        preferences.edit().putString(AppConstants.PREFERENCES_TEMPERATURE,
                preferences.getString(CUSTOM_TEMPERATURE, TEMPERATURE_UNIT_METRIC)).apply();
        preferences.edit().putString(AppConstants.PREFERENCES_WIND,
                preferences.getString(CUSTOM_WIND, WIND_FORMATTED_UNIT_KM_H)).apply();
        preferences.edit().putString(AppConstants.PREFERENCES_PRESSURE,
                preferences.getString(CUSTOM_PRESSURE, PRESSURE_MMGH)).apply();
    }

    private void showSelectionUI() {
        temperatureHolder.setVisibility(View.VISIBLE);
        windHolder.setVisibility(View.VISIBLE);
        pressureHolder.setVisibility(View.VISIBLE);
    }

    private void hideSelectionUI() {
        temperatureHolder.setVisibility(View.GONE);
        windHolder.setVisibility(View.GONE);
        pressureHolder.setVisibility(View.GONE);
    }
}
