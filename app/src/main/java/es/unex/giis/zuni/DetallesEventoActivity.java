package es.unex.giis.zuni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

import es.unex.giis.zuni.eventos.Evento;

public class DetallesEventoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);

        TextView tv = findViewById(R.id.detallesEventoTV);

        Intent intent = getIntent();

        Evento evento = new Evento(intent);
        tv.setText(evento.toString());

    }
}