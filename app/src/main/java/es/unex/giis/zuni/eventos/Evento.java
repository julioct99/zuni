package es.unex.giis.zuni.eventos;

import android.content.Intent;

import androidx.room.Ignore;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.unex.giis.zuni.eventos.db.AlertaConverter;
import es.unex.giis.zuni.eventos.db.FechaConverter;

/* --------------------------------
    Clase Evento: Version 2.0
-------------------------------- */
@Entity(tableName = "eventos")
public class Evento {

    @Ignore
    public static final String ITEM_SEP = System.getProperty("line.separator");


    /* CONSTANTES --------------------------------------------------------------------------------*/

    @Ignore
    public final static String ID = "ID";
    @Ignore
    public final static String TITULO = "titulo";
    @Ignore
    public final static String DESCRIPCION = "descripcion";
    @Ignore
    public final static String FECHA = "fecha";
    @Ignore
    public final static String ALERTA = "alerta";
    @Ignore
    public final static String UBICACION = "ubicacion";
    @Ignore
    public final static String LAT = "lat";
    @Ignore
    public final static String LON = "lon";


    /* FORMATO DE LA FECHA -----------------------------------------------------------------------*/

    @Ignore
    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);


    /* ATRIBUTOS -------------------------------------------------------------------------------- */

    // Nivel de alerta del evento. Por defecto sera BAJA
    public enum Alerta {
        BAJA, MEDIA, ALTA
    }

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String titulo;
    private String descripcion;
    @TypeConverters(FechaConverter.class)
    private Date fecha = new Date();
    @TypeConverters(AlertaConverter.class)
    private Alerta alerta = Alerta.BAJA;
    private String ubicacion;
    private Double lat;     // Latitud
    private Double lon;     // Longitud


    /* CONSTRUCTORES ---------------------------------------------------------------------------- */

    public Evento(long id, String titulo, String descripcion, Date fecha, Alerta alerta,
                  String ubicacion, Double lat, Double lon){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.alerta = alerta;
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;
    }

    @Ignore
    Evento(String titulo, String descripcion, Date fecha, Alerta alerta, String ubicacion,
           Double lat, Double lon){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.alerta = alerta;
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;
    }

    @Ignore
    public Evento(long id, String titulo, String descripcion, String fecha, String alerta,
                  String ubicacion, Double lat, Double lon){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.alerta = Alerta.valueOf(alerta);
        this.ubicacion = ubicacion;
        this.lat = lat;
        this.lon = lon;

        // Se intenta parsear el String con la fecha
        try {
            this.fecha = Evento.FORMAT.parse(fecha);
        } catch (ParseException e){
            this.fecha = new Date();
        }
    }

    @Ignore
    Evento(Intent intent){
        id = intent.getLongExtra(Evento.ID, 0);
        titulo = intent.getStringExtra(Evento.TITULO);
        descripcion = intent.getStringExtra(Evento.DESCRIPCION);
        alerta = Alerta.valueOf(intent.getStringExtra(Evento.ALERTA));
        ubicacion = intent.getStringExtra(Evento.UBICACION);
        lat = intent.getDoubleExtra(Evento.LAT, 0);
        lon = intent.getDoubleExtra(Evento.LON, 0);

        // Se intenta parsear el String con la fecha
        try {
            fecha = Evento.FORMAT.parse(intent.getStringExtra(Evento.FECHA));
        } catch (ParseException e){
            fecha = new Date();
        }
    }


    /* GET + SET  ------------------------------------------------------------------------------- */

    public long getId() { return this.id; }
    public void setId(long id) { this.id = id; }

    public String getTitulo() { return this.titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return this.descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFecha() { return this.fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Alerta getAlerta() { return this.alerta; }
    public void setAlerta(Alerta alerta) { this.alerta = alerta; }

    public String getUbicacion() { return this.ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public Double getLat() { return this.lat; }
    public void setLat(Double lat) { this.lat = lat; }

    public Double getLon() { return this.lon; }
    public void setLon(Double lon) { this.lon = lon; }


    /* METODOS ADICIONALES ---------------------------------------------------------------------- */

    public static void packageIntent(Intent intent, String titulo, String descripcion, String fecha,
                                     Alerta alerta, String ubicacion, Double lat, Double lon){
        intent.putExtra(Evento.TITULO, titulo);
        intent.putExtra(Evento.DESCRIPCION, descripcion);
        intent.putExtra(Evento.FECHA, fecha);
        intent.putExtra(Evento.ALERTA, alerta.toString());
        intent.putExtra(Evento.UBICACION, ubicacion);
        intent.putExtra(Evento.LAT, lat);
        intent.putExtra(Evento.LON, lon);
    }

    public String toString(){
        return "ID: " + id + ITEM_SEP + "Titulo: " + titulo + ITEM_SEP + "Descripcion" + descripcion
                + ITEM_SEP + "Fecha: " + FORMAT.format(fecha) + ITEM_SEP + "Alerta: " + alerta
                + ITEM_SEP + "Ubicacion: " + ubicacion + ITEM_SEP + "Lat: " + lat + ITEM_SEP
                + "Lon: " + lon;
    }

    public String toLog() {
        return "ID: " + id + ITEM_SEP + "Titulo: " + titulo + ITEM_SEP + "Descripcion" + descripcion
                + ITEM_SEP + "Fecha: " + FORMAT.format(fecha) + ITEM_SEP + "Alerta: " + alerta
                + ITEM_SEP + "Ubicacion: " + ubicacion + ITEM_SEP + "Lat: " + lat + ITEM_SEP
                + "Lon: " + lon;
    }
}
