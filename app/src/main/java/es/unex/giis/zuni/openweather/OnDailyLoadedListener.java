package es.unex.giis.zuni.openweather;
import java.util.List;
import es.unex.giis.zuni.daily.Datum;

public interface OnDailyLoadedListener {
    public void onDailyLoaded(List<Datum> listDaily);
}
