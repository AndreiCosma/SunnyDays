package ro.cosma.andrei.sunnydays.bean;

//import android.arch.persistence.room.ColumnInfo;
//import android.arch.persistence.room.Entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

/*Created by Cosma Andrei
* 9/17/2017*/
@Entity(tableName = "weather")
public class WeatherBean implements Serializable {
    @Ignore
    public static final int TYPE_CURRENT = 0;
    @Ignore
    public static final int TYPE_FORECAST = 1;
    @Ignore
    public static final int TYPE_DAILY_FORECAST = 2;

    @PrimaryKey(autoGenerate = true)
    private int databaseId;

    @Ignore
    private Date timeNew;
    @Ignore
    private Date sunrise;
    @Ignore
    private Date sunset;

    @ColumnInfo(name = "weather_bean_type")
    private int beanType;

    /*Weather condition id*/
    @ColumnInfo(name = "condition_id")
    private int id = 0;

    @ColumnInfo(name = "sunrise_time")
    private int sunriseTime;

    @ColumnInfo(name = "sunset_time")
    private int sunsetTime;

    @ColumnInfo(name = "human_readable_sunrise_time")
    private String humanReadableSunriseTime;

    @ColumnInfo(name = "human_readable_sunset_time")
    private String humanReadableSunsetTime;

    /*Group of weather parameters (Rain, Snow, Extreme etc.)*/
    @ColumnInfo(name = "main")
    private String main = "";

    /*Weather condition within the group*/
    @ColumnInfo(name = "description")
    private String description = "";

    /*Weather condition within the group*/
    @ColumnInfo(name = "icon_id")
    private int iconId = 0;

    /*Atmospheric pressure (on the sea level, if there is no sea_level or grnd_level data), hPa*/
    @ColumnInfo(name = "pressure")
    private float pressure = 0;

    /*City geo location, longitude*/
    @ColumnInfo(name = "longitude")
    private float lon;

    /*City geo location, latitude*/
    @ColumnInfo(name = "latitude")
    private float lat;

    /* Humidity, %*/
    @ColumnInfo(name = "humidity")
    private int humidity = 0;

    /*Temperature. Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit. */
    @ColumnInfo(name = "temperature")
    private double temperature = 0;

    @ColumnInfo(name = "night_temperature")
    private double nightTemperature = 0;

    @ColumnInfo(name = "evening_temperature")
    private double eveningTemperature = 0;

    @ColumnInfo(name = "morning_temperature")
    private double morningTemperature = 0;

    /* Minimum temperature at the moment. This is deviation from current temp
    that is possible for large cities and megalopolises geographically expanded
    (use these parameter optionally). Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.*/
    @ColumnInfo(name = "min_temp")
    private double minTemp = 0;

    /*Maximum temperature at the moment. This is deviation from current temp that is
    possible for large cities and megalopolises geographically expanded (use these parameter optionally).
    Unit Default: Kelvin, Metric: Celsius, Imperial: Fahrenheit.*/
    @ColumnInfo(name = "max_temp")
    private double maxTemp = 0;

    /*Atmospheric pressure on the sea level, hPa*/
    @ColumnInfo(name = "sea_level_pressure")
    private float seaLevelPressure = 0;

    /*Atmospheric pressure on the ground level, hPa*/
    @ColumnInfo(name = "ground_level_pressure")
    private float groundLvlPressure = 0;

    /*Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.*/
    @ColumnInfo(name = "wind_speed")
    private double windSpeed = 0;

    /*Wind direction, degrees (meteorological)*/
    @ColumnInfo(name = "wind_direction")
    private int windDirection = 0;

    /*Cloudiness, %*/
    @ColumnInfo(name = "cloudiness")
    private float cloudiness = 0;

    /*Rain volume for the last 3 hours*/
    @ColumnInfo(name = "rain_volume")
    private double rainVolume = 0;

    /*Snow volume for the last 3 hours*/
    @ColumnInfo(name = "snow_volume")
    private float snowVolume = 0;

