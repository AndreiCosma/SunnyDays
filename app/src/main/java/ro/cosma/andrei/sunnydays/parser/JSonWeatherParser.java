package ro.cosma.andrei.sunnydays.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static java.lang.Long.getLong;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.ICON_MAPPING;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_MILES_H;

/*Created by Cosma Andrei
* 9/17/2017*/
public class JSonWeatherParser {
    public static final String TAG = JSonWeatherParser.class.getSimpleName();
    private static JSonWeatherParser instance;


    private JSonWeatherParser() {

    }

    public static JSonWeatherParser getInstance() {
        if (instance == null) {
            instance = new JSonWeatherParser();
        }

        if (ICON_MAPPING.isEmpty()) {
            AppUtils.mapIcons();
        }

        return instance;
    }


    private String WEATHER_WEATHER = "weather";
    private String WEATHER_WEATHER_ID = "id";
    private String WEATHER_WEATHER_MAIN = "main";
    private String WEATHER_WEATHER_DESCRIPTION = "description";
    private String WEATHER_WEATHER_ICON = "icon";

    private static final String ID = "id";
    private String WEATHER_MAIN = "main";
    private String WEATHER_MAIN_TEMP = "temp";
    private String WEATHER_MAIN_PRESSURE = "pressure";
    private String WEATHER_MAIN_HUMIDITY = "humidity";
    private String WEATHER_MAIN_TEMP_MIN = "temp_min";
    private String WEATHER_MAIN_TEMP_MAX = "temp_max";
    private String WEATHER_MAIN_SEA_LEVEL = "sea_level";
    private String WEATHER_MAIN_GROUND_LEVEL = "grnd_level";

    private static final String COORD = "coord";
    private static final String LON = "lon";
    private static final String LAT = "lat";

    private String WEATHER_WIND = "wind";
    private String WEATHER_WIND_SPEED = "speed";
    private String WEATHER_WIND_DEGREES = "deg";

    private String WEATHER_CLOUDS = "clouds";
    private String WEATHER_CLOUDS_ALL = "all";

    private String WEATHER_RAIN = "rain";
    private String WEATHER_RAIN_ALL = "3h";

    private String WEATHER_SNOW = "snow";
    private String WEATHER_SNOW_ALL = "3h";

    private String WEATHER_DATA_TIME = "dt";

    private String WEATHER_SYS = "sys";

    private String SUNRISE = "sunrise";

    private String SUNSET = "sunset";

    private String WEATHER_SYS_COUNTRY = "country";

    private static final String WEATHER_CITY = "city";
    private String WEATHER_CITY_NAME = "name";

    private String LIST = "list";
    private static final String TEMP = "temp";
    private static final String DAY = "day";
    private static final String NIGHT = "night";
    private static final String EVE = "eve";
    private static final String MORN = "morn";
    private static final String MIN = "min";
    private static final String MAX = "max";


    public List<LocationBean> getLocations(JSONObject response) throws JSONException {
        List<LocationBean> list = new ArrayList<>();
        JSONArray array = response.getJSONArray(LIST);
        for (int i = 0; i < array.length(); i++) {
            list.add(getLocation(array.getJSONObject(i)));
        }
        return list;
    }

    public LocationBean getLocation(JSONObject response) throws JSONException {
        LocationBean result = new LocationBean();


        result.setTemperature(response.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP));

        result.setLon(response.getJSONObject(COORD).getLong(LON));
        result.setLat(response.getJSONObject(COORD).getLong(LAT));
        result.setCountry(response.getJSONObject(WEATHER_SYS).getString(WEATHER_SYS_COUNTRY));
        result.setCity(response.getString(WEATHER_CITY_NAME));

        result.setIconId(AppUtils.getImageResourceId(response.getJSONArray(WEATHER_WEATHER).getJSONObject(0).getString(WEATHER_WEATHER_ICON)));
        result.setId(response.getInt(ID));

