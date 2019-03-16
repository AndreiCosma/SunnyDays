package ro.cosma.andrei.sunnydays.widget;

import android.app.Activity;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.parser.JSonWeatherParser;
import ro.cosma.andrei.sunnydays.service.WeatherService;
import ro.cosma.andrei.sunnydays.utils.AppUtils;
import ro.cosma.andrei.sunnydays.utils.PreferencesUtils;

/**
 * The configuration screen for the {@link CurrentWeatherWidgetProvider CurrentWeatherWidgetProvider} AppWidget.
 */
public class CurrentWeatherWidgetConfigureActivity extends Activity implements View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {

    public static final String TAG = CurrentWeatherWidgetConfigureActivity.class.getSimpleName();
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private TextInputEditText mLocationInputEditText;
    private ProgressDialog pd;

    public CurrentWeatherWidgetConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_current_weather_widget_configure);
        mLocationInputEditText = findViewById(R.id.location);
        findViewById(R.id.submit).setOnClickListener(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (AppUtils.hasConnectionToInternet(this)) {
            pd = ProgressDialog.show(this, "", getString(R.string.getting_location));
            try {
                WeatherService.getInstance().getCurrentWeather(this,
                        mLocationInputEditText.getText().toString(),
                        this,
                        this);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                if (pd != null) {
                    pd.dismiss();
                }

            }
        } else {
            //No internet connection
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (pd != null) {
            pd.dismiss();
        }
        Toast.makeText(this, getString(R.string.error_getting_location), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {
        WeatherBean bean = null;
        try {
            bean = JSonWeatherParser.getInstance().getCurrentWeather(response);
        } catch (JSONException e) {
            if (pd != null) {
                pd.dismiss();
            }
            Log.e(TAG, "onResponse: ", e);
        }
        if (pd != null) {
            pd.dismiss();
        }
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        CurrentWeatherWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId, bean);

        /*Salvam in preferinte id-ul orasului cu id-ul widgetului ca fiind cheia
        * Recuperam in onUpdate orasele.*/
        PreferencesUtils.mapCityIdToWidgetId(this, mAppWidgetId, bean.getCityId());

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

