package es.unex.giis.zuni.ui.historico;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.HistoricalAdapter;
import es.unex.giis.zuni.adapter.HistoricalAdapterListener;
import es.unex.giis.zuni.historical.Historical;
import es.unex.giis.zuni.historical.HistoricalMinimal;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;

public class HistoricoActivitySave extends AppCompatActivity {
    private EditText et_city;
    private Spinner spinner1, spinner2;
    private TextView tv_city;
    private Button buttonSearch1, buttonSerach2;
    private Button buttonSave1, buttonSave2;
    //private Button goBackButton;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private HistoricalAdapter adapter;

    //Variable del Intent de invocacion
    private Intent myIntent;

    //Variable del historico
    private Historical histoData;

    //Variable para la carga de Room de las ubicaciones
    static List<Ubicacion> ubis = null;
    private static String seleccion;
    ArrayAdapter<Ubicacion> spinnerAdapter;


    public void act1(View v){
        //TODO Hacer funcion de busqueda en la api

        String cityname = "";

        Log.i("Historico", "Se ha pulsado el boto de guardar el historico");
        Snackbar.make(v, getString(R.string.Historical_save_msg1) + " " + cityname, Snackbar.LENGTH_SHORT).show();

        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }



    public void act2(View v){
        //TODO Hacer funcion de busqueda en la api

    }


    public void act3(View v){
        //TODO Hacer guardado en Room
        //Perform saving
    }


    public void act4(View v){
        //TODO Hacer cancelacion
        //Abort saving
        Log.i("Historico", "Se ha pulsado el boton de cancelar");

        setResult(RESULT_CANCELED);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_save);
        et_city = findViewById(R.id.et_city);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        tv_city = findViewById(R.id.tv_city);
        buttonSearch1 = findViewById(R.id.buttonSearch1);
        buttonSerach2 = findViewById(R.id.buttonSearch2);
        buttonSave1 = findViewById(R.id.buttonSave1);
        buttonSave2 = findViewById(R.id.buttonSave2);
        //goBackButton = findViewById(R.id.GoBackButton);

        //Snackbar.make(root, "Surprise :D", Snackbar.LENGTH_SHORT).show();

        buttonSearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act1(v); //Guarda el historico buscado en la base de datos de historicos
            }
        });


        buttonSerach2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act2(v); //Abortar (Ir hacia atrás)
            }
        });


        buttonSave1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act3(v); //Guardar el historico actual
            }
        });


        buttonSave2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act4(v); //Cancelar guardado e irse a la pantalla anterior
            }
        });

        /*goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Historico", "Se ha pulsado el boton de goBack");
                setResult(RESULT_CANCELED);
                finish();
            }
        });*/


        //Cargar el intent y comprobar si se le ha pasado un historico por el intent
        myIntent = getIntent();

        //Cargar el dato desde el Intent
        String cityname, countrycode;
        cityname = myIntent.getStringExtra("cityname");
        countrycode = myIntent.getStringExtra("countrycode");

        if (cityname != null && countrycode != null){
            //histoData = new Historical(myIntent);
            HistoricalMinimal hm = (HistoricalMinimal) myIntent.getSerializableExtra("data");
            histoData = hm.convertIntoHistorical();

            cityname = myIntent.getStringExtra("cityname");
            countrycode = myIntent.getStringExtra("countrycode");

            String name = cityname + ", " + countrycode;
            tv_city.setText(name);

            //Cargar el RecyclerView
            recyclerView = findViewById(R.id.listHistorical);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getBaseContext());
            recyclerView.setLayoutManager(layoutManager);

            //Mostrar el dato en el RecyclerView
            ArrayList<Historical> histoList = new ArrayList<Historical>();
            histoList.add(histoData);
            HistoricalAdapter adapter = new HistoricalAdapter(histoList, new HistoricalAdapterListener() {
                @Override
                public void imageButtonViewOnClick(View v, int position) {
                    //Todo Insertar codigo aqui
                    //Aqui no se debe hacer nada porque el element no esta en la base de datos (sino que se va a añadir a ella)
                    Snackbar.make(v, getText(R.string.Historical_remove_err_msg), Snackbar.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        }


        //Cargar la lista de ubicaciones guardadas
        loadItems();




        Log.i("Historico Save", "Se ha cargado el Activity de guardar un historico");
    }


    //********************************* CARGA DEL SPINER DE UBICACIONES *********************************
    public void cargarSpinner(){
        //TODO recuperar los datos del spiner del intent
        spinnerAdapter =
                new ArrayAdapter(this,  android.R.layout.simple_spinner_dropdown_item, ubis);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);
        //act2();
    }


    private void loadItems() {
        /*Intent i = getIntent();
        int cnt = i.getIntExtra("ubiCount", 0);

        ubis = new List<Ubicacion>();
        spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ubis);

        for (int x = 0; x < cnt; x--){
            String extraname = "ubi"+x;

            String pName = i.getStringExtra(extraname+"name");
            Double pLat = i.getDoubleExtra(extraname+"lat", 0);
            Double pLon = i.getDoubleExtra(extraname+"lon", 0);
        }*/
        Context mainCont = this;

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ubis = UbicacionDatabase.getInstance(mainCont).getDao().getAll();
                AppExecutors.getInstance().mainThread().execute(() -> cargarSpinner());
            }
        });
    }
}