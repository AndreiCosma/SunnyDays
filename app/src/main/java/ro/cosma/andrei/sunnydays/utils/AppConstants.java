package ro.cosma.andrei.sunnydays.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/*Created by Cosma Andrei
* 9/17/2017*/
public class AppConstants {

    /*Widget*/
    public static final String PREFS_NAME = "ro.cosma.andrei.sunnydays.widget.CurrentWeatherWidgetProvider";
    public static final String PREF_PREFIX_KEY = "appwidget_";
    public static final String KEY_REFRESH = "keyRef";

    /*Key*/
    public static final String CUSTOM_TEMPERATURE = "cTemp";
    public static final String CUSTOM_WIND = "cWind";
    public static final String CUSTOM_PRESSURE = "cPressure";
    /*Key*/
    public static final String PREFERENCES_MODE = "mode";
    public static final String PREFERENCES_TEMPERATURE = "temp";
    public static final String PREFERENCES_WIND = "wind";
    public static final String PREFERENCES_PRESSURE = "pressure";
    public static final String PREFERENCES_LOCATION = "LC";

    /*Value*/
    public static final String MODE_IMPERIAL = "imperial";
    public static final String MODE_METRIC = "metric";
    public static final String MODE_CUSTOM = "custom";

    /*Value*/
    public static final String TEMPERATURE_UNIT_METRIC = "metric";
    public static final String TEMPERATURE_UNIT_IMPERIAL = "imperial";

    public static final String TEMPERATURE_FORMATTED_UNIT_METRIC = "°C";
    public static final String TEMPERATURE_FORMATTER_UNIT_IMPERIAL = "°F";

    /*Value*/
    public static final String WIND_FORMATTED_UNIT_MILES_H = "mph";
    public static final String WIND_FORMATTED_UNIT_KM_H = "km/h";
    public static final String WIND_FORMATTED_UNIT_M_S = "m/s";

    /*Value*/
    public static final String PRESSURE_MMGH = "mmHg";
    public static final String PRESSURE_HPA = "hPa";

    /*Value*/
    public static final String HUMIDITY_UNIT = "%";


    public static final String FORECAST_BEAN = "FB";
    public static final String CURRENT_WEATHER_FORECAST = "CWF";
    public static final String WEATHER_FORECAST = "WF";
    public static final String CURRENT_WEATHER_SAVE = "currentWeatherBean";
    public static HashMap<String, String> ICON_MAPPING = new HashMap<String, String>();
}
