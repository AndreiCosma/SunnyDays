package ro.cosma.andrei.sunnydays.pages.daily.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.R;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.pages.daily.PageDailyForecastFragment;

import static android.R.id.list;


/*Created by Cosma Andrei
* 9/24/2017*/
public class DailyForecastViewPagerAdapter extends FragmentStatePagerAdapter {

    private int count = 2;

    private List<WeatherBean> fourteenDaysForecastList;

    private List<WeatherBean> sevenDaysForecastList = new ArrayList<>();

    private PageDailyForecastFragment sevenDaysForecast;

    private PageDailyForecastFragment fourteenDaysForecast;

    public DailyForecastViewPagerAdapter(FragmentManager fm, List<WeatherBean> list) {
        super(fm);
        fourteenDaysForecastList = list;
        if (list.size() >= 7) {
            sevenDaysForecastList = list.subList(0, 7);
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

    public PageDailyForecastFragment getSevenDaysForecastPage() {
        if (sevenDaysForecast == null) {
            sevenDaysForecast = PageDailyForecastFragment.newInstance(sevenDaysForecastList);
        }
        return sevenDaysForecast;
    }

    public PageDailyForecastFragment getFourteenDaysForecastPage() {
        if (fourteenDaysForecast == null) {
            fourteenDaysForecast = PageDailyForecastFragment.newInstance(fourteenDaysForecastList);
        }
        return fourteenDaysForecast;
    }

    public void setData(List<WeatherBean> data) {
        fourteenDaysForecastList = data;
        if (data.size() >= 7) {
            sevenDaysForecastList = data.subList(0, 7);
        }
        notifyDataSetChanged();
    }
}
