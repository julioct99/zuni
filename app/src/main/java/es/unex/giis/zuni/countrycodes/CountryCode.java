
package es.unex.giis.zuni.countrycodes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryCode {

    public CountryCode(String code, String name){
        this.code=code;
        this.name=name;
    }

    @SerializedName("Code")
    @Expose
    private String code;
    @SerializedName("Name")
    @Expose
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return code.concat(" - ").concat(name);
    }
}
