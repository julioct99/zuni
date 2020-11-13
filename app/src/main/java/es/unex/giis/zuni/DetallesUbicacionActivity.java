package es.unex.giis.zuni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;


public class DetallesUbicacionActivity extends AppCompatActivity {
    // Codigo para peticion de añadir ubicacion
    private static final int EDIT_UBICACION_REQUEST = 2;


    private Ubicacion ubicacion;

    private TextView ubicacionTV;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    MeteoHoraAdapter mhAdapter;
    DailyAdapter dAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ubicacion);


        ubicacionTV = findViewById(R.id.ubicacionUbicacionTV);



        Intent intent = getIntent();

        ubicacion = new Ubicacion(intent);
        Ubicacion ubicacionIntent = new Ubicacion(intent);



        mRecyclerView = findViewById(R.id.listaDetallesUbicacion);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(DetallesUbicacionActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        cargarUbicacion();


        /* BOTON DE BORRAR UBICACION --------------------------------------------------------------- */
        FloatingActionButton deleteUbicacionFab = findViewById(R.id.deleteUbicacionFab);
        deleteUbicacionFab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        /* BOTON DE EDITAR UBICACION --------------------------------------------------------------- */
        FloatingActionButton editUbicacionFab = findViewById(R.id.editUbicacionFab);
        editUbicacionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesUbicacionActivity.this,
                        EditUbicacionActivity.class);

                Ubicacion.packageIntent(intent, ubicacion);

                startActivityForResult(intent, EDIT_UBICACION_REQUEST);
            }
        });
    }


    /* CARGAR PREVISIONES EN EL ADAPTER --------------------------------------------------------- */
    private void cargarPrevisiones(Ubicacion ubicacion) {
        dAdapter = new DailyAdapter(new ArrayList<>());
        AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                dAdapter::swap, ubicacion.getLat(), ubicacion.getLon()

        ));
        mRecyclerView.setAdapter(dAdapter);
    }


    /* MUESTRA LOS ATRIBUTOS DE LA UBICACION EN LA PANTALLA ------------------------------------------ */
    private void mostrarInfoUbicacion(Ubicacion ubicacion) {

        ubicacionTV.setText(ubicacion.getUbicacion());

        cargarPrevisiones(ubicacion);

    }


    /* CARGA EL EVENTO DESDE LA BASE DE DATOS --------------------------------------------------- */
    private void cargarUbicacion() {
        /* SE OBTIENE LA UBICACION DE LA BD */
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Se asigna el evento de la BD a la variable global de la clase.
                ubicacion = UbicacionDatabase.getInstance(DetallesUbicacionActivity.this).getDao()
                        .getUbicacion(ubicacion.getId());

                AppExecutors.getInstance().mainThread().execute(() -> mostrarInfoUbicacion(ubicacion));
            }
        });
    }
}

