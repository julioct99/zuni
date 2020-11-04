package es.unex.giis.zuni.ui.previsiones;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.countrycodes.CountryCode;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;

public class PrevisionesFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private PrevisionesViewModel previsionesViewModel;
    private EditText city;
    private DailyAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinner, spinner2;
    private Button button, button2;
    private static String seleccion;
    private double lat, lon;

    public void act2(){
        seleccion = city.getText().toString();
        adapter=new DailyAdapter(new ArrayList<>());
        AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                adapter::swap,seleccion,spinner2.getSelectedItem().toString().substring(0,2)
        ));
        recyclerView.setAdapter(adapter);
    }

    public void act1(){
        seleccion = spinner.getSelectedItem().toString();
        adapter=new DailyAdapter(new ArrayList<>());
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
        AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                adapter::swap,lat,lon
        ));
        recyclerView.setAdapter(adapter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        previsionesViewModel = ViewModelProviders.of(this).get(PrevisionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_previsiones, container, false);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        spinner2 = (Spinner) root.findViewById(R.id.spinner2);

        button = (Button) root.findViewById(R.id.button);
        button2 = (Button) root.findViewById(R.id.button2);
        city = (EditText) root.findViewById(R.id.et_city);
        recyclerView = (RecyclerView) root.findViewById(R.id.listDaily);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);

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

        return root;
    }
}