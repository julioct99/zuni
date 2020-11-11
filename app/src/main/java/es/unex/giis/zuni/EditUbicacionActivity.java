package es.unex.giis.zuni;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import es.unex.giis.zuni.ubicaciones.Ubicacion;

public class EditUbicacionActivity extends AppCompatActivity {

    private Ubicacion ubicacion;


    private static final String TAG = "Zuni-AddEvento";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ubicacion);

    }
}
