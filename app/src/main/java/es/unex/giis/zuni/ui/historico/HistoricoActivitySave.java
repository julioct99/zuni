package es.unex.giis.zuni.ui.historico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.HistoricalAdapter;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.HistoricalNetworkLoaderRunnable;

public class HistoricoActivitySave extends AppCompatActivity {
    private Button button1, button2, goBackButton;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private HistoricalAdapter adapter;


    public void act1(View v){
        //Perform saving
        String cityname = "";

        Log.i("Historico", "Se ha pulsado el boto de guardar el historico");
        Snackbar.make(v, getString(R.string.Historical_save_msg) + " " + cityname, Snackbar.LENGTH_SHORT).show();

        Intent i = new Intent();
        setResult(RESULT_OK, i);
        finish();
    }



    public void act2(View v){
        //Abort saving
        Log.i("Historico", "Se ha pulsado el boton de cancelar");
        //Snackbar.make(v, "Ir hacia atras...", Snackbar.LENGTH_SHORT).show();

        //HistoricoFragment fragment = new HistoricoFragment();
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).addToBackStack(null).commit();
        //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

        setResult(RESULT_CANCELED);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_save);
        //View root = inflater.inflate(R.layout.fragment_historico_save, container, false);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        goBackButton = findViewById(R.id.GoBackButton);

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
                act2(v); //Abortar (Ir hacia atrás)
            }
        });

        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Historico", "Se ha pulsado el boton de goBack");
                setResult(RESULT_CANCELED);
                finish();
            }
        });



        //Por defecto se muestran los historicos guardados de la ubicacion predeterminada (ajuste 2 de las preferencias)

        /*Log.e("HOLAAAAAAAAAAAA","3");
        recyclerView = (RecyclerView) findViewById(R.id.listHistorical);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        HistoricalAdapter adapter=new HistoricalAdapter(new ArrayList<>());

        AppExecutors.getInstance().networkIO().execute(new HistoricalNetworkLoaderRunnable(
                adapter::swap,39.47649,-6.37224,1604361600
        ));
        recyclerView.setAdapter(adapter);*/


        Log.i("Historico", "Se ha cargado el Activity de guardar un historico");
    }
}