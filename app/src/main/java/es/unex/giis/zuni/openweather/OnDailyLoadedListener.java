package es.unex.giis.zuni.openweather;

        import java.util.List;

        import es.unex.giis.zuni.daily.Daily;

public interface OnDailyLoadedListener {
    public void onDailyLoaded(List<Daily> listDaily);
}
