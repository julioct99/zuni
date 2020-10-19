package es.unex.giis.zuni;

import androidx.room.Entity;

import java.sql.Timestamp;
import java.util.Date;

@Entity
public class MeteoHora {

    private String condicion;
    private String descripcion;
    private float temperatura;
    private float sensacion_termica;
    private float humedad;
    private float viento;
    private float presion;
    private int hora;

    public MeteoHora(){
        condicion="";
        descripcion="";
        temperatura=0;
        sensacion_termica=0;
        humedad=0;
        viento=0;
        presion=0;
        hora=0;
    }

    public MeteoHora(String cond, String desc, float temp, float sens, float hum, float vien, float pres, int h){
        condicion=cond;
        descripcion=desc;
        temperatura=temp;
        sensacion_termica=sens;
        humedad=hum;
        viento=vien;
        presion=pres;
        hora=h;
    }

    public String getCondicion() {
        return condicion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public float getSensacion_termica() {
        return sensacion_termica;
    }

    public float getHumedad() {
        return humedad;
    }

    public float getViento() {
        return viento;
    }

    public float getPresion() {
        return presion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public void setSensacion_termica(float sensacion_termica) {
        this.sensacion_termica = sensacion_termica;
    }

    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }

    public void setViento(float viento) {
        this.viento = viento;
    }

    public void setPresion(float presion) {
        this.presion = presion;
    }

    public int getHora(){
        return hora;
    }

    public void setHora(Timestamp ts){
        hora = new Date(ts.getTime()).getHours();
    }
}