        return result;
    }

    public WeatherBean getCurrentWeather(JSONObject response) throws JSONException {
        WeatherBean result = new WeatherBean();
        result.setBeanType(WeatherBean.TYPE_CURRENT);

		/*Weather object*/
        JSONObject firstEntryWeatherArray = response.getJSONArray(WEATHER_WEATHER).getJSONObject(0);
        result.setId(firstEntryWeatherArray.getInt(WEATHER_WEATHER_ID));
        result.setMain(firstEntryWeatherArray.getString(WEATHER_WEATHER_MAIN));
        result.setDescription(firstEntryWeatherArray.getString(WEATHER_WEATHER_DESCRIPTION));
        result.setIconId(AppUtils.getImageResourceId(firstEntryWeatherArray.getString(WEATHER_WEATHER_ICON)));

        result.setMinTemp(response.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP_MIN));
        result.setMaxTemp(response.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP_MAX));
        result.setTemperature(response.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP));

        if (response.getJSONObject(WEATHER_MAIN).has(WEATHER_MAIN_SEA_LEVEL)) {
            result.setSeaLevelPressure(response.getJSONObject(WEATHER_MAIN).getInt(WEATHER_MAIN_SEA_LEVEL));
            result.setHasSeaLevelPressure(true);
        }
        if (response.getJSONObject(WEATHER_MAIN).has(WEATHER_MAIN_GROUND_LEVEL)) {
            result.setGroundLvlPressure(response.getJSONObject(WEATHER_MAIN).getInt(WEATHER_MAIN_GROUND_LEVEL));
            result.setHasGroundPressureLevel(true);
        }

        result.setPressure(response.getJSONObject(WEATHER_MAIN).getInt(WEATHER_MAIN_PRESSURE));

        result.setWindSpeed(response.getJSONObject(WEATHER_WIND).getDouble(WEATHER_WIND_SPEED));


        result.setHumidity(response.getJSONObject(WEATHER_MAIN).getInt(WEATHER_MAIN_HUMIDITY));

        if (response.getJSONObject(WEATHER_WIND).has(WEATHER_WIND_DEGREES)) {
            result.setWindDirection(response.getJSONObject(WEATHER_WIND).getInt(WEATHER_WIND_DEGREES));
        }

        result.setCloudiness(response.getJSONObject(WEATHER_CLOUDS).getLong(WEATHER_CLOUDS_ALL));

        result.setTime(TimeUnit.SECONDS.toMillis(response.getInt(WEATHER_DATA_TIME)));
        result.setTimeNew(new Date(response.getInt(WEATHER_DATA_TIME)));

        result.setCountry(response.getJSONObject(WEATHER_SYS).getString(WEATHER_SYS_COUNTRY));
        result.setCity(response.getString(WEATHER_CITY_NAME));

        result.setSunriseTime(response.getJSONObject(WEATHER_SYS).getInt(SUNRISE));
        result.setSunrise(new Date(response.getJSONObject(WEATHER_SYS).getInt(SUNRISE)));

        result.setHumanReadableSunriseTime(AppUtils.forecastDateFormatter(result.getSunriseTime()));

        result.setSunsetTime(response.getJSONObject(WEATHER_SYS).getInt(SUNSET));
        result.setSunset(new Date(response.getJSONObject(WEATHER_SYS).getInt(SUNSET)));

        result.setHumanReadableSunsetTime(AppUtils.forecastDateFormatter(result.getSunsetTime()));


        if (response.has(WEATHER_SNOW)) {
            result.setSnowVolume(response.getJSONObject(WEATHER_SNOW).getLong(WEATHER_SNOW_ALL));
            result.setHasSnowVolume(true);
        }

        if (response.has(WEATHER_RAIN)) {
            result.setRainVolume(response.getJSONObject(WEATHER_RAIN).getLong(WEATHER_RAIN_ALL));
            result.setHasRainVolume(true);
        }

        result.setHumanReadableDate(AppUtils.dateFormatter(result.getTime()));

        result.setLon(response.getJSONObject(COORD).getLong(LON));
        result.setLat(response.getJSONObject(COORD).getLong(LAT));

        return result;
    }

    public List<WeatherBean> getForecast(JSONObject response) throws JSONException {

        List<WeatherBean> forecastData = new ArrayList<>();
        JSONArray array = response.getJSONArray(LIST);
        int len = array.length();
        for (int i = 0; i < len; i++) {
            JSONObject candidate = array.getJSONObject(i);

            WeatherBean result = new WeatherBean();
            result.setBeanType(WeatherBean.TYPE_FORECAST);

			/*Weather object*/
            JSONObject firstEntryWeatherArray = candidate.getJSONArray(WEATHER_WEATHER).getJSONObject(0);
            result.setId(firstEntryWeatherArray.getInt(WEATHER_WEATHER_ID));
            result.setMain(firstEntryWeatherArray.getString(WEATHER_WEATHER_MAIN));
            result.setDescription(firstEntryWeatherArray.getString(WEATHER_WEATHER_DESCRIPTION));
            result.setIconId(AppUtils.getImageResourceId(firstEntryWeatherArray.getString(WEATHER_WEATHER_ICON)));


            result.setHumidity(candidate.getJSONObject(WEATHER_MAIN).getInt(WEATHER_MAIN_HUMIDITY));


            result.setMinTemp(candidate.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP_MIN));
            result.setMaxTemp(candidate.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP_MAX));
            result.setTemperature(candidate.getJSONObject(WEATHER_MAIN).getDouble(WEATHER_MAIN_TEMP));


            result.setWindSpeed(candidate.getJSONObject(WEATHER_WIND).getDouble(WEATHER_WIND_SPEED));


            result.setPressure(candidate.getJSONObject(WEATHER_MAIN).getInt(WEATHER_MAIN_PRESSURE));


            result.setWindDirection(candidate.getJSONObject(WEATHER_WIND).getInt(WEATHER_WIND_DEGREES));

            if (candidate.has(WEATHER_SNOW)) {
                if (candidate.getJSONObject(WEATHER_SNOW).has(WEATHER_SNOW_ALL)) {
                    result.setSnowVolume(candidate.getJSONObject(WEATHER_SNOW).getLong(WEATHER_SNOW_ALL));
                }
                result.setHasSnowVolume(true);
            }

            if (candidate.has(WEATHER_RAIN)) {
                if (candidate.getJSONObject(WEATHER_RAIN).has(WEATHER_RAIN_ALL)) {
                    result.setRainVolume(candidate.getJSONObject(WEATHER_RAIN).getDouble(WEATHER_RAIN_ALL));
                }
                result.setHasRainVolume(true);
            }

            result.setCloudiness(candidate.getJSONObject(WEATHER_CLOUDS).getLong(WEATHER_CLOUDS_ALL));

            result.setTime(TimeUnit.SECONDS.toMillis(candidate.getInt(WEATHER_DATA_TIME)));
            result.setTimeNew(new Date(candidate.getInt(WEATHER_DATA_TIME)));

            result.setCountry(response.getJSONObject(WEATHER_CITY).getString(WEATHER_SYS_COUNTRY));

            result.setCity(response.getJSONObject(WEATHER_CITY).getString(WEATHER_CITY_NAME));

            result.setHumanReadableDate(AppUtils.forecastDateFormatter(result.getTime()));

            forecastData.add(result);
        }

        return forecastData;
    }

    public List<WeatherBean> getDailyForecast(JSONObject response) throws JSONException {
        List<WeatherBean> forecastData = new ArrayList<>();
        JSONArray array = response.getJSONArray(LIST);
        int len = array.length();
        for (int i = 0; i < len; i++) {
            JSONObject candidate = array.getJSONObject(i);

            WeatherBean result = new WeatherBean();
            result.setBeanType(WeatherBean.TYPE_DAILY_FORECAST);


            result.setCity(response.getJSONObject(WEATHER_CITY).getString(WEATHER_CITY_NAME));
            result.setCountry(response.getJSONObject(WEATHER_CITY).getString(WEATHER_SYS_COUNTRY));

            result.setTime(TimeUnit.SECONDS.toMillis(candidate.getInt(WEATHER_DATA_TIME)));
            result.setTimeNew(new Date(candidate.getInt(WEATHER_DATA_TIME)));

            JSONObject tempObject = candidate.getJSONObject(TEMP);

            result.setTemperature(tempObject.getDouble(DAY));
            result.setMinTemp(tempObject.getDouble(MIN));
            result.setMaxTemp(tempObject.getDouble(MAX));
            result.setNightTemperature(tempObject.getDouble(NIGHT));
            result.setEveningTemperature(tempObject.getDouble(EVE));
            result.setMorningTemperature(tempObject.getDouble(MORN));

            result.setPressure(candidate.getInt(WEATHER_MAIN_PRESSURE));

            result.setHumidity(candidate.getInt(WEATHER_MAIN_HUMIDITY));

			/*Weather object*/
            JSONObject firstEntryWeatherArray = candidate.getJSONArray(WEATHER_WEATHER).getJSONObject(0);
            result.setId(firstEntryWeatherArray.getInt(WEATHER_WEATHER_ID));
            result.setMain(firstEntryWeatherArray.getString(WEATHER_WEATHER_MAIN));
            result.setDescription(firstEntryWeatherArray.getString(WEATHER_WEATHER_DESCRIPTION));
            result.setIconId(AppUtils.getImageResourceId(firstEntryWeatherArray.getString(WEATHER_WEATHER_ICON)));

            result.setWindSpeed(candidate.getDouble(WEATHER_WIND_SPEED));

            result.setWindDirection(candidate.getInt(WEATHER_WIND_DEGREES));
            result.setCloudiness(candidate.getLong(WEATHER_CLOUDS));

            if (candidate.has(WEATHER_RAIN)) {
                result.setRainVolume(candidate.getLong(WEATHER_RAIN));
                result.setHasRainVolume(true);
            }

            result.setHumanReadableDate(AppUtils.dateFormatter(result.getTime()));

            forecastData.add(result);
        }

        return forecastData;
    }

}
