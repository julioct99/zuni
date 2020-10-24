package es.unex.giis.zuni.openweather;

import es.unex.giis.zuni.current.Current;
import es.unex.giis.zuni.daily.MainDaily;
import es.unex.giis.zuni.porhoras.MeteoHora;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeatherMapService {
    @GET("onecall?")
    Call<MeteoHora> listHoras(@Query("lat") String latt, @Query("lon") String longt, @Query("exclude") String s[], @Query("appid") String appid);

    @GET("weather?")
    Call<Current> listCurrent(@Query("lat") String latt, @Query("lon") String longt, @Query("appid") String appid);

    @GET("daily?")
    Call<MainDaily> listDaily(@Query("lat") String lat, @Query("lon") String lon, @Query("key") String key);

    @GET("daily?")
    Call<MainDaily> listDailyCity(@Query("city") String city, @Query("key") String key);
}
