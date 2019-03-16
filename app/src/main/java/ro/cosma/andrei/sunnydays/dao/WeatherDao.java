package ro.cosma.andrei.sunnydays.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ro.cosma.andrei.sunnydays.bean.LocationBean;
import ro.cosma.andrei.sunnydays.bean.WeatherBean;

/**
 * Created by Cosma Andrei on 18.10.2017.
 */

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM weather WHERE city LIKE :s AND weather_bean_type = :type")
    List<WeatherBean> getByCity(String s, int type);

    @Insert
    void insert(WeatherBean param);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WeatherBean> been);

    @Query("DELETE FROM weather")
    void deleteAllEntries();

    @Query("DELETE FROM weather WHERE city LIKE :s AND weather_bean_type = :type")
    void deleteByCity(String s, int type);

    @Query("DELETE FROM weather WHERE city LIKE :s")
    void deleteByCity(String s);
}
