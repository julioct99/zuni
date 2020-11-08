package es.unex.giis.zuni.openweather;

import java.util.List;

import es.unex.giis.zuni.historical.Hourly;

public interface OnHistoricalLoadedListener {
    public void onHistoricalLoaded(List<Hourly> listHistorical);
}
