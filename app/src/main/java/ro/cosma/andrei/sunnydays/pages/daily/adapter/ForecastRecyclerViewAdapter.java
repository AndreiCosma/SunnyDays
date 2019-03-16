package ro.cosma.andrei.sunnydays.pages.daily.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.daily.listener.IForecastRecyclerViewAdapterListener;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.MODE_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_PRESSURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_TEMPERATURE;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PREFERENCES_WIND;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.PRESSURE_MMGH;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_KM_H;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.WIND_FORMATTED_UNIT_MILES_H;

/*Created by Cosma Andrei
* 9/20/2017*/
public class ForecastRecyclerViewAdapter extends RecyclerView.Adapter<ForecastRecyclerViewAdapter.ViewHolder> {

    private List<WeatherBean> list;
    private IForecastRecyclerViewAdapterListener listener;
    private SharedPreferences preferences;
    private String tempUnit = "";
    private String windUnit = "";


    public ForecastRecyclerViewAdapter(IForecastRecyclerViewAdapterListener listener, List<WeatherBean> list, Context context) {
        this.list = list;
        this.listener = listener;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        tempUnit = preferences.getString(PREFERENCES_TEMPERATURE, MODE_METRIC);
        windUnit = preferences.getString(PREFERENCES_WIND, WIND_FORMATTED_UNIT_KM_H);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_forecast, parent, false);
        return new ForecastRecyclerViewAdapter.ViewHolder(listener, itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindBean(list.get(position), tempUnit, windUnit);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<WeatherBean> forecastList) {
        list.clear();
        list.addAll(forecastList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private WeakReference<IForecastRecyclerViewAdapterListener> listener;

        private WeatherBean bean;

        private TextView nameTv;

        private TextView temperatureMaxTv;

        private TextView temperatureMinTv;

        private ImageView iconIv;

        private TextView windSpeedValueTv;
        private TextView getWindSpeedUnitTv;

        private TextView humidityTv;

        public ViewHolder(IForecastRecyclerViewAdapterListener listener, View itemView) {
            super(itemView);

            this.listener = new WeakReference<IForecastRecyclerViewAdapterListener>(listener);

            itemView.setOnClickListener(this);

            nameTv = (TextView) itemView.findViewById(R.id.name);

            temperatureMaxTv = (TextView) itemView.findViewById(R.id.temperature);

            temperatureMinTv = (TextView) itemView.findViewById(R.id.temperature_min);

            iconIv = (ImageView) itemView.findViewById(R.id.icon);

            windSpeedValueTv = (TextView) itemView.findViewById(R.id.wind_speed_value);
            getWindSpeedUnitTv = (TextView) itemView.findViewById(R.id.wind_speed_unit);

            humidityTv = (TextView) itemView.findViewById(R.id.precipitation_value);
        }

        @Override
        public void onClick(View view) {
            listener.get().onNavigateToDetails(bean);
        }

        public void bindBean(WeatherBean bean, String tempUnit, String windUnit) {
            this.bean = bean;

            int formattedMaxTemp = 0;
            int formattedMinTemp = 0;

            if (TEMPERATURE_UNIT_METRIC.equals(tempUnit)) {
                formattedMaxTemp = AppUtils.kelvinToCelsius(bean.getMaxTemp());
                formattedMinTemp = AppUtils.kelvinToCelsius(bean.getMinTemp());
            } else if (TEMPERATURE_UNIT_IMPERIAL.equals(tempUnit)) {
                formattedMaxTemp = AppUtils.kelvinToFahrenheit(bean.getMaxTemp());
                formattedMinTemp = AppUtils.kelvinToFahrenheit(bean.getMinTemp());
            }

            temperatureMaxTv.setText(String.valueOf(formattedMaxTemp).concat("°"));
            temperatureMinTv.setText(String.valueOf(formattedMinTemp).concat("°"));

            double formattedWindSpeed = bean.getWindSpeed();

            if (WIND_FORMATTED_UNIT_MILES_H.equals(windUnit)) {
                formattedWindSpeed = AppUtils.metersPerSecondToMilesPerHour(bean.getWindSpeed());
            } else if (WIND_FORMATTED_UNIT_KM_H.equals(windUnit)) {
                formattedWindSpeed = AppUtils.metersPerSecondToKmPerHour(bean.getWindSpeed());
            }

            windSpeedValueTv.setText(String.valueOf(formattedWindSpeed));
            getWindSpeedUnitTv.setText(windUnit);


            nameTv.setText(bean.getHumanReadableDate());
            iconIv.setImageResource(bean.getIconId());

            humidityTv.setText(String.valueOf(bean.getHumidity()));
        }
    }
}
