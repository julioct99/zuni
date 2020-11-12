package es.unex.giis.zuni.ubicaciones;

        import android.content.Intent;

        import androidx.annotation.NonNull;
        import androidx.room.Ignore;
        import androidx.room.Entity;
        import androidx.room.PrimaryKey;

        import es.unex.giis.zuni.eventos.Evento;


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


    /* ATRIBUTOS -------------------------------------------------------------------------------- */


    @PrimaryKey(autoGenerate = true)
    private long id;
    private String ubicacion;
    private Double lat;     // Latitud
    private Double lon;     // Longitud


    /* CONSTRUCTORES ---------------------------------------------------------------------------- */

    public Ubicacion (long id,String ubicacion, Double lat, Double lon){
        this.id = id;
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;

    }

    @Ignore
    Ubicacion(String ubicacion, Double lat, Double lon){

        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;
    }



    @Ignore
    public Ubicacion(Intent intent){
        id=intent.getLongExtra(Ubicacion.ID,0);
        ubicacion = intent.getStringExtra(Ubicacion.UBICACION);
        lat = intent.getDoubleExtra(Evento.LAT, 0);
        lon = intent.getDoubleExtra(Evento.LON, 0);
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

    /* METODOS ADICIONALES ---------------------------------------------------------------------- */

    public static void packageIntent(Intent intent, String ubicacion, Double lat, Double lon){
        intent.putExtra(Ubicacion.UBICACION, ubicacion);
        intent.putExtra(Evento.LAT, lat);
        intent.putExtra(Evento.LON, lon);

    }

    @Ignore
    public static void packageIntent(Intent intent, Ubicacion u){
        intent.putExtra(Ubicacion.ID, u.getId());
        intent.putExtra(Ubicacion.UBICACION, u.getUbicacion());
        intent.putExtra(Evento.LAT, u.getLat());
        intent.putExtra(Evento.LON, u.getLon());
    }


    @Override
    public String toString() {
        return id + " - " + ubicacion;
    }

    //public String toString(){
    //    return "ID: " + id + ITEM_SEP + "Ubicacion: " + ubicacion + "Lat: " + lat + ITEM_SEP
    //            + "Lon: " + lon;
    //}

    public String toLog() {
        return "ID: " + id + ITEM_SEP + "Ubicacion: " + ubicacion + "Lat: " + lat + ITEM_SEP
                + "Lon: " + lon;
    }
}
