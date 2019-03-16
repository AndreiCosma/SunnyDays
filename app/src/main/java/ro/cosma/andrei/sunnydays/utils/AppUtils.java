package ro.cosma.andrei.sunnydays.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.LocationBean;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.ICON_MAPPING;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;

/*Created by Cosma Andrei
* 9/17/2017*/
public class AppUtils {
    public static final String TAG = AppUtils.class.getSimpleName();
    private static DecimalFormat numberFormat = new DecimalFormat("#");
    private static AppUtils instance;
    private static DateFormat DATE_FORMATTER = new SimpleDateFormat("EEE, MMM d");
    private static DateFormat DATE_FORMATTER_HOUR_ONLY = new SimpleDateFormat("hh aaa");
    private static DateFormat FORECAST_FORMAT = new SimpleDateFormat("MMM d, hh aaa");
    private static String DOT_DATE_FORMAT = "dd.MM.yyyy";
    private static SimpleDateFormat dotDateFormatter = new SimpleDateFormat(DOT_DATE_FORMAT);

    private static float screenScale = -1;


    public static boolean hasConnectionToInternet(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isConnectedOrConnecting());
    }

    public static void mapIcons() {
        /*Clear sky*/
        ICON_MAPPING.put("01d", "r01d");
        ICON_MAPPING.put("01n", "r01n");

		/*Few clouds*/
        ICON_MAPPING.put("02d", "r02d");
        ICON_MAPPING.put("02n", "r02d");

		/*Scattered clouds*/
        ICON_MAPPING.put("03d", "r03d");
        ICON_MAPPING.put("03n", "r03d");

		/*Broken clouds*/
        ICON_MAPPING.put("04d", "r03d");
        ICON_MAPPING.put("04n", "r03d");

		/*Shower Rain*/
        ICON_MAPPING.put("09d", "r09d");
        ICON_MAPPING.put("09n", "r09d");

		/*Rain*/
        ICON_MAPPING.put("10d", "r10d");
        ICON_MAPPING.put("10n", "r10d");

		/*Thunderstorm*/
        ICON_MAPPING.put("11d", "r11d");
        ICON_MAPPING.put("11n", "r11d");

		/*Snow*/
        ICON_MAPPING.put("13d", "r13d");
        ICON_MAPPING.put("13n", "r13d");

		/*Mist*/
        ICON_MAPPING.put("50d", "r50d");
        ICON_MAPPING.put("50n", "r50d");
    }

    public static Uri getImageResourcePath(String name, String packageName) throws MalformedURLException {
        return Uri.parse("android.resource://" + packageName + "/raw/" + AppConstants.ICON_MAPPING.get(name));
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static int getImageResourceId(String stringId) {
        String realResId = AppConstants.ICON_MAPPING.get(stringId);
        switch (realResId) {
            case "r01d":
                return R.drawable.r01d;
            case "r01n":
                return R.drawable.r01n;
            case "r02d":
                return R.drawable.r02d;
            case "r03d":
                return R.drawable.r03d;
            case "r09d":
                return R.drawable.r09d;
            case "r10d":
                return R.drawable.r10d;
            case "r11d":
                return R.drawable.r11d;
            case "r13d":
                return R.drawable.r13d;
            case "r50d":
                return R.drawable.r50d;
        }
        return 0;
    }

    public static int dpToPixel(int dp, Context context) {
        if (screenScale == -1) {
            screenScale = context.getResources().getDisplayMetrics().density;
        }
        return (int) (dp * screenScale + 0.5f);
    }

    public static String dateFormatter(int time) {
        return DATE_FORMATTER.format(time);
    }

    public static String dateFormatterHourOnly(long time) {
        return DATE_FORMATTER_HOUR_ONLY.format(time);
    }

    public static String forecastDateFormatter(long time) {
        return FORECAST_FORMAT.format(time);
    }

    public static String dateFormatter(long time) {
        return DATE_FORMATTER.format(time);
    }

    public static String dateFormatter(Date date) {
        return DATE_FORMATTER.format(date);
    }

    public static String getFormattedTemperatureUnit(String s) {
        switch (s) {
            case TEMPERATURE_UNIT_METRIC:
                return AppConstants.TEMPERATURE_FORMATTED_UNIT_METRIC;
            case TEMPERATURE_UNIT_IMPERIAL:
                return AppConstants.TEMPERATURE_FORMATTER_UNIT_IMPERIAL;
        }
        return null;
    }

    public static int kelvinToCelsius(double value) {
        return (int) (value - 273.15);
    }

    public static int kelvinToFahrenheit(double value) {
        Log.i(TAG, "kelvinToFahrenheit: ");
        return (int) ((1.8 * (value - 273) + 32));
    }

    public static long hPaToMmHG(int hpaValue) {
        return Long.parseLong(numberFormat.format((long) (hpaValue * 0.750061683)));
    }

    public static double metersPerSecondToKmPerHour(double value) {
        return Double.parseDouble(numberFormat.format(value * 3.6));
    }

    public static double metersPerSecondToMilesPerHour(double value) {
        return Double.parseDouble(numberFormat.format(value * 2.24));
    }

    public static String formatDotDate(Date date) {
        if (date != null) {
            return dotDateFormatter.format(date);
        } else {
            return "";
        }
    }

    public static boolean LocationArrayContains(int cityId, List<LocationBean> listToSearchIn) {
        for (LocationBean bean : listToSearchIn) {
            if (cityId == bean.getId()) {
                return true;
            }
        }
        return false;
    }

    public static List<String> toStringList(List<Integer> intList) {
        ArrayList<String> arrayList = new ArrayList<>();
        for (Integer integer : intList) {
            arrayList.add(String.valueOf(integer));
        }
        return arrayList;
    }
}
