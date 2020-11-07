package es.unex.giis.zuni.eventos.db;

import java.util.List;

import es.unex.giis.zuni.eventos.Evento;

public interface EventoDao {
    public List<Evento> getAll();

    public long insert(Evento item);

    public void delete(long eventoId);

    public void deleteAll();

    public int updateAlerta(Evento item);
}
