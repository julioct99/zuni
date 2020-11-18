package es.unex.giis.zuni;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.unex.giis.zuni.eventos.Evento;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.ubicaciones.Ubicacion;
import es.unex.giis.zuni.ubicaciones.db.UbicacionDatabase;

public class FavUbicacionActivity extends AppCompatActivity {

    private Ubicacion ubicacion;

    private static final String TAG = "Zuni-FavUbicacion";
    private TextView mUbicacionFav;
    //private boolean mbanderaUbiFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_ubicacion);

        mUbicacionFav = findViewById(R.id.ubicacionfav);

        /* SE OBTIENE LA UBICACION DEL INTENT */
        ubicacion = new Ubicacion(getIntent());
        Ubicacion ubicacionIntent = new Ubicacion(getIntent());

        /* SE OBTIENE LA UBICACION DE LA BD  */
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                ubicacion = UbicacionDatabase.getInstance(FavUbicacionActivity.this).getDao()
                        .getUbicacion(ubicacionIntent.getId());

                AppExecutors.getInstance().mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        /* SE CARGAN LOS ATRIBUTOS DE LA UBICACION EN LOS INPUTS --------------------- */
                        mUbicacionFav.setText(ubicacion.getUbicacion());
                    }
                });
            }
        });

        /* Listener para el boton de CANCEL ----------------------------------------------------- */
        final Button cancelButton = findViewById(R.id.no_favUbicacion);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        /* Listener para el boton de AÑADIR UBICACION FAV ----------------------------------------------------- */
        final Button okeyButton = findViewById(R.id.yes_favUbicacion);
        okeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Obtener los datos de la ubicacion*/
                String ubicacion = mUbicacionFav.getText().toString(); //puede ser chungo cambiar el nombre de ubi
                                                                                                    //mejor seria que en algun lado segun la bandera se escribiera
                /* ACTUALIZAR UBICACIONES COMO NO PREDETERMINADAS */
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        List<Ubicacion> listUbi= UbicacionDatabase.getInstance(FavUbicacionActivity.this)
                                .getDao().getAll();
                        for(int i= 0; i<listUbi.size();i++) {
                            if(listUbi.get(i).getBanderaUbiFav()){
                                listUbi.get(i).setBanderaUbiFav(false);
                                UbicacionDatabase.getInstance(FavUbicacionActivity.this)
                                        .getDao().update(listUbi.get(i));
                            }
                        }

                    }
                });

                /* Empaquetar la ubicacion en un intent */
                Intent data = new Intent();
                //y aqui pongo la bandera a true para que se ha dado click y se pasa con el intent
                Ubicacion.packageIntent(data,ubicacion,null,null,true);

                Ubicacion ubicacionCreada = new Ubicacion(data);

                setResult(RESULT_OK, data);
                finish();
            }
        });
    }


}