
package es.unex.giis.zuni.geocode;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Alt {

    @SerializedName("loc")
    @Expose
    private Loc loc;

    public Loc getLoc() {
        return loc;
    }

    public void setLoc(Loc loc) {
        this.loc = loc;
    }

}
