
package es.unex.giis.zuni.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Standard {

    @SerializedName("addresst")
    @Expose
    private Addresst addresst;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("prov")
    @Expose
    private String prov;
    @SerializedName("countryname")
    @Expose
    private String countryname;
    @SerializedName("postal")
    @Expose
    private Postal postal;
    @SerializedName("confidence")
    @Expose
    private String confidence;

    public Addresst getAddresst() {
        return addresst;
    }

    public void setAddresst(Addresst addresst) {
        this.addresst = addresst;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public Postal getPostal() {
        return postal;
    }

    public void setPostal(Postal postal) {
        this.postal = postal;
    }

    public String getConfidence() {
        return confidence;
    }

    public void setConfidence(String confidence) {
        this.confidence = confidence;
    }

}
