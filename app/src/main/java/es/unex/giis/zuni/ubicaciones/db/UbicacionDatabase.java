package es.unex.giis.zuni.ubicaciones.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import es.unex.giis.zuni.ubicaciones.Ubicacion;


@Database(entities = Ubicacion.class, version = 1)
public abstract class UbicacionDatabase extends RoomDatabase {
    private static UbicacionDatabase instance;
    public static UbicacionDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context,UbicacionDatabase.class,"ubicaciones.db").build();
        }
        return instance;
    }

    public abstract UbicacionDao getDao();
}
