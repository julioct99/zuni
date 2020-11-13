package es.unex.giis.zuni.openweather;

import java.io.IOException;

import es.unex.giis.zuni.geocode.GeoCode;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class GeoCodeNetworkLoaderRunnable implements Runnable {

    private final OnGeoCodeLoadedListener mOnGeoCodeLoadedListener;
    private double longt, latt;
    String city, country;
    public GeoCodeNetworkLoaderRunnable(OnGeoCodeLoadedListener onGeoCodeLoadedListener, String city, String country){
        mOnGeoCodeLoadedListener= onGeoCodeLoadedListener;
        this.city=city;
        this.country=country;
    }

    @Override
    public void run() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://geocode.xyz/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenWeatherMapService service = retrofit.create(OpenWeatherMapService.class);
        try {

            if(city!=null && country!=null) {
                if (!city.equals("") && !country.equals("")) {
                    GeoCode geoCode = service.getGeoCode(city.concat(",").concat(country),1).execute().body();
                    if (geoCode != null) {
                        AppExecutors.getInstance().mainThread().execute(() -> mOnGeoCodeLoadedListener.onGeoCodeLoaded(geoCode));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
