/*package es.unex.giis.zuni.openweather;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import es.unex.giis.zuni.Hourly;
import es.unex.giis.zuni.MeteoHora;


public class MeteoHorasLoaderRunnable implements Runnable {

    private final InputStream mInFile;
    private final OnMeteoHorasLoadedListener mOnMeteoHorasLoadedListener;

    public MeteoHorasLoaderRunnable(InputStream inFile, OnMeteoHorasLoadedListener onMeteoHorasLoadedListener){
        mInFile = inFile;
        mOnMeteoHorasLoadedListener = onMeteoHorasLoadedListener;
    }

    @Override
    public void run() {
        // Obtención de los datos a partir del InputStream
        // Llamada al Listener con los datos obtenidos

        JsonReader reader = new JsonReader(new InputStreamReader(mInFile));
        MeteoHora meteoHoras = new MeteoHora();
        Gson gson = new Gson();
        meteoHoras.setLat(gson.fromJson(reader,Double.class));
        meteoHoras.setLon(gson.fromJson(reader,Double.class));
        meteoHoras.setTimezone(gson.fromJson(reader,String.class));
        meteoHoras.setTimezoneOffset(gson.fromJson(reader,Integer.class));

        meteoHoras.setHourly(Arrays.asList(new Gson().fromJson(reader, Hourly[].class)));


        /*
        for(int i=0;i<10;i++){
           Log.i("***********************",meteoHoras.getLat() + meteoHoras.getLon()+meteoHoras.getTimezone()+meteoHoras.getTimezoneOffset());
        }
*/

        /*for(MeteoHora r: meteoHoras){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                mOnMeteoHorasLoadedListener.onMeteoHorasLoaded(meteoHoras);
            }
        });
    }
}
*/