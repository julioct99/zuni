package es.unex.giis.zuni.ui.previsiones;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.countrycodes.CountryCode;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;

public class PrevisionesFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;

    private EditText city;
    private DailyAdapter adapter;
    private RecyclerView recyclerView;
    private Spinner spinner, spinner2;
    private Button button, button2;
    private static String seleccion;
    private double lat, lon;
    static List<Ubicacion> ubis = null;
    public void act2(){
        seleccion = city.getText().toString();

        if(seleccion != null && !seleccion.trim().equals("")){
            if(seleccion.trim().equals("")){
                adapter=new DailyAdapter(new ArrayList<>());
                AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                        adapter::swap,seleccion,spinner2.getSelectedItem().toString().substring(0,2)
                ));
                recyclerView.setAdapter(adapter);
            }
        }
        else{
            Snackbar.make(getView(), getString(R.string.Historical_search_err1_msg), Snackbar.LENGTH_SHORT).show();
        }
    }

    public void cargarSpinner(){
        ArrayAdapter<Ubicacion> spinnerAdapter =
                new ArrayAdapter(getContext(),  android.R.layout.simple_spinner_dropdown_item, ubis);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        if(ubis!=null && ubis.size()>0){
            boolean enc = false;
            for(int i=0;i<ubis.size() && !enc;i++){
                if(ubis.get(i).getBanderaUbiFav()){
                    enc=true;
                    spinner.setSelection(i);
                    act1();
                }
            }
            if(!enc)
                spinner.setSelection(0);
        }
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

    public void act1(){
        if(ubis!=null && ubis.size()>0){
            Ubicacion seleccionada = (Ubicacion) spinner.getSelectedItem();
            seleccion = seleccionada.getUbicacion();

            lat=seleccionada.getLat();
            lon=seleccionada.getLon();

            adapter=new DailyAdapter(new ArrayList<>());

            AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                    adapter::swap,lat,lon
            ));
            recyclerView.setAdapter(adapter);
        }
        else{
            Snackbar.make(getView(), getString(R.string.Historical_search_err3_msg), Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        loadItems();

        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter2 = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,countryCodes);
        spinner2.setAdapter(spinnerAdapter2);
        spinner2.setSelection(208);



        //if(ubis!=null && ubis.size()>0) {
        //    spinner.setSelection(0);
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

        return root;
    }
}