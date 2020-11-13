package es.unex.giis.zuni.ui.detalles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.CurrentAdapter;
import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.countrycodes.CountryCode;
import es.unex.giis.zuni.geocode.GeoCode;
import es.unex.giis.zuni.api.current.Current;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.CurrentNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.GeoCodeNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.MeteoHoraNetworkLoaderRunnable;
import es.unex.giis.zuni.api.porhoras.Hourly;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;

public class DetallesFragment extends Fragment {
    private RecyclerView recyclerView, recyclerView1;
    private RecyclerView.LayoutManager layoutManager, layoutManager1;
    private EditText city;
    MeteoHoraAdapter adapter;
    CurrentAdapter adapter1;
    private Spinner spinner, spinner2;
    private Button button, button2;
    private static String seleccion;
    private double lat, lon;
    private TextView textViewError;
    static List<Ubicacion> ubis = null;
    public void cargarSpinner(){
        ArrayAdapter<Ubicacion> spinnerAdapter =
                new ArrayAdapter(getContext(),  android.R.layout.simple_spinner_dropdown_item, ubis);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        // act1();
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

    public void act2(){
        seleccion = city.getText().toString();

        // ----------------------------- CURRENT -----------------------------


        adapter1=new CurrentAdapter(new ArrayList<Current>());
        AppExecutors.getInstance().networkIO().execute(new CurrentNetworkLoaderRunnable(
                adapter1::swap,seleccion,spinner2.getSelectedItem().toString().substring(0,2)
        ));
        recyclerView1.setAdapter(adapter1);

        // ----------------------------- HORAS -----------------------------

        AppExecutors.getInstance().networkIO().execute(new GeoCodeNetworkLoaderRunnable(
                this::act3,seleccion.replace(" ","%20"),spinner2.getSelectedItem().toString().substring(0,2)
        ));
    }

    private void act3(GeoCode geoCode) {

        adapter=new MeteoHoraAdapter(new ArrayList<Hourly>());
        AppExecutors.getInstance().networkIO().execute(new MeteoHoraNetworkLoaderRunnable(
                adapter::swap,Double.parseDouble(geoCode.getLatt()),Double.parseDouble(geoCode.getLongt())
        ));
        recyclerView.setAdapter(adapter);

    }

    public void act1(){

        Ubicacion seleccionada = (Ubicacion) spinner.getSelectedItem();
        seleccion = seleccionada.getUbicacion();

        lat=seleccionada.getLat();
        lon=seleccionada.getLon();
        // ----------------------------- CURRENT -----------------------------


        adapter1=new CurrentAdapter(new ArrayList<Current>());
        AppExecutors.getInstance().networkIO().execute(new CurrentNetworkLoaderRunnable(
                adapter1::swap,lat,lon
        ));
        recyclerView1.setAdapter(adapter1);

        // ----------------------------- HORAS -----------------------------

        adapter=new MeteoHoraAdapter(new ArrayList<Hourly>());
        AppExecutors.getInstance().networkIO().execute(new MeteoHoraNetworkLoaderRunnable(
                adapter::swap,lat,lon
        ));
        recyclerView.setAdapter(adapter);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detalles, container, false);

        spinner = (Spinner) root.findViewById(R.id.spinner);
        spinner2 = (Spinner) root.findViewById(R.id.spinner2);

        button = (Button) root.findViewById(R.id.button);
        button2 = (Button) root.findViewById(R.id.button2);


        recyclerView = (RecyclerView) root.findViewById(R.id.list_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        city = (EditText) root.findViewById(R.id.et_city);
        recyclerView1 = (RecyclerView) root.findViewById(R.id.current);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(root.getContext());
        recyclerView1.setLayoutManager(layoutManager1);

        loadItems();

        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,countryCodes);

        spinner2.setAdapter(spinnerAdapter2);
        spinner2.setSelection(208);

        //if(ubis!=null && ubis.size()>0) {
        //    act1();
        //}
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act1();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act2();
            }
        });




        /*
        // ----------------------------- CURRENT -----------------------------

        adapter1=new CurrentAdapter(new ArrayList<Current>());
        AppExecutors.getInstance().networkIO().execute(new CurrentNetworkLoaderRunnable(
                adapter1::swap,lat,lon
        ));
        recyclerView1.setAdapter(adapter1);

        // ----------------------------- HORAS -----------------------------

        adapter=new MeteoHoraAdapter(new ArrayList<Hourly>());
        AppExecutors.getInstance().networkIO().execute(new MeteoHoraNetworkLoaderRunnable(
                adapter::swap,lat,lon
        ));
        recyclerView.setAdapter(adapter);

        */

        return root;
    }
}
