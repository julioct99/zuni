package es.unex.giis.zuni.ui.ubicaciones;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


import es.unex.giis.zuni.AddUbicacionActivity;
import es.unex.giis.zuni.DetallesUbicacionActivity;
import es.unex.giis.zuni.R;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.UbicacionAdapter;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;

public class UbicacionesFragment extends Fragment {

    // Codigo para peticion de añadir ubicacion
    private static final int ADD_UBICACION_REQUEST = 0;
    // Codigo para peticion de eliminar ubicacion
    private static final int DELETE_UBICACION_REQUEST = 1;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UbicacionAdapter mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ubicaciones, container, false);

        /* FAB -> AÑADIR UBICACION ------------------------------------------------------------------*/
        FloatingActionButton fab = root.findViewById(R.id.addUbicaciones);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddUbicacionActivity.class);
                startActivityForResult(intent, ADD_UBICACION_REQUEST);
            }
        });

        mRecyclerView = root.findViewById(R.id.ubicaciones_recycler_view);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        /* LISTENER DE CLICK EN LOS ITEMS DE LA LISTA ------------------------------------------- */
        mAdapter = new UbicacionAdapter(getActivity(), new UbicacionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Ubicacion item) {

                Intent intent = new Intent(getActivity(), DetallesUbicacionActivity.class);

                Ubicacion.packageIntent(intent, item);

                startActivityForResult(intent, DELETE_UBICACION_REQUEST);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        UbicacionDatabase.getInstance(getActivity());


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Snackbar.make(getView(), "Ubicación añadida", Snackbar.LENGTH_LONG);

        if (requestCode == ADD_UBICACION_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Ubicacion ubicacion = new Ubicacion(data);

                /* INSERTAR EVENTO EN LA BASE DE DATOS */
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        long id = UbicacionDatabase.getInstance(getActivity())
                                .getDao()
                                .insert(ubicacion);

                        ubicacion.setId(id);

                        AppExecutors.getInstance().mainThread().execute(() -> mAdapter.add(ubicacion));
                    }
                });
            }
        }
        else if (requestCode == DELETE_UBICACION_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Ubicacion ubicacion = new Ubicacion(data);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        UbicacionDatabase.getInstance(getActivity()).getDao()
                                .delete(ubicacion.getId());
                    }
                });

                AppExecutors.getInstance().mainThread().execute(() -> mAdapter.remove(ubicacion));
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
                List<Ubicacion> items = UbicacionDatabase.getInstance(getActivity()).getDao().getAll();
                AppExecutors.getInstance().mainThread().execute(() -> mAdapter.load(items));
            }
        });
    }

    }
