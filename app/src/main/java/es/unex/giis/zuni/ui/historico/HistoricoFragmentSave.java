package es.unex.giis.zuni.ui.historico;

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
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.HistoricalNetworkLoaderRunnable;
import es.unex.giis.zuni.ui.detalles.DetallesFragment;

public class HistoricoFragmentSave extends Fragment {
    private Button button1, button2;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private HistoricalAdapter adapter;


    public void act1(View v){
        String cityname = "";

        if (cityname.equals("")){
            Snackbar.make(v, getString(R.string.Historical_save_err1_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
            return;
        }

        Log.i("Historico", "Se ha pulsado el boto de guardar el historico");
        Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();
    }



    public void act2(View v){
        Log.i("Historico", "Se ha pulsado el boton de ir hacia atras");
        Snackbar.make(v, "Ir hacia atras...", Snackbar.LENGTH_SHORT).show();

        HistoricoFragment fragment = new HistoricoFragment();
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historico_save, container, false);
        button1 = root.findViewById(R.id.button1);
        button2 = root.findViewById(R.id.button2);

        //Snackbar.make(root, "Surprise :D", Snackbar.LENGTH_SHORT).show();

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act1(v); //Guarda el historico buscado en la base de datos de historicos
            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                act2(v); //Ir hacia atrás
            }
        });



        //Por defecto se muestran los historicos guardados de la ubicacion predeterminada (ajuste 2 de las preferencias)

        Log.e("HOLAAAAAAAAAAAA","3");
        recyclerView = (RecyclerView) root.findViewById(R.id.listHistorical);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        HistoricalAdapter adapter=new HistoricalAdapter(new ArrayList<>());

        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                adapter::swap,39.47649,-6.37224,1604361600
        ));
        recyclerView.setAdapter(adapter);


        Log.i("Historico", "Se ha cargado el fragment del historico");
        return root;
    }
}