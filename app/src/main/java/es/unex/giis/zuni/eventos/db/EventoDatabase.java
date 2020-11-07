package es.unex.giis.zuni.eventos.db;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.giis.zuni.eventos.Evento;

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
