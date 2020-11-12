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

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.HistoricalAdapter;
import es.unex.giis.zuni.countrycodes.CountryCode;
import es.unex.giis.zuni.geocode.GeoCode;
import es.unex.giis.zuni.historical.Historical;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.GeoCodeNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.HistoricalNetworkLoaderRunnable;
import es.unex.giis.zuni.ui.detalles.DetallesFragment;

@SuppressWarnings("ALL")
public class HistoricoFragment extends Fragment {
    private static final int REQUEST_SAVE_RESULT = 1;

    private EditText EditText_city;
    private Spinner spinner1, spinner2;
    private Button button1, button2;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    //Variables for GeoCode API
    private double lon, lat;
    private String geoName;

    //Variables for OpenWheather API
    private Historical histoData;

    private HistoricalAdapter adapter;


    private void act1(View v){
        //Get the data from the view
        String cityname = EditText_city.getText().toString().trim();
        String countrycode = spinner1.getSelectedItem().toString().substring(0,2);

        Log.i("Historico", "Se ha pulsado el boto de busqueda de \"" + cityname + "\" en el country \"" + countrycode + "\"");

        if (cityname.equals("")){
            Snackbar.make(v, getString(R.string.Historical_save_err1_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
            return;
        }


        //Get coordinates from the GeoCode API
        AppExecutors.getInstance().networkIO().execute(new GeoCodeNetworkLoaderRunnable(
                this::setUbicacion,cityname.replace(" ","%20"), countrycode
        ));


        //Get the historical from the API
        adapter = new HistoricalAdapter(new ArrayList<Historical>());
        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                this::setHistorical,39.47649,-6.37224,1604361600
        ));


        if (histoData == null){
            Snackbar.make(v, getString(R.string.Historical_save_err1_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
            return;
        }


        //Se invoca la nueva pantalla y se añade el dato
        Intent i = new Intent(getActivity(), HistoricoActivitySave.class);
        //i.putExtra("Historico1", histoData); //TODO arreglar esto (hacer un empaquetador de un Historico en un Intent)
        startActivityForResult(i, REQUEST_SAVE_RESULT);


        Log.i("Historico", "Se ha pulsado el boto de guardar historico por nombre");
        Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
    }



    private void act2(View v){
        //Get the data from the view
        //String seleccion = spinner2.getSelectedItem().toString();



        Log.i("Historico", "Se ha pulsado el boto de guardar historico de la localización guardada");
        //Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();


        startSaveActivity();
    }


    private void setUbicacion(GeoCode geoCode) {
        lat = Double.parseDouble(geoCode.getLatt());
        lon = Double.parseDouble(geoCode.getLongt());
        geoName = geoCode.getStandard().getCity();
    }

    //Metodo para que el Thread de recuperación de 1 historico pueda guardar el dato dentro de esta clase
    private void setHistorical(List<Historical> dataset){
        histoData = dataset.get(0);
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

        JsonReader reader = new JsonReader(new InputStreamReader(getResources().openRawResource(R.raw.country_codes)));
        List<CountryCode> countryCodes = Arrays.asList(new Gson().fromJson(reader, CountryCode[].class));
        ArrayAdapter<CountryCode> spinnerAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,countryCodes);
        spinner1.setAdapter(spinnerAdapter);
        spinner1.setSelection(208);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act1(v); //Busqueda del historico de "ayer" de la ciudad (indicada en city) y se guarda en la base de datos
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act2(v); //Busqueda del historico de "ayer" de la ciudad guardada (indicada en spinner2) y se guarda en la base de datos
            }
        });



        //Por defecto se muestran los historicos guardados de la ubicacion predeterminada (ajuste 2 de las preferencias)

        Log.i("Historico","Se cargan los historicos guardados. Se meten en el recycler view");
        recyclerView = (RecyclerView) root.findViewById(R.id.listHistorical);
        recyclerView.setHasFixedSize(true); //esto hay que ponerlo siempre (no se pa que)
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        HistoricalAdapter adapter=new HistoricalAdapter(new ArrayList<>());

        //Original lognt:39.47649  lat:-6.37224
        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                adapter::swap,39.47649,-6.37224,1604361600
        ));
        recyclerView.setAdapter(adapter);


        Log.i("Historico", "Se ha cargado el fragment del historico");
        return root;
    }
}