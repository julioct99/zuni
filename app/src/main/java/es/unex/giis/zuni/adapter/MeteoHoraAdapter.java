package es.unex.giis.zuni.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.porhoras.Hourly;

public class MeteoHoraAdapter extends RecyclerView.Adapter<MeteoHoraAdapter.MyViewHolder> {
    private List<Hourly> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

       public ImageView image;
        public TextView hora;

        public TextView descripcion;
        public TextView humedad;
        public TextView viento;

        public TextView st;
        public TextView temp;
        public View mView;

        public Hourly mItem;

        public MyViewHolder(View v) {
            super(v);
            mView=v;

            image = v.findViewById(R.id.h_image);
            hora = v.findViewById(R.id.h_hora);

            descripcion = v.findViewById(R.id.h_descripcion);
            humedad = v.findViewById(R.id.h_humedad);
            viento = v.findViewById(R.id.h_viento);

            st = v.findViewById(R.id.h_st);
            temp = v.findViewById(R.id.h_temp);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MeteoHoraAdapter(ArrayList<Hourly> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public MeteoHoraAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_meteohoras, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);

        switch (holder.mItem.getWeather().get(0).getMain()){
            case "Thunderstorm":
                holder.image.setImageResource(R.drawable.tormenta);
                break;
            case "Drizzle":
            case "Rain":
                holder.image.setImageResource(R.drawable.lluvia);
                break;
            case "Snow":
                holder.image.setImageResource(R.drawable.nieve);
                break;
            case "Clear":
                holder.image.setImageResource(R.drawable.sol);
                break;
            case "Clouds":
                holder.image.setImageResource(R.drawable.nube);
                break;
        }

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("0",simbolos);
        DecimalFormat df1 = new DecimalFormat("0.0",simbolos);
        DecimalFormat df2 = new DecimalFormat("0.00",simbolos);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(holder.mItem.getDt()*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY HH:mm");
        String dateString = sdf.format(c.getTime());


        holder.hora.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH)
                .format(new Date((holder.mItem.getDt()) * 1000l)));

        //Timestamp ts = new Timestamp((holder.mItem.getDt()*1000));
        //holder.hora.setText(ts.toString());

        String input = holder.mItem.getWeather().get(0).getDescription();
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        holder.descripcion.setText(output);

        holder.humedad.setText("PoP: ".concat(df.format(holder.mItem.getPop())).concat("%"));

        holder.viento.setText("WS: ".concat(df2.format(holder.mItem.getWindSpeed())).concat(" m/s"));
        holder.st.setText("T: ".concat(df2.format(holder.mItem.getFeelsLike())).concat(" ºC"));
        holder.temp.setText("FL:".concat(df2.format(holder.mItem.getTemp())).concat(" ºC"));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void swap(List<Hourly> dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }
}

/*
package es.unex.giis.zuni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

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
 */