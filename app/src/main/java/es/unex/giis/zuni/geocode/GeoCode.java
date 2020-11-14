
package es.unex.giis.zuni.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoCode {

    @SerializedName("standard")
    @Expose
    private Standard standard;
    @SerializedName("longt")
    @Expose
    private String longt;
    @SerializedName("alt")
    @Expose
    private Alt alt;
    @SerializedName("elevation")
    @Expose
    private Elevation elevation;
    @SerializedName("latt")
    @Expose
    private String latt;

    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public Alt getAlt() {
        return alt;
    }

    public void setAlt(Alt alt) {
        this.alt = alt;
    }

    public Elevation getElevation() {
        return elevation;
    }

    public void setElevation(Elevation elevation) {
        this.elevation = elevation;
    }

    public String getLatt() {
        return latt;
    }

    public void setLatt(String latt) {
        this.latt = latt;
    }

}
