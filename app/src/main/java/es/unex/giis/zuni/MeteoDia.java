package es.unex.giis.zuni;

import androidx.room.Entity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
public class MeteoDia {

    private String condicion;
    private String descripcion;
    private float max;
    private float min;
    private float humedad;
    private float viento;
    private float presion;
    private Date fecha;

    private MeteoHora horas;

    public MeteoDia(){
        condicion="";
        descripcion="";
        max=0;
        min=0;
        humedad=0;
        viento=0;
        presion=0;
        fecha=new Date();
        horas=new MeteoHora();
    }

    public String getCondicion(){
        return condicion;
    }

    public String getDescripcion(){
        return descripcion;
    }

    public float getHumedad() {
        return humedad;
    }

    public Date getFecha() {
        return fecha;
    }

    public float getMax() {

        return max;
    }

    public float getMin() {
        return min;
    }

    public float getPresion() {
        return presion;
    }

    public float getViento() {
        return viento;
    }

    public List<Hourly> getHoras() {
        return horas.getHourly();
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFecha(Date d) {
        this.fecha = d;
    }

    public void setHoras(MeteoHora horas) {
        this.horas = horas;
    }

    public void setListaHoras(List<Hourly> list){
        this.horas.setHourly(list);
    }

    public void setHumedad(float humedad) {
        this.humedad = humedad;
    }

    public void setMax(float max) {
        this.max = max-273.15f;
    }

    public void setMin(float min) {
        this.min = min-273.15f;
    }

    public void setPresion(float presion) {
        this.presion = presion;
    }

    public void setViento(float viento) {
        this.viento = viento;
    }

    public void setDatosEjemplo() {

        condicion = "Clear";
        descripcion = "clear sky";
        max = 23.5f;
        min = 10f;
        humedad = 10;
        viento = 23.5f;
        presion = 1017f;
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        fecha = today.getTime();

        /*for (int i = 0; i < 24; i++) {
            String _condicion =null;
            switch (i%6){
                case 0:
                    _condicion="Thunderstorm";
                    break;
                case 1:
                case 2:
                    _condicion="Rain";
                    break;
                case 3:
                    _condicion="Snow";
                    break;
                case 4:
                    _condicion="Clear";
                    break;
                case 5:
                    _condicion="Clouds";
                    break;
            }*/
            //horas.add(new MeteoHora(_condicion, "clear sky", i + 0.1f * i, i + 0.1f * i - 0.3f, 10 + i / 2, i, 100 * i, i));


    }
}
