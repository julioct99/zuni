package es.unex.giis.zuni;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.eventos.db.EventoDatabase;
import es.unex.giis.zuni.openweather.AppExecutors;

public class EditEventoActivity extends AppCompatActivity {

    private Evento evento;

    // 7 Dias en milisegundos
    private static final int SEVEN_DAYS = 604800000;

    private static final String TAG = "Zuni-AddEvento";

    private static String timeString;
    private static String dateString;
    private static TextView dateView;
    private static TextView timeView;

    private Date mFecha;
    private EditText mTitulo;
    private EditText mDescripcion;
    private Spinner mUbicacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_evento);

        /* SE INICIALIZAN LOS INPUTS ------------------------------------------------------------ */
        mTitulo = findViewById(R.id.eventoTituloInput);
        mDescripcion = findViewById(R.id.eventoDescripcionInput);
        mUbicacion = findViewById(R.id.eventoUbicacionSpinner);

        dateView = findViewById(R.id.eventoDate);
        timeView = findViewById(R.id.eventoTime);

        String [] opciones = {"Monterrubio de la Serena","Caceres","Nueva York"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(EditEventoActivity.this,
                android.R.layout.simple_spinner_item,opciones);
        mUbicacion.setAdapter(spinnerAdapter);


        /* SE OBTIENE EL EVENTO DEL INTENT */
        evento = new Evento(getIntent());
        Evento eventoIntent = new Evento(getIntent());

        /* SE OBTIENE EL EVENTO DE LA BD  */
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                evento = EventoDatabase.getInstance(EditEventoActivity.this).getDao()
                        .getEvento(eventoIntent.getId());

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        /* SE CARGAN LOS ATRIBUTOS DEL EVENTO EN LOS INPUTS --------------------- */
                        mTitulo.setText(evento.getTitulo());
                        mDescripcion.setText(evento.getDescripcion());

                        int posicionSpinner = spinnerAdapter.getPosition(evento.getUbicacion());
                        mUbicacion.setSelection(posicionSpinner);

                        setDateTime(evento.getFecha());
                    }
                });
            }
        });


        /* Listener para el boton de fecha ------------------------------------------------------ */
        final Button mEventoFechaInput = findViewById(R.id.eventoFechaButton);
        mEventoFechaInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        /* Listener para el boton de hora ------------------------------------------------------- */
        final Button mEventoHoraInput = findViewById(R.id.eventoHoraButton);
        mEventoHoraInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });


        /* Listener para el boton de CANCEL ----------------------------------------------------- */
        final Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });


        /* Listener para el boton de RESET ------------------------------------------------------ */
        final Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitulo.setText("");
                mDescripcion.setText("");
                setDefaultDateTime();
            }
        });


        /* Listener para el boton de SUBMIT ----------------------------------------------------- */
        final Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Obtener los datos del evento */
                String titulo = mTitulo.getText().toString();
                String descripcion = mDescripcion.getText().toString();
                String fullDate = dateString + " " + timeString;
                Evento.Alerta alerta = getAlerta();
                String ubicacion = mUbicacion.getSelectedItem().toString();
                Double lat = getLat(ubicacion);
                Double lon = getLon(ubicacion);


                /* Empaquetar el evento en un intent */
                Intent data = new Intent();
                Evento.packageIntent(data, titulo, descripcion, fullDate, alerta, ubicacion, lat,
                        lon);

                Evento eventoCreado = new Evento(data);

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }




    /* METODOS AUXILIARES ----------------------------------------------------------------------- */
    private void setDateTime(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));

        dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));

        timeView.setText(timeString);
    }


    private void setDefaultDateTime(){
        // Por defecto es el tiempo actual + 7 dias
        mFecha = new Date();
        mFecha = new Date(mFecha.getTime() + SEVEN_DAYS);

        setDateTime(mFecha);
    }


    private static void setDateString(int year, int monthOfYear, int dayOfMonth){
        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = year + "-" + mon + "-" + day;
    }


    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";
    }


    public Evento.Alerta getAlerta(){
        return Evento.Alerta.BAJA;
    }


    public Double getLat(String ubicacion){
        Double lat = 0.0;
        switch (ubicacion){
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


    public Double getLon(String ubicacion){
        Double lon = 0.0;
        switch (ubicacion){
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
        DialogFragment newFragment = new EditEventoActivity.DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }


    /* DIÁLOGO PARA HORA ------------------------------------------------------------------------ */

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
        DialogFragment newFragment = new EditEventoActivity.TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
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