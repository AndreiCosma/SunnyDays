package ro.cosma.andrei.sunnydays.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;
import ro.cosma.andrei.sunnydays.dao.LocationDao;
import ro.cosma.andrei.sunnydays.dao.WeatherDao;

/*Created by Cosma Andrei
* 9/26/2017*/
@Database(entities = {LocationBean.class, WeatherBean.class}, version = 5, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract LocationDao locationDao();

    public abstract WeatherDao weatherDao();
}
