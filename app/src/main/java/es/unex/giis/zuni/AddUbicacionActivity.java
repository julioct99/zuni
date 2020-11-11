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
                        this::muestraUbicacion,mUbicacion.getText().toString().replace(" ","%20"),spinner3.getSelectedItem().toString().substring(0,2)
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
                String ubicacion = mUbicacion.getText().toString();

                String spinner = spinner3.getSelectedItem().toString();
                Double lat = getLat(ubicacion);
                Double lon = getLon(ubicacion);


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


    /* METODOS AUXILIARES ----------------------------------------------------------------------- */
    //---------------------------------------------------CAMBIARRRRR------------------------------------
    public Double getLat(String ubicacion) {
        Double lat = 0.0;
        switch (ubicacion) {
            case "Monterrubio de la Serena":
                lat = 38.58844;
                break;
            case "Caceres":
                lat = 39.48932;
                break;
            case "Nueva York":
                lat = 40.68908;
                break;
        }
        return lat;
    }


    public Double getLon(String ubicacion) {
        Double lon = 0.0;
        switch (ubicacion) {
            case "Monterrubio de la Serena":
                lon = -5.44484;
                break;
            case "Caceres":
                lon = -6.36581;
                break;
            case "Nueva York":
                lon = -73.95861;
                break;
        }
        return lon;
    }


    /* DIÁLOGO PARA FECHA ----------------------------------------------------------------------- */
/*
    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);

            dateView.setText(dateString);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

*/
    /* DIÁLOGO PARA HORA ------------------------------------------------------------------------ */
/*
    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);

            timeView.setText(timeString);
        }
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

*/
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