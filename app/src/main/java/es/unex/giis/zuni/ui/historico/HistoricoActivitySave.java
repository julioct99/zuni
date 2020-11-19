package es.unex.giis.zuni.ui.historico;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.HistoricalAdapter;
import es.unex.giis.zuni.adapter.HistoricalAdapterListener;
import es.unex.giis.zuni.countrycodes.CountryCode;
import es.unex.giis.zuni.geocode.GeoCode;
import es.unex.giis.zuni.historical.Historical;
import es.unex.giis.zuni.historical.HistoricalMinimal;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.GeoCodeNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.HistoricalNetworkLoaderRunnable;
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
    private ProgressBar mProgressBar;
    private HistoricalAdapter adapter;

    //Variable del Intent de invocacion
    private Intent myIntent;

    //Variables for GeoCode API
    private String cityname = null, countrycode = null;
    private double lon, lat;
    private String geoName;

    //Variable del historico
    private Historical histoData = null;
    private boolean isRecovered;

    //Variable para la carga de Room de las ubicaciones
    static List<Ubicacion> ubis = null;
    private static String seleccion;
    ArrayAdapter<Ubicacion> spinnerAdapter;

    //Variable para la actividad que se esta ejecutando
    private int actNum;
    private View myView;




    //********************************* APPEXECUTOR RETURN FUNCTIONS *********************************

    private void setUbicacion(GeoCode geoCode) {
        lat = Double.parseDouble(geoCode.getLatt());
        lon = Double.parseDouble(geoCode.getLongt());
        geoName = geoCode.getStandard().getCity();
        act1p2();
    }

    //Metodo para que el Thread de recuperación de 1 historico pueda guardar el dato dentro de esta clase
    private void setHistorical(List<Historical> dataset){
        histoData = dataset.get(0);
        if (actNum == 1)
            disp1();
        else if(actNum == 2)
            disp2();

        mProgressBar.setVisibility(View.GONE);
    }




    //********************************* BUTTON ACTIONS *********************************

    public void act1(View v){
        //Tell that is running the act1
        actNum = 1;

        //Save the view
        myView = v;

        //Tell that is recovering data
        isRecovered = false;

        //Get the data from the view
        cityname = et_city.getText().toString().trim();
        countrycode = spinner1.getSelectedItem().toString().substring(0,2);

        Log.i("Historico ACT1", "Se ha pulsado el boto de busqueda de \"" + cityname + "\" en el country \"" + countrycode + "\"");

        if (cityname.equals("")){
            Snackbar.make(v, getString(R.string.Historical_search_err1_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);
        //Get coordinates from the GeoCode API
        AppExecutors.getInstance().networkIO().execute(new GeoCodeNetworkLoaderRunnable(
                this::setUbicacion,cityname.replace(" ","%20"), countrycode
        ));
    }

    private void act1p2(){
        //Load the second part of the act1 after the Thread 1
        Log.i("Historico ACT1", "Se ha recuperado la ubicacion por nombre lat: " + Double.toString(lat) + " long: " + Double.toString(lon));

        //Get the historical from the
        long timeS = (System.currentTimeMillis()) / 1000 - 86400; //Carga el timestamp en segundos de ayer
        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                this::setHistorical,lat,lon,timeS
        ));
    }


    private void disp1(){
        //Display results from act1 after the Thread 2
        Log.i("Historico ACT1", "Se ha recuperado el hisorico por la ubicacion");

        if (histoData == null){
            Snackbar.make(myView, getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
            Log.e("Historico ACT1", "No se ha devuelto un historico valido");
            return;
        }
        Log.i("Historico ACT1", "Se ha devuelto un historico valido");




        //Mostrar el dato en el RecyclerView
        ArrayList<Historical> histoList = new ArrayList<Historical>();
        histoList.add(histoData);
        HistoricalAdapter adapter = new HistoricalAdapter(histoList, new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                //Aqui no se debe hacer nada porque el element no esta en la base de datos (sino que se va a añadir a ella)
                Snackbar.make(v, getText(R.string.Historical_remove_err_msg), Snackbar.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);


        //Actualizar el label del nombre
        String tmp = cityname + ", " + countrycode;
        tv_city.setText(tmp);

        isRecovered = true; //Indicar que se ha recuperado el dato


        Snackbar.make(myView, getString(R.string.Historical_search_msg1) + " " + cityname, Snackbar.LENGTH_SHORT).show();
        Log.i("Historico ACT1", "Se ha pulsado el boton de buscar historico por nombre");
    }






    public void act2(View v){
        //Tell that is running act2
        actNum = 2;

        //Save the view
        myView = v;

        //Tell that is recovering data
        isRecovered = false;

        //Get the location data from the spiner selected location
        Ubicacion seleccionada = (Ubicacion) spinner2.getSelectedItem();
        seleccion = seleccionada.getUbicacion();

        lat=seleccionada.getLat();
        lon=seleccionada.getLon();
        cityname = seleccionada.getUbicacion();
        countrycode = "";


        mProgressBar.setVisibility(View.VISIBLE);
        long timeS = (System.currentTimeMillis()) / 1000 - 86400; //Carga el timestamp en segundos de ayer
        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                this::setHistorical,lat,lon,timeS
        ));
        recyclerView.setAdapter(adapter);
    }


    private void disp2(){
        //Display results from act2 after the Thread 1

        if (histoData == null){
            Snackbar.make(myView, getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
            Log.e("Historico ACT1", "No se ha devuelto un historico valido");
            return;
        }
        Log.i("Historico ACT1", "Se ha devuelto un historico valido");


        //Mostrar el dato en el RecyclerView
        ArrayList<Historical> histoList = new ArrayList<Historical>();
        histoList.add(histoData);
        HistoricalAdapter adapter = new HistoricalAdapter(histoList, new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                //Aqui no se debe hacer nada porque el element no esta en la base de datos (sino que se va a añadir a ella)
                Snackbar.make(v, getText(R.string.Historical_remove_err_msg), Snackbar.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);


        //Actualizar el label del nombre
        String tmp = cityname;
        tv_city.setText(tmp);

        isRecovered = true; //Indicar que se ha recuperado el dato


        Snackbar.make(myView, getString(R.string.Historical_search_msg1) + " " + cityname, Snackbar.LENGTH_SHORT).show();
        Log.i("Historico ACT1", "Se ha pulsado el boton de buscar historico por nombre");
    }







    public void act3(View v){
        //Perform saving
        Log.i("Historico", "Se ha pulsado el boto de guardar el historico");

        //Save the view
        myView = v;

        //Check if historical was recovered correctly
        if (!isRecovered){
            Snackbar.make(v, getString(R.string.Historical_save_err1_msg), Snackbar.LENGTH_SHORT).show();
            return;
        }

        HistoricalMinimal hm = new HistoricalMinimal();
        hm.initFromHistorical(histoData, cityname, countrycode);

        Intent i = new Intent();
        i.putExtra("Historico", hm);
        setResult(RESULT_OK, i);
        finish();
    }


    public void act4(View v){
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
        mProgressBar = findViewById(R.id.progressBar5);
        //goBackButton = findViewById(R.id.GoBackButton);

        isRecovered = false; //Indicar que aun no se ha recuperado ningun dato


        //Inicializar Recycler View
        recyclerView = findViewById(R.id.listHistorical);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);



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
                    //Aqui no se debe hacer nada porque el element no esta en la base de datos (sino que se va a añadir a ella)
                    Snackbar.make(v, getText(R.string.Historical_remove_err_msg), Snackbar.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(adapter);
        }



        //Cargar el spinner de CountryCodes
        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,countryCodes);
        spinner1.setAdapter(spinnerAdapter);
        spinner1.setSelection(208);


        //Cargar la lista de ubicaciones guardadas
        loadItems();




        Log.i("Historico Save", "Se ha cargado el Activity de guardar un historico");
    }


    //********************************* CARGA DEL SPINER DE UBICACIONES *********************************
    public void cargarSpinner(){
        spinnerAdapter =
                new ArrayAdapter(this,  android.R.layout.simple_spinner_dropdown_item, ubis);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);
        //act2();
    }


    private void loadItems() {
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