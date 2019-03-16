package ro.cosma.andrei.sunnydays.pages.hourly.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.daily.PageDailyForecastFragment;
import ro.cosma.andrei.sunnydays.pages.hourly.PageHourlyForecastFragment;


/*Created by Cosma Andrei
* 9/24/2017*/
public class HourlyForecastViewPagerAdapter extends FragmentStatePagerAdapter {

    private int count = 2;

    private List<WeatherBean> fourteenDaysForecastList;

    private List<WeatherBean> sevenDaysForecastList = new ArrayList<>();

    private PageHourlyForecastFragment sevenDaysForecast;

    private PageHourlyForecastFragment fourteenDaysForecast;

    public HourlyForecastViewPagerAdapter(FragmentManager fm, List<WeatherBean> list) {
        super(fm);
        fourteenDaysForecastList = list;
        if (list.size() >= 24) {
            sevenDaysForecastList = list.subList(0, 24);
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return getSevenDaysForecastPage();
            case 1:
                return getFourteenDaysForecastPage();
        }
        return null;
    }


    @Override
    public int getCount() {
        return count;
    }

    public PageHourlyForecastFragment getSevenDaysForecastPage() {
        if (sevenDaysForecast == null) {
            sevenDaysForecast = PageHourlyForecastFragment.newInstance(sevenDaysForecastList);
        }
        return sevenDaysForecast;
    }

    public PageHourlyForecastFragment getFourteenDaysForecastPage() {
        if (fourteenDaysForecast == null) {
            fourteenDaysForecast = PageHourlyForecastFragment.newInstance(fourteenDaysForecastList);
        }
        return fourteenDaysForecast;
    }

    public void setData(List<WeatherBean> data) {
        fourteenDaysForecastList = data;
        if (data.size() >= 7) {
            sevenDaysForecastList = data.subList(0, 24);
        }
        notifyDataSetChanged();
    }
}
