package es.unex.giis.zuni.eventos.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.giis.zuni.eventos.Evento;

@Database(entities = Evento.class, version = 1)
public abstract class EventoDatabase extends RoomDatabase {
    private static EventoDatabase instance;

    public static EventoDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context, EventoDatabase.class, "eventos.db")
                    .build();
        }
        return instance;
    }
}
