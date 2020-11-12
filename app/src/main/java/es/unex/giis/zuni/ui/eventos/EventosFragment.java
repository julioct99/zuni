package es.unex.giis.zuni.ui.eventos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import es.unex.giis.zuni.AddEventoActivity;
import es.unex.giis.zuni.DetallesEventoActivity;
import es.unex.giis.zuni.MainActivity;
import es.unex.giis.zuni.R;
import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.EventoAdapter;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.openweather.AppExecutors;

public class EventosFragment extends Fragment {

    // Codigo para peticion de añadir evento
    private static final int ADD_EVENTO_REQUEST = 0;
    // Codigo para peticion de eliminar evento
    private static final int DELETE_EVENTO_REQUEST = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EventoAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eventos, container, false);


        /* FAB -> AÑADIR EVENTO ------------------------------------------------------------------*/
        FloatingActionButton fab = root.findViewById(R.id.addEventoFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEventoActivity.class);
                startActivityForResult(intent, ADD_EVENTO_REQUEST);
            }
        });


        mRecyclerView = root.findViewById(R.id.eventos_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        /* LISTENER DE CLICK EN LOS ITEMS DE LA LISTA ------------------------------------------- */
        mAdapter = new EventoAdapter(getActivity(), new EventoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento item) {

                Intent intent = new Intent(getActivity(), DetallesEventoActivity.class);
                Evento.packageIntent(intent, item);
                startActivityForResult(intent, DELETE_EVENTO_REQUEST);
            }
        });


        mRecyclerView.setAdapter(mAdapter);
        EventoDatabase.getInstance(getActivity());
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Snackbar.make(getView(), "Evento añadido", Snackbar.LENGTH_LONG);

        if (requestCode == ADD_EVENTO_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Evento evento = new Evento(data);

                /* INSERTAR EVENTO EN LA BASE DE DATOS */
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        long id = EventoDatabase.getInstance(getActivity())
                                .getDao()
                                .insert(evento);

                        evento.setId(id);

                        AppExecutors.getInstance().mainThread().execute(() -> mAdapter.add(evento));
                    }
                });
            }
        }
        else if (requestCode == DELETE_EVENTO_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Evento evento = new Evento(data);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        EventoDatabase.getInstance(getActivity()).getDao()
                                .delete(evento.getId());
                    }
                });

                AppExecutors.getInstance().mainThread().execute(() -> mAdapter.remove(evento));
            }
        }
    }


    /* CICLO DE VIDA DEL ACTIVITY --------------------------------------------------------------- */

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }


    /* CARGAR LOS ELEMENTOS --------------------------------------------------------------------- */

    private void loadItems() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> items = EventoDatabase.getInstance(getActivity()).getDao().getAll();
                AppExecutors.getInstance().mainThread().execute(() -> mAdapter.load(items));
            }
        });
    }
}