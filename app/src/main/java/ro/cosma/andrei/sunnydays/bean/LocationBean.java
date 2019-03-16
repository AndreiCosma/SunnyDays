package ro.cosma.andrei.sunnydays.bean;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/*Created by Cosma Andrei
* 9/26/2017*/
@Entity(tableName = "Location")
public class LocationBean implements Serializable {

    @ColumnInfo(name = "CITY_ID")
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "COUNTRY")
    private String country;

    @ColumnInfo(name = "CITY")
    private String city;

    @ColumnInfo(name = "TEMPERATURE")
    private double temperature;

    @ColumnInfo(name = "ICON_ID")
    private int iconId;


    @ColumnInfo(name = "LON")
    private double lon;


    @ColumnInfo(name = "LAT")
    private double lat;

    @Ignore
    public LocationBean() {
    }

    public LocationBean(int id, String country, String city, double temperature, int iconId, double lon, double lat) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.temperature = temperature;
        this.iconId = iconId;
        this.lon = lon;
        this.lat = lat;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "LocationBean{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", temperature=" + temperature +
                ", iconId=" + iconId +
                ", lon=" + lon +
                ", lat=" + lat +
                '}';
    }
}
