package es.unex.giis.zuni.ui.detalles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import es.unex.giis.zuni.MeteoDia;
import es.unex.giis.zuni.R;
import es.unex.giis.zuni.ui.eventos.SlideshowViewModel;

public class DetallesFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detalles, container, false);

        final MeteoDia dia = new MeteoDia();
        dia.setDatosEjemplo();

        TextView condicion, descripcion, min, max, viento, humedad, presion, sdia;
        ImageView image;
        condicion=root.findViewById(R.id.condicion);

        descripcion = root.findViewById(R.id.descripcion);
        min=root.findViewById(R.id.min);
        max=root.findViewById(R.id.max);
        viento=root.findViewById(R.id.viento);
        humedad=root.findViewById(R.id.humedad);
        presion=root.findViewById(R.id.presion);
        image=root.findViewById(R.id.image);


        condicion.setText(dia.getCondicion());

        descripcion.setText(dia.getDescripcion());
        min.setText("Min: ".concat(Float.toString(dia.getMin()).concat(" ºC")));
        max.setText("Max: ".concat(Float.toString(dia.getMax()).concat(" ºC")));


        viento.setText("Viento: ".concat(Float.toString(dia.getViento())));
        humedad.setText("Humedad: ".concat(Float.toString(dia.getHumedad())));
        presion.setText("Presion: ".concat(Float.toString(dia.getPresion())));


        switch (dia.getCondicion()){
            case "Thunderstorm":
                image.setImageResource(R.drawable.tormenta);
                break;
            case "Drizzle":
            case "Rain":
                image.setImageResource(R.drawable.lluvia);
                break;
            case "Snow":
                image.setImageResource(R.drawable.nieve);
                break;
            case "Clear":
                image.setImageResource(R.drawable.sol);
                break;
            case "Clouds":
                image.setImageResource(R.drawable.nube);
                break;
        }


/*
        final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        return root;
    }
}