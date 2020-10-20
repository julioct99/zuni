package es.unex.giis.zuni.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
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
        TextView humedad = view.findViewById(R.id.h_humedad);
        TextView viento = view.findViewById(R.id.h_viento);
        TextView presion = view.findViewById(R.id.h_presion);
        TextView st = view.findViewById(R.id.h_st);
        TextView temp = view.findViewById(R.id.h_temp);

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

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("0",simbolos);
        DecimalFormat df1 = new DecimalFormat("0.0",simbolos);
        DecimalFormat df2 = new DecimalFormat("0.00",simbolos);

        hora.setText(Integer.toString(mh.getHora()).concat(":00"));

        condicion.setText(mh.getCondicion());
        descripcion.setText(mh.getDescripcion());

        humedad.setText("H: ".concat(df.format(mh.getHumedad())).concat("%"));
        presion.setText("P: ".concat(df1.format(mh.getPresion())).concat(" hPa"));
        viento.setText("WS: ".concat(df2.format(mh.getViento())).concat(" m/s"));
        st.setText("T: ".concat(df2.format(mh.getSensacion_termica())).concat(" ºC"));
        temp.setText("FL:".concat(df2.format(mh.getTemperatura())).concat(" ºC"));

        return view;
    }
}
