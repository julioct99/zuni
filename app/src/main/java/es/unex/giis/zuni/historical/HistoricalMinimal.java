package es.unex.giis.zuni.historical;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "historicals")
public class HistoricalMinimal implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "cityname")
    private String cityname;
    @ColumnInfo(name = "countrycode")
    private String countrycode;

    @ColumnInfo(name = "lat")
    private Double lat;
    @ColumnInfo(name = "lon")
    private Double lon;

    @ColumnInfo(name = "Dt")
    private Integer Dt;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "sunrise")
    private Integer sunrise;
    @ColumnInfo(name = "sunset")
    private Integer sunset;
    @ColumnInfo(name = "tmpMax")
    private Double tmpMax;
    @ColumnInfo(name = "tmpMin")
    private Double tmpMin;
    @ColumnInfo(name = "windSpeed")
    private Double windSpeed;
    @ColumnInfo(name = "humidity")
    private Integer humidity;
    @ColumnInfo(name = "main")
    private String main;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }


    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }


    public Integer getDt() {
        return Dt;
    }

    public void setDt(Integer dt) {
        Dt = dt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSunrise() {
        return sunrise;
    }

    public void setSunrise(Integer sunrise) {
        this.sunrise = sunrise;
    }

    public Integer getSunset() {
        return sunset;
    }

    public void setSunset(Integer sunset) {
        this.sunset = sunset;
    }

    public Double getTmpMax() {
        return tmpMax;
    }

    public void setTmpMax(Double tmpMax) {
        this.tmpMax = tmpMax;
    }

    public Double getTmpMin() {
        return tmpMin;
    }

    public void setTmpMin(Double tmpMin) {
        this.tmpMin = tmpMin;
    }

    public Double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    @Ignore
    public void initFromHistorical(Historical h, String cityname, String countrycode){
        this.setCityname(cityname);
        this.setCountrycode(countrycode);
        this.initFromHistorical(h);
    }


    @Ignore
    public void initFromHistorical(Historical h){
        this.setLat(h.getLat());
        this.setLon(h.getLon());

        this.setDt(h.getCurrent().getDt());
        this.setDescription(h.getCurrent().getWeather().get(0).getDescription());
        this.setMain(h.getCurrent().getWeather().get(0).getMain());
        this.setSunrise(h.getCurrent().getSunrise());
        this.setSunset(h.getCurrent().getSunset());
        this.setWindSpeed(h.getCurrent().getWindSpeed());
        this.setHumidity(h.getCurrent().getHumidity());

        //Calculate max and min Temperature
        Double auxMax = null;
        Double auxMin = null;

        for(Hourly i : h.getHourly()){
            if (auxMax == null)
                auxMax = i.getTemp();
            else if (auxMax < i.getTemp())
                auxMax = i.getTemp();

            if (auxMin == null)
                auxMin = i.getTemp();
            else if (auxMin > i.getTemp())
                auxMin = i.getTemp();
        }

        this.setTmpMax(auxMax);
        this.setTmpMin(auxMin);
    }
    @Ignore
    public Historical convertIntoHistorical(){
        Historical h = new Historical();

        Weather w = new Weather();
        Current c = new Current();

        h.setLat(this.getLat());
        h.setLon(this.getLon());

        c.setDt(this.getDt());

        w.setDescription(this.getDescription());
        w.setMain(this.getMain());

        ArrayList<Weather> wl = new ArrayList<Weather>();//Add weather after add everything in
        wl.add(w);
        c.setWeather(wl);

        c.setSunrise(this.getSunrise());
        c.setSunset(this.getSunset());
        c.setWindSpeed(this.getWindSpeed());
        c.setHumidity(this.getHumidity());

        ArrayList<Hourly> tl = new ArrayList<Hourly>();
        Hourly t1 = new Hourly();
        t1.setTemp(this.getTmpMax());
        tl.add(t1);
        Hourly t2 = new Hourly();
        t2.setTemp(this.getTmpMin());
        tl.add(t2);


        h.setHourly(tl); //Add the Hourly after add everything in
        h.setCurrent(c);//Add current after add everything in

        return h;
    }

}