    /* Time of data calculation, unix, UTC */
    @ColumnInfo(name = "time")
    private long time = 0;

    @ColumnInfo(name = "country")
    private String country = "";

    @ColumnInfo(name = "city_id")
    private int cityId = 0;

    @ColumnInfo(name = "city")
    private String city = "";

    @ColumnInfo(name = "human_readable_date")
    private String humanReadableDate;

    @ColumnInfo(name = "rain")
    private boolean rain;

    @ColumnInfo(name = "snow")
    private boolean snow;

    @ColumnInfo(name = "sea")
    private boolean sea;

    @ColumnInfo(name = "ground")
    private boolean ground;

    @Ignore
    public WeatherBean() {
    }

    @Ignore
    public WeatherBean(WeatherBean bean) {
        this.id = bean.getId();
        this.main = bean.getMain();
        this.description = bean.getDescription();
        this.iconId = bean.getIconId();
        this.temperature = bean.getTemperature();
        this.nightTemperature = bean.getNightTemperature();
        this.eveningTemperature = bean.getEveningTemperature();
        this.morningTemperature = bean.getMorningTemperature();
        this.pressure = bean.getPressure();
        this.lon = bean.getLon();
        this.lat = bean.getLat();
        this.humidity = bean.getHumidity();
        this.minTemp = bean.getMinTemp();
        this.maxTemp = bean.getMaxTemp();
        this.seaLevelPressure = bean.getSeaLevelPressure();
        this.groundLvlPressure = bean.getGroundLvlPressure();
        this.windSpeed = bean.getWindSpeed();
        this.windDirection = bean.getWindDirection();
        this.cloudiness = bean.getCloudiness();
        this.rainVolume = bean.getRainVolume();
        this.snowVolume = bean.getSnowVolume();
        this.time = bean.getTime();
        this.country = bean.getCountry();
        this.city = bean.getCity();
        this.rain = bean.hasRainVolume();
        this.snow = bean.hasSnowVolume();
        this.sea = bean.hasSeaLevelPressure();
        this.ground = bean.hasGroundPressureLevel();
        this.humanReadableDate = bean.getHumanReadableDate();
    }

