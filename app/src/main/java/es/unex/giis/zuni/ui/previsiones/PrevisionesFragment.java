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

import java.util.ArrayList;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.daily.Datum;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;

public class PrevisionesFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private PrevisionesViewModel previsionesViewModel;
    private EditText city;
    private DailyAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinner;
    private Button button, button2;
    private static String seleccion;
    private double lat, lon;

    public void act2(){
        seleccion = city.getText().toString();
        AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                adapter::swap,seleccion
        ));
        recyclerView.setAdapter(adapter);
    }

    public void act1(){
        seleccion = spinner.getSelectedItem().toString();
        adapter=new DailyAdapter(new ArrayList<Datum>());

        switch(seleccion){
            case "Monterrubio de la Serena":
                lat=38.59758;
                lon=-5.43701;
                break;
            case "Caceres":
                lat=39.47528;
                lon=-6.3724;
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
        button = (Button) root.findViewById(R.id.button);
        button2 = (Button) root.findViewById(R.id.button2);
        city = (EditText) root.findViewById(R.id.et_city);
        recyclerView = (RecyclerView) root.findViewById(R.id.listDaily);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DailyAdapter(new ArrayList<Datum>());

        String [] opciones = {"Monterrubio de la Serena","Caceres"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,opciones);
        spinner.setAdapter(spinnerAdapter);

        spinner.setSelection(0);
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