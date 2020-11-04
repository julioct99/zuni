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
import es.unex.giis.zuni.api.current.Current;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.CurrentNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.MeteoHoraNetworkLoaderRunnable;
import es.unex.giis.zuni.api.porhoras.Hourly;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

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
    public void act2(){
        seleccion = city.getText().toString();

        // ----------------------------- CURRENT -----------------------------

        adapter1=new CurrentAdapter(new ArrayList<Current>());
        AppExecutors.getInstance().networkIO().execute(new CurrentNetworkLoaderRunnable(
                adapter1::swap,seleccion,spinner2.getSelectedItem().toString().substring(0,2)
        ));
        recyclerView1.setAdapter(adapter1);

        // ----------------------------- HORAS -----------------------------

        adapter=new MeteoHoraAdapter(new ArrayList<Hourly>());
        recyclerView.setAdapter(adapter);

        textViewError.setVisibility(VISIBLE);
        textViewError.setTextSize(16f);
        textViewError.setText("Debido a restricciones de la api, no podemos consultar el tiempo por horas introduciendo un nombre." +
                "Debes ir a la pestaña de lista de ubicaciones y añadirla desde ahí. Gracias!");

    }

    public void act1(){
        seleccion = spinner.getSelectedItem().toString();

        switch(seleccion){
            case "Monterrubio de la Serena":
                lat=38.59758;
                lon=-5.43701;
                break;
            case "Caceres":
                lat=39.47649;
                lon=-6.37224;
                break;
            case "Nueva York":
                lat=40.71427;
                lon=-74.00597;
                break;
            default:
                lat=0;
                lon=0;
        }
        // ----------------------------- CURRENT -----------------------------

        adapter1=new CurrentAdapter(new ArrayList<Current>());
        AppExecutors.getInstance().networkIO().execute(new CurrentNetworkLoaderRunnable(
                adapter1::swap,lat,lon
        ));
        recyclerView1.setAdapter(adapter1);

        // ----------------------------- HORAS -----------------------------
        textViewError.setVisibility(INVISIBLE);
        textViewError.setTextSize(0f);
        textViewError.setText("");
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

        textViewError = root.findViewById(R.id.texterror);

        recyclerView = (RecyclerView) root.findViewById(R.id.list_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        city = (EditText) root.findViewById(R.id.et_city);
        recyclerView1 = (RecyclerView) root.findViewById(R.id.current);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(root.getContext());
        recyclerView1.setLayoutManager(layoutManager1);


        String [] opciones = {"Monterrubio de la Serena","Caceres","Nueva York"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);

        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,countryCodes);

        spinner2.setAdapter(spinnerAdapter2);
        spinner2.setSelection(208);

        act1();
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
        return root;
    }
}
