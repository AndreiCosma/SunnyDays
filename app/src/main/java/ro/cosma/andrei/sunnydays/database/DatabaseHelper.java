package ro.cosma.andrei.sunnydays.database;

import android.arch.persistence.room.Room;
import android.content.Context;

/*Created by Cosma Andrei
* 9/26/2017*/
public class DatabaseHelper {

    private static DatabaseHelper instance;
    private static WeatherDatabase database;

    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public WeatherDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context,
                    WeatherDatabase.class, "WeatherDatabase").fallbackToDestructiveMigration().build();
        }
        return database;
    }

}
