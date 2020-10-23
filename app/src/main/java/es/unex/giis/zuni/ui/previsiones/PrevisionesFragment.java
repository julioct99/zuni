package es.unex.giis.zuni.ui.previsiones;

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
import es.unex.giis.zuni.adapter.DailyAdapter;
import es.unex.giis.zuni.daily.Daily;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.DailyNetworkLoaderRunnable;

public class PrevisionesFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    private PrevisionesViewModel previsionesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        previsionesViewModel = ViewModelProviders.of(this).get(PrevisionesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_previsiones, container, false);


        RecyclerView recyclerView;
        DailyAdapter adapter;
        recyclerView = (RecyclerView) root.findViewById(R.id.listDaily);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new DailyAdapter(new ArrayList<Daily>());

        AppExecutors.getInstance().networkIO().execute(new DailyNetworkLoaderRunnable(
                adapter::swap,38.59758,-5.43701
        ));

        recyclerView.setAdapter(adapter);

        return root;
    }
}