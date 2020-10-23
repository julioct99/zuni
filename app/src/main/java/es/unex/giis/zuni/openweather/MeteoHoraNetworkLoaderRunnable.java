package es.unex.giis.zuni.openweather;

        import android.util.Log;

        import java.io.IOException;
import java.util.List;

import es.unex.giis.zuni.porhoras.Hourly;
import es.unex.giis.zuni.porhoras.MeteoHora;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MeteoHoraNetworkLoaderRunnable implements Runnable {

    private final OnMeteoHorasLoadedListener mOnMeteoHorasLoadedListener;
    private double longt, latt;
    public MeteoHoraNetworkLoaderRunnable(OnMeteoHorasLoadedListener onMeteoHorasLoadedListener, double latt, double longt){
        mOnMeteoHorasLoadedListener= onMeteoHorasLoadedListener;
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
            String exclude[] = {"current","minutely","daily","alerts"};

            List<Hourly> listHourly = null;

            MeteoHora listHoras = service.listHoras(Double.toString(latt),Double.toString(longt),exclude,"55ab2d28aad932680b93bf96e8e44f6e").execute().body();



            if(listHoras!=null) {
                listHourly = listHoras.getHourly();
                if(listHoras.getHourly()!=null) {
                    List<Hourly> finalListHourly = listHourly;
                    AppExecutors.getInstance().mainThread().execute(() -> mOnMeteoHorasLoadedListener.onMeteoHorasLoaded(finalListHourly));
                }
            }
            else{
                Log.e("LIST_NULL","LA LISTA DE HOURLY ES NULA.");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
