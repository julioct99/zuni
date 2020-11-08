package es.unex.giis.zuni.openweather;

import java.util.List;

import es.unex.giis.zuni.api.porhoras.Hourly;

public interface OnMeteoHorasLoadedListener {
    public void onMeteoHorasLoaded(List<Hourly> listHoras);
}
