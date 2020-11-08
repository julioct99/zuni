package es.unex.giis.zuni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.openweather.AppExecutors;

public class DetallesEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        TextView tv = findViewById(R.id.detallesEventoTV);

        Intent intent = getIntent();

        Evento evento = new Evento(intent);
        tv.setText(evento.toString());

        Button bDeleteEvento = findViewById(R.id.deleteEventoButton);
        bDeleteEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        EventoDatabase.getInstance(DetallesEventoActivity.this).getDao()
                                      .delete(evento.getId());
                    }
                });
                finish();
            }
        });

    }


    /* CICLO DE VIDA DEL ACTIVITY --------------------------------------------------------------- */
    @Override
    public void onResume() {
        super.onResume();

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

        // EventoDatabase.getInstance(DetallesEventoActivity.this).close();

        super.onDestroy();
    }
}