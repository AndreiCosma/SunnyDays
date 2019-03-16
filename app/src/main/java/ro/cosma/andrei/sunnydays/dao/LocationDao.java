package ro.cosma.andrei.sunnydays.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import ro.cosma.andrei.sunnydays.bean.LocationBean;

/*Created by Cosma Andrei
* 9/26/2017*/
@Dao
public interface LocationDao {
    /*De transmis stringul intre ,,%''  exemplu: %string%*/
    @Query("SELECT * FROM Location WHERE CITY LIKE :s")
    List<LocationBean> getByString(String s);

    @Query("SELECT * FROM Location")
    List<LocationBean> getAll();

    @Query("SELECT * FROM Location WHERE CITY_ID = :id")
    List<LocationBean> get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(LocationBean... users);

    @Delete
    void delete(LocationBean user);

    @Query("DELETE FROM Location")
    void deleteAllEntries();

    @Query("DELETE FROM Location WHERE CITY_ID == :id")
    void deleteById(int id);

    @Query("SELECT CITY_ID FROM Location")
    List<Integer> getIdList();
}
