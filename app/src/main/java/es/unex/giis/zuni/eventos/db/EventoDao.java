package es.unex.giis.zuni.eventos.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.giis.zuni.eventos.Evento;

@Dao
public interface EventoDao {
    @Query("SELECT * FROM eventos")
    public List<Evento> getAll();

    @Insert
    public long insert(Evento item);

    @Query("DELETE FROM eventos WHERE id = :eventoId")
    public void delete(long eventoId);

    @Query("DELETE FROM eventos")
    public void deleteAll();

    @Update
    public int update(Evento item);
}
