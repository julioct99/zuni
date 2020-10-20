package es.unex.giis.zuni.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import es.unex.giis.zuni.MeteoHora;
import es.unex.giis.zuni.R;

public class MeteoHoraAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MeteoHora> models;

    public MeteoHoraAdapter(Context context, ArrayList<MeteoHora> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return models.get(i).getHora();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if(view==null){
            view = View.inflate(context, R.layout.list_meteohoras,null);
        }

        ImageView image = view.findViewById(R.id.h_image);
        TextView hora = view.findViewById(R.id.h_hora);
        TextView condicion = view.findViewById(R.id.h_condicion);
        TextView descripcion = view.findViewById(R.id.h_descripcion);

        MeteoHora mh = models.get(i);
        switch (mh.getCondicion()){
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

        hora.setText(Integer.toString(mh.getHora()).concat(":00"));
        condicion.setText(mh.getCondicion());
        descripcion.setText(mh.getDescripcion());

        return view;
    }
}
