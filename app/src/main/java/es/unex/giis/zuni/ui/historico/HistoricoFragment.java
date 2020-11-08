package es.unex.giis.zuni.ui.historico;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.adapter.HistoricalAdapter;
import es.unex.giis.zuni.openweather.AppExecutors;
import es.unex.giis.zuni.openweather.HistoricalNetworkLoaderRunnable;

public class HistoricoFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private HistoricalAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_historico, container, false);
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

        return root;
    }
}