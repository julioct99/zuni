package es.unex.giis.zuni.historical.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.giis.zuni.historical.HistoricalMinimal;
import es.unex.giis.zuni.ubicaciones.Ubicacion;

@Database(entities = HistoricalMinimal.class, version = 1)
public abstract class HistoricalMinimalDatabase extends RoomDatabase {
    private static HistoricalMinimalDatabase instance;

    public static HistoricalMinimalDatabase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context, HistoricalMinimalDatabase.class, "historicals.db").build();
        }
        return instance;
    }

    public abstract HistoricalMinimalDAO getDao();
}
