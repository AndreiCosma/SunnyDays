package ro.cosma.andrei.sunnydays.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import ro.cosma.andrei.sunnydays.R;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_LOCATION;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;

/**
 * Created by andrei on 1/22/2018.
 */

public class PreferencesUtils {

    public static String getPressureUnitPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFERENCES_PRESSURE, PRESSURE_MMGH);
    }

    public static String getTemperatureUnitPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
    }

    public static String getCurrentLocationPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFERENCES_LOCATION, "Craiova");
    }

    public static String getCurrentWindUnitPref(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREFERENCES_WIND, WIND_FORMATTED_UNIT_KM_H);
    }


    public static void saveTemperatureUnitPref(Context context, String tempUnit) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(AppConstants.PREFERENCES_TEMPERATURE, tempUnit).apply();
        ;
    }

    public static void saveCurrentLocationPref(Context context, String location) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(AppConstants.PREFERENCES_LOCATION, location).apply();
        ;
    }

    public static void saveCurrentWindUnitPref(Context context, String windUnit) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(AppConstants.PREFERENCES_WIND, windUnit).apply();
        ;
    }

    public static void savePressureUnitPref(Context context, String pressureUnit) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(AppConstants.PREFERENCES_PRESSURE, pressureUnit).apply();
        ;
    }

    // Write the prefix to the SharedPreferences object for this widget
    public static void mapCityIdToWidgetId(Context context, int appWidgetId, int cityId) {
        context.getSharedPreferences(AppConstants.PREFS_NAME, 0).edit().putInt(AppConstants.PREF_PREFIX_KEY + appWidgetId, cityId).apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    public static int getCityIdFromWidgetId(Context context, int appWidgetId) {
        return context.getSharedPreferences(AppConstants.PREFS_NAME, 0).getInt(AppConstants.PREF_PREFIX_KEY + appWidgetId, 0);

    }

    public static void deleteCityIdMapToWidgetId(Context context, int appWidgetId) {
        context.getSharedPreferences(AppConstants.PREFS_NAME, 0).edit().remove(AppConstants.PREF_PREFIX_KEY + appWidgetId).apply();
    }
}
