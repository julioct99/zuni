package es.unex.giis.zuni.openweather;

import android.util.Log;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import es.unex.giis.zuni.historical.Historical;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HistoricalNetworkLoaderRunnable implements Runnable {

    private final OnHistoricalLoadedListener mOnHistoricalLoadedListener;

    private double longt, latt;
    private long dt;
    public HistoricalNetworkLoaderRunnable(OnHistoricalLoadedListener onHistoricalLoadedListener, double latt, double longt, long dt){
        mOnHistoricalLoadedListener= onHistoricalLoadedListener;
        this.latt=latt;
        this.longt=longt;
        this.dt=dt;
    }


    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        try {
            Log.e("Historico API","Se ha creado el builder de Retrofit");

            Long timeS = (System.currentTimeMillis()) / 1000 - 86400;
            Historical historical = service.listHistorical(Double.toString(39.47649),Double.toString(-6.37224),Long.toString(timeS),"55ab2d28aad932680b93bf96e8e44f6e").execute().body();
            Log.e("Historico API","Se ha cargado el histrico ");

            if(historical!=null) {
                Log.e("Historico API","5");
                if(historical.getHourly()!=null) {
                    Log.e("Historico API","2");
                    Log.e("Historico API",historical.getHourly().get(0).getWeather().get(0).getMain());
                    ArrayList<Historical> historicalList = new ArrayList<Historical>();
                    historicalList.add(historical);
                    AppExecutors.getInstance().mainThread().execute(() -> mOnHistoricalLoadedListener.onHistoricalLoaded(historicalList));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
