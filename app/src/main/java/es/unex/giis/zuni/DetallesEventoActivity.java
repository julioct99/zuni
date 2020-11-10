package es.unex.giis.zuni;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Date;

import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.openweather.AppExecutors;

public class DetallesEventoActivity extends AppCompatActivity {

    // Codigo para peticion de añadir evento
    private static final int EDIT_EVENTO_REQUEST = 2;

    private Evento evento;

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        tv = findViewById(R.id.detallesEventoTV);

        Intent intent = getIntent();

        evento = new Evento(intent);
        Evento eventoIntent = new Evento(intent);


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


    /* CARGA EL EVENTO DESDE LA BASE DE DATOS --------------------------------------------------- */
    private void cargarEvento(){
        /* SE OBTIENE EL EVENTO DE LA BD */
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                // Se asigna el evento de la BD a la variable global de la clase.
                evento = EventoDatabase.getInstance(DetallesEventoActivity.this).getDao()
                        .getEvento(evento.getId());

                AppExecutors.getInstance().mainThread().execute(() ->tv.setText(evento.toString()));
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