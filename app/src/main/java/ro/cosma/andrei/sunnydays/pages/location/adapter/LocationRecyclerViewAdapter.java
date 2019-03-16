package ro.cosma.andrei.sunnydays.pages.location.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.pages.location.listeners.ILocationRecyclerViewAdapterListener;
import ro.cosma.andrei.sunnydays.utils.AppUtils;

import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_IMPERIAL;
import static ro.cosma.andrei.sunnydays.utils.AppConstants.TEMPERATURE_UNIT_METRIC;

/*Created by Cosma Andrei
* 9/26/2017*/
public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder> {
    private String currentLocation;
    private List<LocationBean> list;
    private ILocationRecyclerViewAdapterListener listener;
    private String tempUnit = "";

    public LocationRecyclerViewAdapter(ILocationRecyclerViewAdapterListener listener, String tempUnit, List<LocationBean> list, String currentLocation) {
        this.listener = listener;
        this.list = list;
        this.tempUnit = tempUnit;
        this.currentLocation = currentLocation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(listener, LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindBean(list.get(position), tempUnit, currentLocation);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setNewDataset(List<LocationBean> searchedData) {
        list = searchedData;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private LocationBean bean;
        private WeakReference<ILocationRecyclerViewAdapterListener> listener;
        private TextView countryTV;
        private TextView cityTv;
        private TextView tempTv;
        private ImageView iconIv;
        private TextView deleteTv;
        private View dividerView;

        public ViewHolder(ILocationRecyclerViewAdapterListener listener, View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.listener = new WeakReference<ILocationRecyclerViewAdapterListener>(listener);
            countryTV = (TextView) itemView.findViewById(R.id.country);
            cityTv = (TextView) itemView.findViewById(R.id.city);
            tempTv = (TextView) itemView.findViewById(R.id.temp);
            iconIv = (ImageView) itemView.findViewById(R.id.icon);
            deleteTv = (TextView) itemView.findViewById(R.id.delete);
            deleteTv.setOnClickListener(this);
            dividerView = itemView.findViewById(R.id.divider);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.delete) {
                listener.get().onDeletePressed(bean.getId(), getAdapterPosition());
            } else {
                listener.get().onNavigateToCurrentWeather(bean);
            }
        }

        private void bindBean(LocationBean bean, String tempUnit, String currentLocation) {
            this.bean = bean;
            countryTV.setText(bean.getCountry());
            cityTv.setText(bean.getCity());

            int formattedTemp = 0;

            if (bean.getCity().equalsIgnoreCase(currentLocation)) {
                deleteTv.setEnabled(false);
                deleteTv.setTextColor(listener.get().getContext().getColor(R.color.flat_button_color_disabled));
            } else {
                deleteTv.setEnabled(true);
                deleteTv.setTextColor(listener.get().getContext().getColor(R.color.flat_button_color_enabled));
            }

            if (TEMPERATURE_UNIT_METRIC.equals(tempUnit)) {
                formattedTemp = AppUtils.kelvinToCelsius(bean.getTemperature());
            } else if (TEMPERATURE_UNIT_IMPERIAL.equals(tempUnit)) {

                formattedTemp = AppUtils.kelvinToFahrenheit(bean.getTemperature());
            }
            tempTv.setText(String.valueOf(formattedTemp).concat(AppUtils.getFormattedTemperatureUnit(tempUnit)));
            iconIv.setImageResource(bean.getIconId());
        }

    }
}
