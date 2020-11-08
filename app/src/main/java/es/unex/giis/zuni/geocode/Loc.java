
package es.unex.giis.zuni.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loc {

    @SerializedName("longt")
    @Expose
    private String longt;
    @SerializedName("prov")
    @Expose
    private String prov;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("countryname")
    @Expose
    private String countryname;
    @SerializedName("postal")
    @Expose
    private String postal;
    @SerializedName("region")
    @Expose
    private Region region;
    @SerializedName("latt")
    @Expose
    private String latt;

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public String getLatt() {
        return latt;
    }

    public void setLatt(String latt) {
        this.latt = latt;
    }

}
