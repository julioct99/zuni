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
import es.unex.giis.zuni.historical.db.HistoricalMinimalDatabase;
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
    private ArrayList<Historical> histoList;

    //Variable del Historical Adapter
    private HistoricalAdapter adapter;

    //Variable para la carga de Room de las ubicaciones
    static List<Ubicacion> ubis = null;
    //private static String seleccion;
    ArrayAdapter<Ubicacion> spinnerAdapter;

    //Variable para la actividad que se esta ejecutando
    private int actNum;
    //private boolean isRecovered;


    //********************************* CARGA DEL SPINER DE UBICACIONES *********************************
    public void cargarSpinner(){
        spinnerAdapter = new ArrayAdapter(getContext(),  android.R.layout.simple_spinner_dropdown_item, ubis);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter);
    }


    private void loadSpinnerItems() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ubis = UbicacionDatabase.getInstance(getActivity()).getDao().getAll();
                AppExecutors.getInstance().mainThread().execute(() -> cargarSpinner());
            }
        });
    }




    //********************************* ROOM FUNCTIONS *********************************

    private void loadRoomItems(String cityname) {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                List<HistoricalMinimal> items = HistoricalMinimalDatabase.getInstance(getActivity()).getDao().getHistoricalsByCityname(cityname);
                AppExecutors.getInstance().mainThread().execute(() -> setHistoricalList(items)); // mAdapter.load(items)
            }
        });
    }


    private void saveRoomItem(HistoricalMinimal hm){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                long id = HistoricalMinimalDatabase.getInstance(getActivity()).getDao().insert(hm);
                AppExecutors.getInstance().mainThread().execute(() -> getInsertionNumber(id));
            }
        });
    }


    private void deleteRoomItem(String cityname, Integer dt){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                //TODO modificar esto para borrar un item en la lista de items
                HistoricalMinimalDatabase.getInstance(getActivity()).getDao().delete(cityname, dt);
                AppExecutors.getInstance().mainThread().execute(() -> refreshSearch());
            }
        });
    }



    //********************************* APPEXECUTOR RETURN FUNCTIONS *********************************

    private void setUbicacion(GeoCode geoCode) {
        lat = Double.parseDouble(geoCode.getLatt());
        lon = Double.parseDouble(geoCode.getLongt());
        geoName = geoCode.getStandard().getCity();
    }

    //Metodo para que el Thread de recuperación de 1 historico pueda guardar el dato dentro de esta clase
    private void setHistorical(List<Historical> dataset){
        histoData = dataset.get(0);
        disp1();
    }

    //Metodo para que el Thread de recuperacion de un Array de historicos pueda guardar el dato dentro de esta clase
    private void setHistoricalList(List<HistoricalMinimal> dataset){
        if (dataset.size() == 0){
            Log.i("Historico", "No se ha recuperado ningun dato");
            Snackbar.make(getView(), getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
        }


        histoList = new ArrayList<Historical>();

        for (HistoricalMinimal hm : dataset){
            Historical h;
            h = hm.convertIntoHistorical();

            histoList.add(h);
        }


        if (actNum == 1)
            disp1();
        else if (actNum == 2)
            disp2();

    }


    private void getInsertionNumber(long id){
        Snackbar.make(getView(), getString(R.string.Historical_save_ok_msg), Snackbar.LENGTH_SHORT).show();
        Log.i("Historical", "Se ha insertado un elemento en el historico con el id: " + Long.toString(id));
    }


    private void refreshSearch(){
        if (actNum == 1)
            act1(getView());
        else if (actNum == 2)
            act2();
    }




    //********************************* BUTTON ACTIONS *********************************
    private void act1(View v){
        //Tell that is running the act1
        actNum = 1;

        //Get the data from the view
        cityname = EditText_city.getText().toString().trim();
        countrycode = spinner1.getSelectedItem().toString().substring(0,2);

        Log.i("Historico", "Se ha pulsado el boto de busqueda de \"" + cityname + "\" en el country \"" + countrycode + "\"");

        if (cityname.equals("")){
            Snackbar.make(v, getString(R.string.Historical_search_err1_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
            return;
        }


        //Get data from Room
        loadRoomItems(cityname);
    }




    private void disp1(){
        //Display results from act1 after the Thread 2
        Log.i("Historico", "Se ha recuperado el hisorico por la ubicacion");

        if (histoList == null){
            Snackbar.make(getView(), getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
            Log.e("Historico", "No se ha devuelto un historico valido");
            return;
        }
        Log.i("Historico", "Se ha devuelto un historico valido");


        //Mostrar el dato en el RecyclerView
        HistoricalAdapter adapter = new HistoricalAdapter(histoList, new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                //TODO Implementar borrado del historico
                Snackbar.make(v, "Borrar el historico " + position, Snackbar.LENGTH_SHORT).show();

                Integer dt = histoList.get(position).getCurrent().getDt();

                deleteRoomItem(cityname, dt);
            }
        });
        recyclerView.setAdapter(adapter);



        //isRecovered = true; //Indicar que se ha recuperado el dato


        Log.i("Historico", "Se ha pulsado el boto de buscar historico por nombre en Room");
        Snackbar.make(getView(), getString(R.string.Historical_search_msg1) + " " + cityname, Snackbar.LENGTH_SHORT).show();
        //Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
    }






    private void act2(){
        //Tell taht is running act2
        actNum = 2;


        if (spinner2.getSelectedItem() == null){
            Snackbar.make(getView(), getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
            return;
        }


        //Get the location data from the spiner selected location
        Ubicacion seleccionada = (Ubicacion) spinner2.getSelectedItem();
        cityname = seleccionada.getUbicacion();
        countrycode = "";

        lat=seleccionada.getLat();
        lon=seleccionada.getLon();

        Log.i("Historico", "Se ha pulsado el boto de busqueda de \"" + cityname + "\" en el Spinner");


        //Get data from Room
        loadRoomItems(cityname);
    }


    private void disp2(){
        //Display results from act1 after the Thread 2
        Log.i("Historico", "Se ha recuperado el hisorico por la ubicacion");

        if (histoList == null){
            Snackbar.make(getView(), getString(R.string.Historical_search_err2_msg), Snackbar.LENGTH_SHORT).show();
            Log.e("Historico", "No se ha devuelto un historico valido");
            return;
        }
        Log.i("Historico", "Se ha devuelto un historico valido");


        //Mostrar el dato en el RecyclerView
        HistoricalAdapter adapter = new HistoricalAdapter(histoList, new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                //TODO Implementar borrado del historico
                Snackbar.make(v, "Borrar el historico " + position, Snackbar.LENGTH_SHORT).show();

                Integer dt = histoList.get(position).getCurrent().getDt();

                deleteRoomItem(cityname, dt);
            }
        });
        recyclerView.setAdapter(adapter);


        Log.i("Historico", "Se ha pulsado el boto de buscar historico por nombre en Room");
        Snackbar.make(getView(), getString(R.string.Historical_search_msg1) + " " + cityname, Snackbar.LENGTH_SHORT).show();
    }





    private void act3(View v){
        //Se invoca la nueva pantalla y se añade el dato
        Intent i = new Intent(getActivity(), HistoricoActivitySave.class);
        startActivityForResult(i, REQUEST_SAVE_RESULT);
    }








    //********************************* ACTIVITY RESULT *********************************

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (REQUEST_SAVE_RESULT == requestCode){
            if (Activity.RESULT_OK == resultCode){
                Log.i("ASD", "Es el request code y el result code");
                //TODO Hacer el guardado en room del dato pasado por el Intent

                HistoricalMinimal hm = (HistoricalMinimal) data.getSerializableExtra("Historico");
                saveRoomItem(hm);

                //Snackbar.make(getView(), getString(R.string.Historical_save_msg1) + " " + cityname, Snackbar.LENGTH_SHORT).show();
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
        loadSpinnerItems();


        //Cargar el RecyclerView
        Log.i("Historico","Se carga el recycler view");
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