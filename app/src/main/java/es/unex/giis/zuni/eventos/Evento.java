package es.unex.giis.zuni.eventos;

import androidx.room.Ignore;

import android.content.Intent;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* --------------------------------
    Clase Evento: Version 1.0
    ToDo:
        Ubicacion
-------------------------------- */
public class Evento {

    public static final String ITEM_SEP = System.getProperty("line.separator");

    /* CONSTANTES --------------------------------------------------------------------------------*/
    public final static String ID = "ID";
    public final static String TITULO = "titulo";
    public final static String DESCRIPCION = "descripcion";
    public final static String FECHA = "fecha";


    /* FORMATO DE LA FECHA -----------------------------------------------------------------------*/

    public final static SimpleDateFormat FORMAT = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss", Locale.US);


    /* ATRIBUTOS -------------------------------------------------------------------------------- */
    private long id;
    private String titulo;
    private String descripcion;
    private Date fecha = new Date();


    /* CONSTRUCTORES ---------------------------------------------------------------------------- */

    public Evento(String titulo, String descripcion, Date fecha){
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Evento(long id, String titulo, String descripcion, String fecha){
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;

        // Se intenta parsear el String con la fecha
        try {
            this.fecha = Evento.FORMAT.parse(fecha);
        } catch (ParseException e){
            this.fecha = new Date();
        }
    }

    Evento(Intent intent){
        id = intent.getLongExtra(Evento.ID, 0);
        titulo = intent.getStringExtra(Evento.TITULO);
        descripcion = intent.getStringExtra(Evento.DESCRIPCION);

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


    /* METODOS ADICIONALES ---------------------------------------------------------------------- */
    public static void packageIntent(Intent intent, String titulo, String descripcion, String fecha){
        intent.putExtra(Evento.TITULO, titulo);
        intent.putExtra(Evento.DESCRIPCION, descripcion);
        intent.putExtra(Evento.FECHA, fecha);
    }

    public String toString(){
        return "ID: " + id + ITEM_SEP + "Titulo: " + titulo + ITEM_SEP + "Descripcion" + descripcion
                + ITEM_SEP + "Fecha: " + FORMAT.format(fecha);
    }
}