    public WeatherBean(int beanType, int id, int sunriseTime, int sunsetTime, String humanReadableSunriseTime, String humanReadableSunsetTime, String main, String description, int iconId, float pressure, float lon, float lat, int humidity, double temperature, double nightTemperature, double eveningTemperature, double morningTemperature, double minTemp, double maxTemp, float seaLevelPressure, float groundLvlPressure, double windSpeed, int windDirection, float cloudiness, double rainVolume, float snowVolume, long time, String country, int cityId, String city, String humanReadableDate, boolean rain, boolean snow, boolean sea, boolean ground) {
        this.beanType = beanType;
        this.id = id;
        this.sunriseTime = sunriseTime;
        this.sunsetTime = sunsetTime;
        this.humanReadableSunriseTime = humanReadableSunriseTime;
        this.humanReadableSunsetTime = humanReadableSunsetTime;
        this.main = main;
        this.description = description;
        this.iconId = iconId;
        this.pressure = pressure;
        this.lon = lon;
        this.lat = lat;
        this.humidity = humidity;
        this.temperature = temperature;
        this.nightTemperature = nightTemperature;
        this.eveningTemperature = eveningTemperature;
        this.morningTemperature = morningTemperature;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.seaLevelPressure = seaLevelPressure;
        this.groundLvlPressure = groundLvlPressure;
        this.windSpeed = windSpeed;
        this.windDirection = windDirection;
        this.cloudiness = cloudiness;
        this.rainVolume = rainVolume;
        this.snowVolume = snowVolume;
        this.time = time;
        this.country = country;
        this.cityId = cityId;
        this.city = city;
        this.humanReadableDate = humanReadableDate;
        this.rain = rain;
        this.snow = snow;
        this.sea = sea;
        this.ground = ground;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public float getPressure() {
        return pressure;
    }

    public void setPressure(float pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public float getSeaLevelPressure() {
        return seaLevelPressure;
    }

    public void setSeaLevelPressure(float seaLevelPressure) {
        this.seaLevelPressure = seaLevelPressure;
    }

    public float getGroundLvlPressure() {
        return groundLvlPressure;
    }

    public void setGroundLvlPressure(float groundLvlPressure) {
        this.groundLvlPressure = groundLvlPressure;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    public float getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(float cloudiness) {
        this.cloudiness = cloudiness;
    }

    public double getRainVolume() {
        return rainVolume;
    }

    public void setRainVolume(double rainVolume) {
        this.rainVolume = rainVolume;
    }

    public float getSnowVolume() {
        return snowVolume;
    }

    public void setSnowVolume(float snowVolume) {
        this.snowVolume = snowVolume;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
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

    public boolean hasRainVolume() {
        return rain;
    }

    public void setHasRainVolume(boolean rain) {
        this.rain = rain;
    }

    public boolean hasSnowVolume() {
        return snow;
    }

    public void setHasSnowVolume(boolean snow) {
        this.snow = snow;
    }

    public boolean hasSeaLevelPressure() {
        return sea;
    }

    public void setHasSeaLevelPressure(boolean sea) {
        this.sea = sea;
    }

    public boolean hasGroundPressureLevel() {
        return ground;
    }

    public void setHasGroundPressureLevel(boolean ground) {
        this.ground = ground;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getNightTemperature() {
        return nightTemperature;
    }

    public void setNightTemperature(double nightTemperature) {
        this.nightTemperature = nightTemperature;
    }

    public double getEveningTemperature() {
        return eveningTemperature;
    }

    public void setEveningTemperature(double eveningTemperature) {
        this.eveningTemperature = eveningTemperature;
    }

    public double getMorningTemperature() {
        return morningTemperature;
    }

    public void setMorningTemperature(double morningTemperature) {
        this.morningTemperature = morningTemperature;
    }

    public String getHumanReadableDate() {
        return humanReadableDate;
    }

    public void setHumanReadableDate(String humanReadableDate) {
        this.humanReadableDate = humanReadableDate;
    }

    public int getSunriseTime() {
        return sunriseTime;
    }

    public void setSunriseTime(int sunriseTime) {
        this.sunriseTime = sunriseTime;
    }

    public int getSunsetTime() {
        return sunsetTime;
    }

    public void setSunsetTime(int sunsetTime) {
        this.sunsetTime = sunsetTime;
    }

    public String getHumanReadableSunriseTime() {
        return humanReadableSunriseTime;
    }

    public void setHumanReadableSunriseTime(String humanReadableSunriseTime) {
        this.humanReadableSunriseTime = humanReadableSunriseTime;
    }

    public String getHumanReadableSunsetTime() {
        return humanReadableSunsetTime;
    }

    public void setHumanReadableSunsetTime(String humanReadableSunsetTime) {
        this.humanReadableSunsetTime = humanReadableSunsetTime;
    }

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public Date getTimeNew() {
        return timeNew;
    }

    public void setTimeNew(Date timeNew) {
        this.timeNew = timeNew;
    }

    public Date getSunrise() {
        return sunrise;
    }

    public void setSunrise(Date sunrise) {
        this.sunrise = sunrise;
    }

    public Date getSunset() {
        return sunset;
    }

    public void setSunset(Date sunset) {
        this.sunset = sunset;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public boolean isRain() {
        return rain;
    }

    public void setRain(boolean rain) {
        this.rain = rain;
    }

    public boolean isSnow() {
        return snow;
    }

    public void setSnow(boolean snow) {
        this.snow = snow;
    }

    public boolean isSea() {
        return sea;
    }

    public void setSea(boolean sea) {
        this.sea = sea;
    }

    public boolean isGround() {
        return ground;
    }

    public void setGround(boolean ground) {
        this.ground = ground;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }
}
