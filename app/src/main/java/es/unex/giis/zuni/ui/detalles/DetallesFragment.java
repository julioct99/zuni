package es.unex.giis.zuni.ui.detalles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.CurrentAdapter;
import es.unex.giis.zuni.adapter.MeteoHoraAdapter;
import es.unex.giis.zuni.current.Current;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.CurrentNetworkLoaderRunnable;
import es.unex.giis.zuni.openweather.MeteoHoraNetworkLoaderRunnable;
import es.unex.giis.zuni.porhoras.Hourly;

public class DetallesFragment extends Fragment {

    private DetallesViewModel detallesshowViewModel;
    private RecyclerView.LayoutManager layoutManager, layoutManager1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detallesshowViewModel =
                ViewModelProviders.of(this).get(DetallesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_detalles, container, false);

        // ----------------------------- CURRENT -----------------------------

        CurrentAdapter currentAdapter = new CurrentAdapter(new ArrayList<Current>());
        RecyclerView recyclerView1;
        CurrentAdapter adapter1;
        recyclerView1 = (RecyclerView) root.findViewById(R.id.current);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(root.getContext());
        recyclerView1.setLayoutManager(layoutManager1);
        adapter1=new CurrentAdapter(new ArrayList<Current>());
        AppExecutors.getInstance().networkIO().execute(new CurrentNetworkLoaderRunnable(
                adapter1::swap,38.59758,-5.43701
        ));
        recyclerView1.setAdapter(adapter1);

        // ----------------------------- HORAS -----------------------------

        RecyclerView recyclerView;
        MeteoHoraAdapter adapter;
        recyclerView = (RecyclerView) root.findViewById(R.id.list_items);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MeteoHoraAdapter(new ArrayList<Hourly>());
        AppExecutors.getInstance().networkIO().execute(new MeteoHoraNetworkLoaderRunnable(
                adapter::swap,38.59758,-5.43701
        ));
        recyclerView.setAdapter(adapter);



        return root;



    }
}
