package es.unex.giis.zuni.openweather;

import es.unex.giis.zuni.geocode.GeoCode;

public interface OnGeoCodeLoadedListener {
    public void onGeoCodeLoaded(GeoCode geoCode);
}
