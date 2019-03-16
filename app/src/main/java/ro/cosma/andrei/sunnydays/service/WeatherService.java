package ro.cosma.andrei.sunnydays.service;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import ro.cosma.andrei.sunnydays.network.VolleyRequestMaker;

import static com.github.mikephil.charting.charts.Chart.LOG_TAG;

/*Created by Cosma Andrei
* 9/17/2017*/
public class WeatherService {
    public static final String TAG = WeatherService.class.getSimpleName();
    private static WeatherService instance;

    private final String WEATHER_CURRENT_GROUP =
            "http://api.openweathermap.org/data/2.5/group?";


    private final String WEATHER_CURRENT_BASE_URL =
            "http://api.openweathermap.org/data/2.5/weather?";

    private final String WEATHER_FORECAST_BASE_URL =
            "http://api.openweathermap.org/data/2.5/forecast?";

    private final String WEATHER_DAILY_FORECAST_BASE_URL =
            "http://api.openweathermap.org/data/2.5/forecast/daily?";

    private final String ID_PARAM = "id";
    private final String LANG_PARAM = "lang";
    private final String QUERY_PARAM = "q";
    private final String FORMAT_PARAM = "mode";
    private final String UNITS_PARAM = "units";
    private final String DAYS_PARAM = "cnt";
    private final String APPID_PARAM = "APPID";
    private final String APPID_CODE = "7ea63a81fbe6ad12124c0cf84b4ee1c7";

    private WeatherService() {
    }

    public static WeatherService getInstance() {
        if (instance == null) {
            instance = new WeatherService();
        }
        return instance;
    }

    public void getCurrentWeather(Context context, List<String> location, Response.Listener responseListener, Response.ErrorListener errorListener) throws MalformedURLException {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < location.size(); i++) {
            if (i != location.size() - 1) {
                builder.append(location.get(i)).append(",");
            } else {
                builder.append(location.get(i));
            }
        }
        Uri builtUri = Uri.parse(WEATHER_CURRENT_GROUP).buildUpon()
                .appendQueryParameter(ID_PARAM, builder.toString())
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();


        Log.i(TAG, "getCurrentWeather: " + builtUri.toString());
        URL url = new URL(builtUri.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(),
                new JSONObject(),
                responseListener,
                errorListener);
        objectRequest.setShouldCache(false);
        VolleyRequestMaker.getInstance(context).addToRequestQueue(objectRequest);
    }

    public void getCurrentWeather(Context context, String location, Response.Listener responseListener, Response.ErrorListener errorListener) throws MalformedURLException {
        Uri builtUri = Uri.parse(WEATHER_CURRENT_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();


        Log.i(TAG, "getCurrentWeather: " + builtUri.toString());
        URL url = new URL(builtUri.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(),
                new JSONObject(),
                responseListener,
                errorListener);
        objectRequest.setShouldCache(false);
        VolleyRequestMaker.getInstance(context).addToRequestQueue(objectRequest);
    }


    public void getForecast(Context context, String location, Response.Listener responseListener, Response.ErrorListener errorListener) throws MalformedURLException {
        Uri builtUri = Uri.parse(WEATHER_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();

        URL url = new URL(builtUri.toString());

        Log.i(TAG, "getForecast: " + builtUri.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(),
                new JSONObject(),
                responseListener,
                errorListener);
        objectRequest.setShouldCache(false);
        VolleyRequestMaker.getInstance(context).addToRequestQueue(objectRequest);
    }

    public void getForecast(Context context, String location, int count, Response.Listener responseListener, Response.ErrorListener errorListener) throws MalformedURLException {
        Uri builtUri = Uri.parse(WEATHER_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(DAYS_PARAM, Integer.toString(count))
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();

        URL url = new URL(builtUri.toString());

        Log.i(TAG, "getForecast: " + builtUri.toString());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(),
                new JSONObject(),
                responseListener,
                errorListener);
        objectRequest.setShouldCache(false);
        VolleyRequestMaker.getInstance(context).addToRequestQueue(objectRequest);
    }

    public void getDailyForecast(Context context, String location, int count, Response.Listener responseListener, Response.ErrorListener errorListener) throws MalformedURLException {
        Uri builtUri = Uri.parse(WEATHER_DAILY_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(DAYS_PARAM, Integer.toString(count))
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();

        Log.i(TAG, "getDailyForecast: " + builtUri.toString());
        URL url = new URL(builtUri.toString());

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET,
                url.toString(),
                new JSONObject(),
                responseListener,
                errorListener);
        objectRequest.setShouldCache(false);
        VolleyRequestMaker.getInstance(context).addToRequestQueue(objectRequest);
    }

    /*MAKE REQUEST ONLY IN ASYNC TASK*/
    public JSONObject getForecast(String location, int count) throws MalformedURLException, JSONException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString;

        Uri builtUri = Uri.parse(WEATHER_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(DAYS_PARAM, Integer.toString(count))
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();

        URL url = new URL(builtUri.toString());

        Log.i(TAG, "getForecast: " + builtUri.toString());


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return new JSONObject(jsonString);
    }

    /*MAKE REQUEST ONLY IN ASYNC TASK*/
    public JSONObject getDailyForecast(String location, int count) throws JSONException, MalformedURLException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonString;

        Uri builtUri = Uri.parse(WEATHER_DAILY_FORECAST_BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, location)
                .appendQueryParameter(FORMAT_PARAM, "json")
                .appendQueryParameter(DAYS_PARAM, Integer.toString(count))
                .appendQueryParameter(APPID_PARAM, APPID_CODE)
                .appendQueryParameter(LANG_PARAM, Locale.getDefault().getLanguage())
                .build();

        Log.i(TAG, "getDailyForecast: " + builtUri.toString());
        URL url = new URL(builtUri.toString());

        Log.i(TAG, "getForecast: " + builtUri.toString());


        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonString = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return new JSONObject(jsonString);
    }


}
