package es.unex.giis.zuni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.api.daily.Datum;
import es.unex.giis.zuni.api.porhoras.Hourly;
import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.MeteoHoraNetworkLoaderRunnable;
import es.unex.giis.zuni.utils.DateRangeChecker;

public class DetallesEventoActivity extends AppCompatActivity {

    // Codigo para peticion de añadir evento
    private static final int EDIT_EVENTO_REQUEST = 2;

    private Evento evento;

    private TextView descripcionTV;
    private TextView tituloTV;
    private TextView ubicacionTV;
    private TextView fechaTV;

    private TextView noPrevisionesTV;
    private TextView fechaDisponibleTV;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    MeteoHoraAdapter mhAdapter;
    DailyAdapter dAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        descripcionTV = findViewById(R.id.descripcionEventoTV);
        tituloTV = findViewById(R.id.tituloEventoTV);
        ubicacionTV = findViewById(R.id.ubicacionEventoTV);
        fechaTV = findViewById(R.id.fechaEventoTV);

        //
        noPrevisionesTV = findViewById(R.id.noPrevisionesTV);
        fechaDisponibleTV = findViewById(R.id.fechaDisponibleEventoTV);
        //

        Intent intent = getIntent();

        evento = new Evento(intent);
        Evento eventoIntent = new Evento(intent);



        mRecyclerView = findViewById(R.id.listaPrevisionesEvento);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(DetallesEventoActivity.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        cargarEvento();


        /* BOTON DE BORRAR EVENTO */
        FloatingActionButton deleteEventoFab = findViewById(R.id.deleteEventoFab);
        deleteEventoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        /* BOTON DE EDITAR EVENTO */
        FloatingActionButton editEventoFab = findViewById(R.id.editEventoFab);
        editEventoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetallesEventoActivity.this,
                        EditEventoActivity.class);

                Evento.packageIntent(intent, evento);

                startActivityForResult(intent, EDIT_EVENTO_REQUEST);
            }
        });
    }


    @Override
    public void onResume(){
        super.onResume();
        if(dAdapter != null){
            dAdapter.clear();
        }
        noPrevisionesTV.setVisibility(View.VISIBLE);
        fechaDisponibleTV.setVisibility(View.VISIBLE);
        cargarEvento();
    }


    /* CARGAR PREVISIONES EN EL ADAPTER --------------------------------------------------------- */
    private void cargarPrevisiones(Evento evento){
        dAdapter = new DailyAdapter(new ArrayList<>());
        AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                dAdapter::swap, evento.getLat(), evento.getLon()

        ));
        mRecyclerView.setAdapter(dAdapter);
    }


    /* MUESTRA LOS ATRIBUTOS DEL EVENTO EN LA PANTALLA ------------------------------------------ */
    private void mostrarInfoEvento(Evento evento){
        descripcionTV.setText(evento.getDescripcion());
        tituloTV.setText(evento.getTitulo());
        ubicacionTV.setText(evento.getUbicacion());
        fechaTV.setText(evento.getFecha().toString());

        // Se comprueba si la fecha del evento es en menos de 14 dias
        DateRangeChecker drc = new DateRangeChecker();
        if(drc.dateWithinDays(evento.getFecha(), 14)){
            noPrevisionesTV.setVisibility(View.INVISIBLE);
            fechaDisponibleTV.setVisibility(View.INVISIBLE);
            cargarPrevisiones(evento);
        } else {
            fechaDisponibleTV.setText(drc.substractDays(evento.getFecha(), 14).toString());
        }
    }


    /* CARGA EL EVENTO DESDE LA BASE DE DATOS --------------------------------------------------- */
    private void cargarEvento(){
        /* SE OBTIENE EL EVENTO DE LA BD */
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Se asigna el evento de la BD a la variable global de la clase.
                evento = EventoDatabase.getInstance(DetallesEventoActivity.this).getDao()
                        .getEvento(evento.getId());

                AppExecutors.getInstance().mainThread().execute(() -> mostrarInfoEvento(evento));
            }
        });
    }


    /* SE ESPERA EL RESULTADO DE LA OPERACION EDIT_EVENTO_REQUEST ------------------------------- */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_EVENTO_REQUEST){
            if (resultCode == Activity.RESULT_OK){
                Evento eventoEditado = new Evento(data);
                eventoEditado.setId(evento.getId());

                /* ACTUALIZAR EVENTO EN LA BASE DE DATOS */
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int id = EventoDatabase.getInstance(DetallesEventoActivity.this)
                                .getDao()
                                .update(eventoEditado);

                        // Se recarga el evento de esta clase para obtener el nuevo objeto
                        AppExecutors.getInstance().mainThread().execute(() -> cargarEvento());
                    }
                });
            }
        }
    }
}