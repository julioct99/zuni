package es.unex.giis.zuni.ubicaciones;

        import android.content.Intent;

        import androidx.annotation.NonNull;
        import androidx.room.Ignore;
        import androidx.room.Entity;
        import androidx.room.PrimaryKey;


/* --------------------------------
    Clase Ubicacion: Version 1.0
-------------------------------- */
@Entity(tableName = "ubicaciones")
public class Ubicacion {

    @Ignore
    public static final String ITEM_SEP = System.getProperty("line.separator");


    /* CONSTANTES --------------------------------------------------------------------------------*/

    @Ignore
    public final static String ID = "ID";
    @Ignore
    public final static String UBICACION = "ubicacion";
    @Ignore
    public final static String LAT = "lat";
    @Ignore
    public final static String LON = "lon";
    @Ignore
    public final static String BANDERAUBIFAV = "banderaubifav" ;


    /* ATRIBUTOS -------------------------------------------------------------------------------- */


    @PrimaryKey(autoGenerate = true)
    private long id;
    private String ubicacion;
    private Double lat;     // Latitud
    private Double lon;     // Longitud
    private Boolean banderaUbiFav;


    /* CONSTRUCTORES ---------------------------------------------------------------------------- */

    public Ubicacion (long id,String ubicacion, Double lat, Double lon, Boolean banderaUbiFav){
        this.id = id;
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;
        this.banderaUbiFav = banderaUbiFav;

    }

    @Ignore
    public Ubicacion (long id,String ubicacion, Double lat, Double lon){
        this.id = id;
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;
        this.banderaUbiFav = false;

    }


    @Ignore
    Ubicacion(String ubicacion, Double lat, Double lon, Boolean banderaUbiFav){
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;
        this.banderaUbiFav = banderaUbiFav;
    }



    @Ignore
    public Ubicacion(Intent intent){
        id=intent.getLongExtra(Ubicacion.ID,0);
        ubicacion = intent.getStringExtra(Ubicacion.UBICACION);
        lat = intent.getDoubleExtra(Ubicacion.LAT, 0);
        lon = intent.getDoubleExtra(Ubicacion.LON, 0);
        banderaUbiFav = intent.getBooleanExtra(Ubicacion.BANDERAUBIFAV,false);
    }


    /* GET + SET  ------------------------------------------------------------------------------- */

    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }

    public String getUbicacion() { return this.ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Double getLat() { return this.lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return this.lon; }
    public void setLon(Double lon) { this.lon = lon; }

    public Boolean getBanderaUbiFav() { return this.banderaUbiFav; }
    public void setBanderaUbiFav(Boolean banderaUbiFav) { this.banderaUbiFav = banderaUbiFav; }

    /* METODOS ADICIONALES ---------------------------------------------------------------------- */

    public static void packageIntent(Intent intent, String ubicacion, Double lat, Double lon, Boolean banderaUbiFav){
        intent.putExtra(Ubicacion.UBICACION, ubicacion);
        intent.putExtra(Ubicacion.LAT, lat);
        intent.putExtra(Ubicacion.LON, lon);
        intent.putExtra(Ubicacion.BANDERAUBIFAV,banderaUbiFav);

    }
    @Ignore
    public static void packageIntent(Intent intent, String ubicacion, Double lat, Double lon){
        intent.putExtra(Ubicacion.UBICACION, ubicacion);
        intent.putExtra(Ubicacion.LAT, lat);
        intent.putExtra(Ubicacion.LON, lon);
        intent.putExtra(Ubicacion.BANDERAUBIFAV,false);

    }

    @Ignore
    public static void packageIntent(Intent intent, Ubicacion u){
        intent.putExtra(Ubicacion.ID, u.getId());
        intent.putExtra(Ubicacion.UBICACION, u.getUbicacion());
        intent.putExtra(Ubicacion.LAT, u.getLat());
        intent.putExtra(Ubicacion.LON, u.getLon());
        intent.putExtra(Ubicacion.BANDERAUBIFAV,u.getBanderaUbiFav());
    }


    @Override
    public String toString() {
        return id + " - " + ubicacion;
    }

    //public String toString(){
    //    return "ID: " + id + ITEM_SEP + "Ubicacion: " + ubicacion + "Lat: " + lat + ITEM_SEP
    //            + "Lon: " + lon + ITEM_SEP + "BanderaUbiFav: " + banderaUbiFav;
    //}

    public String toLog() {
        return "ID: " + id + ITEM_SEP + "Ubicacion: " + ubicacion + "Lat: " + lat + ITEM_SEP
                + "Lon: " + lon + ITEM_SEP + "BanderaUbiFav: " + banderaUbiFav;
    }
}
