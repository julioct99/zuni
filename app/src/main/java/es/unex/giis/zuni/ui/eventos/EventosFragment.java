package es.unex.giis.zuni.ui.eventos;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private EventoAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_eventos, container, false);
        // final TextView textView = root.findViewById(R.id.text_eventos);
        // textView.setText("This is Eventos fragment");


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


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new EventoAdapter(getActivity(), new EventoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Evento item) {

                Intent intent = new Intent(getActivity(), DetallesEventoActivity.class);

                Evento.packageIntent(intent, item);

                startActivity(intent);

                // ToDo ... Detalles del evento
            }
        });


        mRecyclerView.setAdapter(mAdapter);


        EventoDatabase.getInstance(getActivity());


        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // super.onActivityResult(requestCode, resultCode, data);
        Snackbar.make(getView(), "Evento añadido", Snackbar.LENGTH_LONG);

        if (requestCode == ADD_EVENTO_REQUEST){
            Snackbar.make(mRecyclerView, "Evento añadido", Snackbar.LENGTH_LONG);
        }
    }


    /* CICLO DE VIDA DEL ACTIVITY --------------------------------------------------------------- */
    @Override
    public void onResume() {
        super.onResume();

        // Load saved ToDoItems, if necessary

        if (mAdapter.getItemCount() == 0)
            loadItems();
    }

    @Override
    public void onPause() {
        super.onPause();

        // ALTERNATIVE: Save all ToDoItems
    }

    @Override
    public void onDestroy() {
        // ToDoItemCRUD crud = ToDoItemCRUD.getInstance(this);
        // crud.close();

        EventoDatabase.getInstance(getActivity()).close();

        super.onDestroy();
    }


    /* CARGAR LOS ELEMENTOS --------------------------------------------------------------------- */

    private void loadItems() {
        // ToDoItemCRUD crud = ToDoItemCRUD.getInstance(this);
        // List<ToDoItem> items = crud.getAll();
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<Evento> items = EventoDatabase.getInstance(getActivity()).getDao().getAll();
                AppExecutors.getInstance().mainThread().execute(() -> mAdapter.load(items));
            }
        });
    }
}