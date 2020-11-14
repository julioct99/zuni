package es.unex.giis.zuni.historical.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import es.unex.giis.zuni.historical.HistoricalMinimal;

@Dao
public interface HistoricalMinimalDAO {
    @Query("SELECT * FROM historicals")
    public List<HistoricalMinimal> getAll();

    @Query("SELECT * FROM historicals WHERE id = :id")
    public HistoricalMinimal getHistorical(long id);

    @Query("SELECT * FROM historicals WHERE cityname = :cityname")
    public List<HistoricalMinimal> getHistoricalsByCityname(String cityname);

    @Insert
    public long insert(HistoricalMinimal item);

    @Query("DELETE FROM historicals WHERE cityname = :cityname AND Dt = :dt")
    public void delete(String cityname, long dt);

    @Update
    public int update(HistoricalMinimal item);


    @Query("DELETE FROM historicals")
    public void deleteAll();
}
