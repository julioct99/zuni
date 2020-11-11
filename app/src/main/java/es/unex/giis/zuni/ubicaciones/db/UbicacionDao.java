package es.unex.giis.zuni.ubicaciones.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.giis.zuni.ubicaciones.Ubicacion;


@Dao
public interface UbicacionDao {
    @Query("SELECT * FROM ubicaciones")
    public List<Ubicacion> getAll();

    @Query("SELECT * FROM ubicaciones WHERE id = :ubicacionId")
    public Ubicacion getUbicacion(long ubicacionId);

    @Insert
    public long insert(Ubicacion item);

    @Query("DELETE FROM ubicaciones WHERE id = :ubicacionId")
    public void delete(long ubicacionId);

    @Query("DELETE FROM ubicaciones")
    public void deleteAll();

    @Update
    public int update(Ubicacion item);
}