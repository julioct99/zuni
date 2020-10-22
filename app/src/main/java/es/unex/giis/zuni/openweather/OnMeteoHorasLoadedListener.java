package es.unex.giis.zuni.openweather;

import java.util.List;

import es.unex.giis.zuni.utils.Hourly;

public interface OnMeteoHorasLoadedListener {
    public void onMeteoHorasLoaded(List<Hourly> listHoras);
}
