package es.unex.giis.zuni.openweather;

import es.unex.giis.zuni.geocode.GeoCode;
import es.unex.giis.zuni.historical.Historical;
import es.unex.giis.zuni.api.current.Current;
import es.unex.giis.zuni.api.daily.MainDaily;
import es.unex.giis.zuni.api.porhoras.MeteoHora;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("onecall?")
    Call<MeteoHora> listHoras(@Query("lat") String latt, @Query("lon") String longt, @Query("exclude") String s[], @Query("appid") String appid);

    @GET("onecall?")
    Call<MeteoHora> listHorasCity(@Query("q") String city, @Query("exclude") String s[], @Query("appid") String appid);

    @GET("weather?")
    Call<Current> listCurrent(@Query("lat") String latt, @Query("lon") String longt, @Query("appid") String appid);

    @GET("weather?")
    Call<Current> listCurrentCity(@Query("q") String city, @Query("appid") String appid);

    @GET("daily?")
    Call<MainDaily> listDaily(@Query("lat") String lat, @Query("lon") String lon, @Query("key") String key);

    @GET("daily?")
    Call<MainDaily> listDailyCity(@Query("city") String city,@Query("country") String country,  @Query("key") String key);

    @GET("onecall/timemachine?")
    Call<Historical> listHistorical(@Query("lat") String latt, @Query("lon") String longt,@Query("dt") String dt, @Query("appid") String appid);

    @GET("{city}?")
    Call<GeoCode> getGeoCode(@Path("city") String city, @Query("json") int json);
}
