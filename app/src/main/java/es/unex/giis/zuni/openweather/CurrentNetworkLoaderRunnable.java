package es.unex.giis.zuni.openweather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.unex.giis.zuni.current.Current;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CurrentNetworkLoaderRunnable implements Runnable {

    private final OnCurrentLoadedListener mOnCurrentLoadedListener;
    private double longt, latt;
    public CurrentNetworkLoaderRunnable(OnCurrentLoadedListener onCurrentLoadedListener, double latt, double longt){
        mOnCurrentLoadedListener= onCurrentLoadedListener;
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

            Current current = service.listCurrent(Double.toString(latt),Double.toString(longt),"55ab2d28aad932680b93bf96e8e44f6e").execute().body();
            List<Current> listCurrent = new ArrayList<Current>();
            listCurrent.add(current);

            AppExecutors.getInstance().mainThread().execute(()->mOnCurrentLoadedListener.onCurrentLoaded(listCurrent));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
