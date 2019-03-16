package ro.cosma.andrei.sunnydays.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.utils.AppConstants;
import ro.cosma.andrei.sunnydays.utils.AppUtils;
import ro.cosma.andrei.sunnydays.utils.PreferencesUtils;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_FORMATTED_UNIT_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_FORMATTER_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link CurrentWeatherWidgetConfigureActivity CurrentWeatherWidgetConfigureActivity}
 */
public class CurrentWeatherWidgetProvider extends AppWidgetProvider {
    public static final String TAG = CurrentWeatherWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.i(TAG, "onUpdate:");
        for (int appWidgetId : appWidgetIds) {
            Log.i(TAG, "onUpdate: " + appWidgetId);
            updateAppWidget(context, appWidgetManager, appWidgetId, null);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        for (int i : appWidgetIds
                ) {
            Log.i(TAG, "onDeleted: " + i);
        }
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            PreferencesUtils.deleteCityIdMapToWidgetId(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action, int appWidgetId) {
        Intent intent = new Intent(context, CurrentWeatherWidgetProvider.class);
        Log.i(TAG, "getPendingSelfIntent: " + intent);
        intent.setAction(action);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, WeatherBean bean) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);

        if (bean != null) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.current_weather_widget);
            views.setTextViewText(R.id.location, bean.getCity().concat(" - ").concat(bean.getCountry()));
            views.setTextViewText(R.id.time, AppUtils.dateFormatterHourOnly(bean.getTime()));
            //views.setImageViewResource(R.id.icon, Picasso.with(this).load().into(););

            if (TEMPERATURE_UNIT_METRIC.equalsIgnoreCase(tempUnit)) {
                views.setTextViewText(R.id.temp, String.valueOf(AppUtils.kelvinToCelsius(bean.getTemperature())));
                views.setTextViewText(R.id.unit, TEMPERATURE_FORMATTED_UNIT_METRIC);
            } else {
                views.setTextViewText(R.id.temp, String.valueOf(AppUtils.kelvinToFahrenheit(bean.getTemperature())));
                views.setTextViewText(R.id.unit, TEMPERATURE_FORMATTER_UNIT_IMPERIAL);
            }

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

}

