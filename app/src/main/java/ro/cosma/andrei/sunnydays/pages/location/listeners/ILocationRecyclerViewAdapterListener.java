package ro.cosma.andrei.sunnydays.pages.location.listeners;

import android.content.Context;

import ro.cosma.andrei.sunnydays.bean.LocationBean;

/*Created by Cosma Andrei
* 9/20/2017*/
public interface ILocationRecyclerViewAdapterListener {
	void onNavigateToCurrentWeather(LocationBean bean);
    Context getContext();
    void onDeletePressed(int id, int adapterPosition);
}
