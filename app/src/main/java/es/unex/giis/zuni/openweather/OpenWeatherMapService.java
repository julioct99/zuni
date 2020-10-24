package es.unex.giis.zuni.openweather;

import es.unex.giis.zuni.current.Current;
import es.unex.giis.zuni.daily.MainDaily;
import es.unex.giis.zuni.porhoras.MeteoHora;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// https://api.openweathermap.org/data/2.5/weather?lat=" + latt + "&lon=" + longt + "&appid=55ab2d28aad932680b93bf96e8e44f6e
// https://api.openweathermap.org/data/2.5/onecall?lat=38.59758&lon=-5.43701&exclude=current,minutely,daily,alerts&appid=55ab2d28aad932680b93bf96e8e44f6e
public interface OpenWeatherMapService {
    @GET("onecall?lat=38.589345&lon=-5.445131&exclude=minutely,daily,alerts,current&appid=229adb0389614e769d1c119b0bb34bd2")
    Call<MeteoHora> listHoras(@Query("latt") String latt, @Query("longt") String longt, @Query("exclude") String s[], @Query("appid") String appid);

    @GET("weather?lat=38.589345&lon=-5.445131&appid=55ab2d28aad932680b93bf96e8e44f6e")
    Call<Current> listCurrent(@Query("latt") String latt, @Query("longt") String longt, @Query("appid") String appid);

    @GET("daily?lat=38.5892&lon=-5.44349&key=5a03135617514e24ad8e588aca207439")
    Call<MainDaily> listDaily(@Query("lat") String lat, @Query("lon") String lon, @Query("appid") String appid);

}

// https://api.openweathermap.org/data/2.5/onecall?lat=38.59758&lon=-5.43701&exclude=current,minutely,daily,alerts&appid=55ab2d28aad932680b93bf96e8e44f6e