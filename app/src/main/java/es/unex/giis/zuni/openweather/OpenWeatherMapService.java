package es.unex.giis.zuni.openweather;

import es.unex.giis.zuni.MeteoHora;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// https://api.openweathermap.org/data/2.5/weather?lat=" + latt + "&lon=" + longt + "&appid=55ab2d28aad932680b93bf96e8e44f6e
// https://api.openweathermap.org/data/2.5/onecall?lat=38.59758&lon=-5.43701&exclude=current,minutely,daily,alerts&appid=55ab2d28aad932680b93bf96e8e44f6e
public interface OpenWeatherMapService {
    @GET("onecall?lat=38.59758&lon=-5.43701&exclude=current,minutely,daily,alerts&appid=55ab2d28aad932680b93bf96e8e44f6e")
    Call<MeteoHora> listHoras(@Query("latt") String latt, @Query("longt") String longt, @Query("exclude") String s[],@Query("appid") String appid);
}
/*
    @GET("/onecall")
    void getWeatherInfo (@Query("lat") String latitude,
                         @Query("lon") String longitude,
                         @Query("exclude") String cnt[],
                         @Query("appid") String appid,
                         Call<MeteoHora> listHoras;
}
*/

// https://api.openweathermap.org/data/2.5/onecall?lat=38.59758&lon=-5.43701&exclude=current,minutely,daily,alerts&appid=55ab2d28aad932680b93bf96e8e44f6e