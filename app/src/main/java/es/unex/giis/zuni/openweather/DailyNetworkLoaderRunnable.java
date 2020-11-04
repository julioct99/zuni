package es.unex.giis.zuni.openweather;

import java.io.IOException;

import es.unex.giis.zuni.api.daily.MainDaily;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DailyNetworkLoaderRunnable implements Runnable {

    private final OnDailyLoadedListener mOnDailyLoadedListener;

    private double longt, latt;
    private String city, country;
    public DailyNetworkLoaderRunnable(OnDailyLoadedListener onDailyLoadedListener, double latt, double longt){
        mOnDailyLoadedListener= onDailyLoadedListener;
        this.latt=latt;
        this.longt=longt;
        city=null;
        country=null;
    }

    public DailyNetworkLoaderRunnable(OnDailyLoadedListener onDailyLoadedListener,String city, String country){
        mOnDailyLoadedListener= onDailyLoadedListener;

        this.city=city;
        this.country=country;
    }

    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weatherbit.io/v2.0/forecast/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        try {

            if(city!=null){
                if(!city.equals("")){
                    MainDaily mainDaily = service.listDailyCity(city,country,"5a03135617514e24ad8e588aca207439").execute().body();
                    if(mainDaily!=null) {
                        if(mainDaily.getData()!=null) {
                            AppExecutors.getInstance().mainThread().execute(() -> mOnDailyLoadedListener.onDailyLoaded(mainDaily.getData()));
                        }
                    }
                }
            }
            else{
                MainDaily mainDaily = service.listDaily(Double.toString(latt),Double.toString(longt),"5a03135617514e24ad8e588aca207439").execute().body();
                if(mainDaily!=null) {
                    if(mainDaily.getData()!=null) {
                        AppExecutors.getInstance().mainThread().execute(() -> mOnDailyLoadedListener.onDailyLoaded(mainDaily.getData()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
