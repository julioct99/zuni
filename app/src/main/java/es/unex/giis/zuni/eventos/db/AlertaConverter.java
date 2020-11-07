package es.unex.giis.zuni.eventos.db;

import androidx.room.TypeConverter;

import es.unex.giis.zuni.eventos.Evento;

public class AlertaConverter {
    public static String toString(Evento.Alerta alerta) {
        return alerta == null ? null : alerta.name();
    }

    public static Evento.Alerta toAlerta(String alerta){
        return alerta == null ? null : Evento.Alerta.valueOf(alerta);
    }
}
