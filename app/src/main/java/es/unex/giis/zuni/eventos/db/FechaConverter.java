package es.unex.giis.zuni.eventos.db;

import androidx.room.TypeConverter;

import java.util.Date;

public class FechaConverter {
    public static long toTimestamp(Date fecha){
        return fecha == null ? null : fecha.getTime();
    }

    public static Date toDate(Long t){
        return t == null ? null : new Date(t);
    }
}
