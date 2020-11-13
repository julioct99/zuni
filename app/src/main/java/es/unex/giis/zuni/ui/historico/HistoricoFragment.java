package es.unex.giis.zuni.ui.historico;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import es.unex.giis.zuni.ui.detalles.DetallesFragment;

@SuppressWarnings("ALL")
public class HistoricoFragment extends Fragment {
    private static final int REQUEST_SAVE_RESULT = 1;

    private EditText EditText_city;
    private Spinner spinner1, spinner2;
    private Button button1, button2;
    FloatingActionButton buttonAdd;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //Variables for GeoCode API
    private String cityname, countrycode;
    private double lon, lat;
    private String geoName;

    //Variables for OpenWheather API
    private Historical histoData;

    //Variable del Historical Adapter
    private HistoricalAdapter adapter;

    //Variable para la carga de Room de las ubicaciones
    static List<Ubicacion> ubis = null;
    private static String seleccion;
    ArrayAdapter<Ubicacion> spinnerAdapter;

    //Variable para la actividad que se esta ejecutando
    private int actNum;

//TODO IMPORTANT Hacer Room
    //********************************* CARGA DEL SPINER DE UBICACIONES *********************************
    public void cargarSpinner(){
        spinnerAdapter =
                new ArrayAdapter(getContext(),  android.R.layout.simple_spinner_dropdown_item, ubis);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);
        act2();
    }


    private void loadItems() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ubis = UbicacionDatabase.getInstance(getActivity()).getDao().getAll();
                AppExecutors.getInstance().mainThread().execute(() -> cargarSpinner());
            }
        });
    }


    //********************************* ACCIONES DE LA GUI *********************************
    private void act1(View v){
        //TODO Arreglar boton
        //Tell that is running the act1
        actNum = 1;

        //Get the data from the view
        cityname = EditText_city.getText().toString().trim();
        countrycode = spinner1.getSelectedItem().toString().substring(0,2);

        Log.i("Historico ACT1", "Se ha pulsado el boto de busqueda de \"" + cityname + "\" en el country \"" + countrycode + "\"");

        if (cityname.equals("")){
            Snackbar.make(v, getString(R.string.Historical_search_err1_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
            return;
        }


        //Get coordinates from the GeoCode API
        AppExecutors.getInstance().networkIO().execute(new GeoCodeNetworkLoaderRunnable(
                this::setUbicacion,cityname.replace(" ","%20"), countrycode
        ));
    }


    private void act1p2(){
        //Load the second part of the act1 after the Thread 1
        Log.i("Historico ACT1", "Se ha recuperado la ubicacion por nombre lat: " + Double.toString(lat) + " long: " + Double.toString(lon));

        //Get the historical from the
        Long timeS = (System.currentTimeMillis()) / 1000 - 86400; //Carga el timestamp en segundos de ayer
        adapter = new HistoricalAdapter(new ArrayList<Historical>(), new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                //TODO Añadir aqui el codigo
                Snackbar.make(v, "ASDF", Snackbar.LENGTH_SHORT).show();
            }
        });
        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                this::setHistorical,lat,lon,timeS
        ));

    }


    private void disp1(){
        //Display results from act1 after the Thread 2
        Log.i("Historico ACT1", "Se ha recuperado el hisorico por la ubicacion");

        if (histoData == null){
            Snackbar.make(getView(), getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
            Log.e("Historico ACT1", "No se ha devuelto un historico valido");
            return;
        }
        Log.i("Historico ACT1", "Se ha devuelto un historico valido");


        //Se invoca la nueva pantalla y se añade el dato
        Intent i = new Intent(getActivity(), HistoricoActivitySave.class);
        HistoricalMinimal hm = new HistoricalMinimal();
        hm.initFromHistorical(histoData);
        i.putExtra("data", hm);
        //histoData.packageIntoIntent(i, histoData);
        i.putExtra("cityname", cityname);
        i.putExtra("countrycode", countrycode);
        startActivityForResult(i, REQUEST_SAVE_RESULT);


        Log.i("Historico ACT1", "Se ha pulsado el boto de buscar historico por nombre");
        //Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
    }






    private void act2(){
        //TODO Arreglar boton
        //Tell taht is running act2
        actNum = 2;

        //Get the location data from the spiner selected location
        Ubicacion seleccionada = (Ubicacion) spinner2.getSelectedItem();
        seleccion = seleccionada.getUbicacion();

        lat=seleccionada.getLat();
        lon=seleccionada.getLon();



        Long timeS = (System.currentTimeMillis()) / 1000 - 86400; //Carga el timestamp en segundos de ayer
        adapter = new HistoricalAdapter(new ArrayList<>(), new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                Snackbar.make(v, "Esto es una prueba del boton de borrado en la clase HistoricoFragent", Snackbar.LENGTH_SHORT).show();
                //TODO Añadir aqui el codigo
            }
        });

        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                adapter::swap,lat,lon,timeS
        ));
        recyclerView.setAdapter(adapter);
        

        //Get the data from the view
        //String seleccion = spinner2.getSelectedItem().toString();




        //Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();


        //startSaveActivity();
    }








    private void act3(View v){
        //TODO Abrir la actividad de guardar historico usando un intent

        //Se invoca la nueva pantalla y se añade el dato
        Intent i = new Intent(getActivity(), HistoricoActivitySave.class);
        startActivityForResult(i, REQUEST_SAVE_RESULT);
    }






    private void setUbicacion(GeoCode geoCode) {
        lat = Double.parseDouble(geoCode.getLatt());
        lon = Double.parseDouble(geoCode.getLongt());
        geoName = geoCode.getStandard().getCity();
        act1p2();
    }

    //Metodo para que el Thread de recuperación de 1 historico pueda guardar el dato dentro de esta clase
    private void setHistorical(List<Historical> dataset){
        histoData = dataset.get(0);
        disp1();
    }


    private void startSaveActivity() {
        Intent i = new Intent(getActivity(), HistoricoActivitySave.class);
        startActivityForResult(i, REQUEST_SAVE_RESULT);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (REQUEST_SAVE_RESULT == requestCode){
            if (Activity.RESULT_OK == resultCode){
                Log.i("ASD", "Es el request code y el result code");
            }
            else {
                Log.i("ASD", "Es el request code pero no result code");
                Snackbar.make(getView(), getString(R.string.Historical_save_cancel_msg), Snackbar.LENGTH_SHORT).show();
            }
        }
        else {
            Log.i("ASD", "No es el request code");
            super.onActivityResult(requestCode, resultCode, data);
        }
    }






    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historico, container, false);
        EditText_city = root.findViewById(R.id.et_city);
        spinner1 = root.findViewById(R.id.spinner1);
        spinner2 = root.findViewById(R.id.spinner2);
        button1 = root.findViewById(R.id.button1);
        button2 = root.findViewById(R.id.button2);
        buttonAdd = root.findViewById(R.id.addHistorical);

        //Cargar el spinner de CountryCodes
        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,countryCodes);
        spinner1.setAdapter(spinnerAdapter);
        spinner1.setSelection(208);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Historico", "Se ha pulsado el boton de buscar historico segun el nombre de la ciudad");
                act1(v); //Busqueda del historico de "ayer" de la ciudad (indicada en city) y se guarda en la base de datos
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Historico", "Se ha pulsado el boton de buscar historico de la localización guardada");
                act2(); //Busqueda del historico de "ayer" de la ciudad guardada (indicada en spinner2) y se guarda en la base de datos
            }
        });


        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Historico", "Se ha pulsado el boton de añadir un nuevo historico");
                act3(v);
            }
        });



        //Se carga la lista de ubicaciones guardadas
        loadItems();


        //Cargar el RecyclerView
        Log.i("Historico","Se cargan carga el recycler view");
        recyclerView = (RecyclerView) root.findViewById(R.id.listHistorical);
        recyclerView.setHasFixedSize(true); //esto hay que ponerlo siempre (no se pa que)
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //Por defecto se muestran los historicos guardados de la ubicacion predeterminada (ajuste 2 de las preferencias)
        /*HistoricalAdapter adapter = new HistoricalAdapter(new ArrayList<>());

        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                adapter::swap,39.47649,-6.37224,1604361600
        ));
        recyclerView.setAdapter(adapter);*/


        Log.i("Historico", "Se ha cargado el fragment del historico");
        return root;
    }
}