package es.unex.giis.zuni.openweather;

import java.util.List;

import es.unex.giis.zuni.api.current.Current;

public interface OnCurrentLoadedListener {
    public void onCurrentLoaded(List<Current> listCurrent);
}
