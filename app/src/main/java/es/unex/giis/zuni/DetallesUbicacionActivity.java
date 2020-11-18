package es.unex.giis.zuni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;


public class DetallesUbicacionActivity extends AppCompatActivity {
    // Codigo para peticion de añadir ubicacion
    private static final int FAV_UBICACION_REQUEST = 2;


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


        /* BOTON DE PONER LA UBICACION COMO PREDETERMINADA--------------------------------------------------------------- */
        FloatingActionButton favUbicacionFab = findViewById(R.id.favUbicacionFab);
        favUbicacionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesUbicacionActivity.this,
                        FavUbicacionActivity.class);

                Ubicacion.packageIntent(intent, ubicacion);

                startActivityForResult(intent, FAV_UBICACION_REQUEST);
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

        if(ubicacion.getBanderaUbiFav()) {
            ubicacionTV.setText(ubicacion.getUbicacion().concat(" (Ubicación predeterminada)"));
        }else{
            ubicacionTV.setText(ubicacion.getUbicacion());
        }

        cargarPrevisiones(ubicacion);

    }


    /* CARGA EL UBICACION DESDE LA BASE DE DATOS --------------------------------------------------- */
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

    /* SE ESPERA EL RESULTADO DE LA OPERACION FAV_UBICACION_REQUEST ------------------------------- */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FAV_UBICACION_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Ubicacion ubicacionFav = new Ubicacion(data);
                ubicacionFav.setId(ubicacion.getId());

                //PONER LA UBICACION CON (Ubicación predeterminada)
                ubicacionTV.setText(ubicacion.getUbicacion());

                /* ACTUALIZAR UBICACION EN LA BASE DE DATOS */
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int id = UbicacionDatabase.getInstance(DetallesUbicacionActivity.this)
                                .getDao()
                                .update(ubicacionFav);

                        // Se recarga la ubicacion de esta clase para obtener el nuevo objeto
                        AppExecutors.getInstance().mainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if(dAdapter != null){
                                    dAdapter.clear();
                                }

                                cargarUbicacion();
                            }
                        });
                    }
                });
            }
        }
    }
 }

