package es.unex.giis.zuni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import es.unex.giis.zuni.countrycodes.CountryCode;
import es.unex.giis.zuni.geocode.GeoCode;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.api.porhoras.Hourly;
import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.db.EventoDao;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.geocode.GeoCode;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.GeoCodeNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.MeteoHoraNetworkLoaderRunnable;
import es.unex.giis.zuni.ubicaciones.Ubicacion;

import static java.security.AccessController.getContext;

public class AddUbicacionActivity extends AppCompatActivity {

    private static final String TAG = "Zuni-AddUbicacion";


    private EditText mUbicacion;
    private TextView mTexto;
    private double lat;
    private double lon;
    private Spinner spinner3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ubicacion);


        /* SE INICIALIZAN LOS INPUTS ------------------------------------------------------------ */

        mUbicacion = findViewById(R.id.ubicacionUbicacionInput);
        mTexto = findViewById(R.id.ubicacionTextoUbic);
        spinner3 = findViewById(R.id.spinner3);

        //Llamada a API para extraer códigos de país
        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter2 = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,countryCodes);
        spinner3.setAdapter(spinnerAdapter2);
        spinner3.setSelection(208);



        /* Listener para el boton de CANCEL ----------------------------------------------------- */
        final Button cancelButton = findViewById(R.id.ubicacionCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        /* Listener para el boton de BUSCAR ------------------------------------------------------ */
        final Button buscarButton = findViewById(R.id.ubicacionBuscarButton);
        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppExecutors.getInstance().networkIO().execute(new GeoCodeNetworkLoaderRunnable(
                        this::muestraUbicacion,mUbicacion.getText().toString().trim().replace(" ","%20"),spinner3.getSelectedItem().toString().substring(0,2)
                ));

            }

            private void muestraUbicacion(GeoCode geoCode) {
                lat = Double.parseDouble(geoCode.getLatt());
                lon = Double.parseDouble(geoCode.getLongt());
                mTexto.setText(geoCode.getStandard().getCity());
            }

        });





        /* Listener para el boton de RESET ------------------------------------------------------ */
        final Button resetButton = findViewById(R.id.ubicacionReset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUbicacion.setText("");

            }
        });


        /* Listener para el boton de SUBMIT ----------------------------------------------------- */
        final Button submitButton = findViewById(R.id.ubicacionSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Obtener los datos del evento */
                String ubicacion = mUbicacion.getText().toString().trim();

                String spinner = spinner3.getSelectedItem().toString();

                /* Empaquetar el evento en un intent */
                Intent data = new Intent();
                Ubicacion.packageIntent(data, ubicacion, lat, lon);

                Ubicacion ubicacionCreada = new Ubicacion(data);

                mUbicacion.setText(ubicacionCreada.toString());

                setResult(RESULT_OK, data);
                finish();
            }
        });

    }


    /*LOG --------------------------------------------------------------------------------------- */
    private void log(String msg) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i(TAG, msg);
    }



}