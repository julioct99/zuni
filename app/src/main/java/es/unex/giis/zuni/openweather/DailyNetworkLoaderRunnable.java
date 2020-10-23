package es.unex.giis.zuni.openweather;

import java.io.IOException;

import es.unex.giis.zuni.daily.MainDaily;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DailyNetworkLoaderRunnable implements Runnable {

    private final OnDailyLoadedListener mOnDailyLoadedListener;
    private double longt, latt;
    public DailyNetworkLoaderRunnable(OnDailyLoadedListener onDailyLoadedListener, double latt, double longt){
        mOnDailyLoadedListener= onDailyLoadedListener;
        this.latt=latt;
        this.longt=longt;
    }
    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        try {
            String exclude[] = {"current","minutely","hourly","alerts"};
            MainDaily mainDaily = service.listDaily(Double.toString(latt),Double.toString(longt),exclude,"55ab2d28aad932680b93bf96e8e44f6e").execute().body();
            if(mainDaily!=null) {
                if(mainDaily.getDaily()!=null) {
                    AppExecutors.getInstance().mainThread().execute(() -> mOnDailyLoadedListener.onDailyLoaded(mainDaily.getDaily()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
