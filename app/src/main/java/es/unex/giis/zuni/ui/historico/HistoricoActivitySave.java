package es.unex.giis.zuni.ui.historico;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.HistoricalAdapter;
import es.unex.giis.zuni.adapter.HistoricalAdapterListener;
import es.unex.giis.zuni.historical.Historical;
import es.unex.giis.zuni.historical.HistoricalMinimal;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.HistoricalNetworkLoaderRunnable;

public class HistoricoActivitySave extends AppCompatActivity {
    private TextView tv_city;
    private Button button1, button2;
    //private Button goBackButton;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private HistoricalAdapter adapter;

    //Variable del Intent de invocacion
    private Intent myIntent;

    //Variable del historico
    private Historical histoData;


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

        setResult(RESULT_CANCELED);
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_save);
        tv_city = findViewById(R.id.tv_city);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        //goBackButton = findViewById(R.id.GoBackButton);

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

        /*goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Historico", "Se ha pulsado el boton de goBack");
                setResult(RESULT_CANCELED);
                finish();
            }
        });*/


        //Cargar el dato desde el Intent
        String cityname, countrycode;

        myIntent = getIntent();
        //histoData = new Historical(myIntent);
        HistoricalMinimal hm = (HistoricalMinimal) myIntent.getSerializableExtra("data");
        histoData = hm.convertIntoHistorical();

        cityname = myIntent.getStringExtra("cityname");
        countrycode = myIntent.getStringExtra("countrycode");

        String name = cityname + ", " + countrycode;
        tv_city.setText(name);

        //Cargar el RecyclerView
        recyclerView = findViewById(R.id.listHistorical);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(layoutManager);

        //Mostrar el dato en el RecyclerView
        ArrayList<Historical> histoList = new ArrayList<Historical>();
        histoList.add(histoData);
        HistoricalAdapter adapter = new HistoricalAdapter(histoList, new HistoricalAdapterListener() {
            @Override
            public void imageButtonViewOnClick(View v, int position) {
                //Todo Insertar codigo aqui
                //Aqui no se debe hacer nada porque el element no esta en la base de datos (sino que se va a añadir a ella)
                Snackbar.make(v, getText(R.string.Historical_remove_err_msg), Snackbar.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);


        Log.i("Historico Save", "Se ha cargado el Activity de guardar un historico");
    }
}