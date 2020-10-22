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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.MeteoHoraNetworkLoaderRunnable;
import es.unex.giis.zuni.ui.eventos.EventosViewModel;
import es.unex.giis.zuni.utils.Current;
import es.unex.giis.zuni.utils.Hourly;
import es.unex.giis.zuni.utils.MeteoDia;

public class DetallesFragment extends Fragment {
    private TextView condicion, descripcion, min, max, viento, humedad, presion, sdia;
    private ImageView image;
    private EventosViewModel slideshowViewModel;
    private RecyclerView.LayoutManager layoutManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(EventosViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detalles, container, false);

        final MeteoDia dia = new MeteoDia();
        dia.setDatosEjemplo();


        condicion=root.findViewById(R.id.condicion);

        descripcion = root.findViewById(R.id.descripcion);
        min=root.findViewById(R.id.min);
        max=root.findViewById(R.id.max);
        viento=root.findViewById(R.id.viento);
        humedad=root.findViewById(R.id.humedad);
        presion=root.findViewById(R.id.presion);
        image=root.findViewById(R.id.image);




        // AQUI EMPIEZA LA LISTA DE HORAS

        RecyclerView recyclerView;
        MeteoHoraAdapter adapter;

        recyclerView = (RecyclerView) root.findViewById(R.id.list_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
         Current curr = null;
        adapter=new MeteoHoraAdapter(new ArrayList<Hourly>());


        AppExecutors.getInstance().networkIO().execute(new MeteoHoraNetworkLoaderRunnable(
                adapter::swap,38.59758,-5.43701
        ));
        recyclerView.setAdapter(adapter);

        // AQUI EMPIEZA CURRENT
        /*

*/
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
