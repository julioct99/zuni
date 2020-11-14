package es.unex.giis.zuni.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.historical.Historical;
import es.unex.giis.zuni.historical.Hourly;

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.MyViewHolder> {
    final Integer KelvinOffset = 273;
    private List<Historical> mDataset;

    //Variable for the button Listener
    private final HistoricalAdapterListener listener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView image_condition, image_sunrise, image_sunset;
        public TextView tv_day, tv_description, tv_sunrise, tv_sunset, tv_max, tv_min, tv_humidity, tv_ws;
        public ImageButton bt_erase;
        public View mView;

        public Historical mItem;

        private WeakReference<HistoricalAdapterListener> listenerRef;

        public MyViewHolder(View v, HistoricalAdapterListener listener) {
            super(v);

            listenerRef = new WeakReference<>(listener);

            mView=v;

            image_condition = v.findViewById(R.id.image_condition);
            image_sunrise = v.findViewById(R.id.image_sunrise);
            image_sunset = v.findViewById(R.id.image_sunset);

            tv_day = v.findViewById(R.id.tv_day);
            tv_description = v.findViewById(R.id.tv_description);
            tv_sunrise = v.findViewById(R.id.tv_sunrise);
            tv_sunset = v.findViewById(R.id.tv_sunset);
            tv_max = v.findViewById(R.id.textMax);
            tv_min = v.findViewById(R.id.textMin);
            tv_humidity = v.findViewById(R.id.textH);
            tv_ws = v.findViewById(R.id.textWS);

            bt_erase = v.findViewById(R.id.bt_erase);
            bt_erase.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //Accion a ejecutar en el elemento
            //Snackbar.make(v, "Esto es una prueba del boton de borrado", Snackbar.LENGTH_SHORT).show();

            //Se debe vincular mediante una interface al fragment del Historico. Mirar proceso en:
            //https://stackoverflow.com/questions/30284067/handle-button-click-inside-a-row-in-recyclerview
            listenerRef.get().imageButtonViewOnClick(v, getAdapterPosition());
        }



    }

    public HistoricalAdapter(ArrayList<Historical> myDataset, HistoricalAdapterListener listener) {
        mDataset = myDataset;
        this.listener = listener;
    }


    @Override
    public HistoricalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_historical, parent, false);
        return new MyViewHolder(v, listener);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);


        //holder.tv_day.setText("TEST");
        holder.tv_day.setText(new SimpleDateFormat("E, dd MMM yyyy", Locale.ENGLISH).format(new Date((holder.mItem.getCurrent().getDt()) * 1000l)));
        holder.tv_description.setText(holder.mItem.getCurrent().getWeather().get(0).getDescription());


        //Esto es para hacer el cambio de Timestamp a HH:MM:SS para que sea bonito
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(((holder.mItem.getCurrent().getSunrise())*1000l));
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        String dateSunrise = sdf1.format(c1.getTime());
        holder.tv_sunrise.setText(dateSunrise);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(((holder.mItem.getCurrent().getSunset())*1000l));
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String dateSunset = sdf2.format(c2.getTime());
        holder.tv_sunset.setText(dateSunset);

        //Calculate max and min Temperature
        Double tmpMax = null;
        Double tmpMin = null;

        for(Hourly i : holder.mItem.getHourly()){
            if (tmpMax == null)
                tmpMax = i.getTemp();
            else if (tmpMax < i.getTemp())
                tmpMax = i.getTemp();

            if (tmpMin == null)
                tmpMin = i.getTemp();
            else if (tmpMin > i.getTemp())
                tmpMin = i.getTemp();
        }

        holder.tv_max.setText(new DecimalFormat("###.##").format(tmpMax - KelvinOffset).concat(" ºC"));
        holder.tv_min.setText(new DecimalFormat("###.##").format(tmpMin - KelvinOffset).concat(" ºC"));

        holder.tv_humidity.setText(Integer.toString(holder.mItem.getCurrent().getHumidity()).concat("%"));
        holder.tv_ws.setText(new DecimalFormat("###.##").format(holder.mItem.getCurrent().getWindSpeed()).concat(" m/s"));


        if(holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("thunder") || holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("storm") )
            holder.image_condition.setImageResource(R.drawable.tormenta);
        else if(holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("drizzle") || holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("rain") )
            holder.image_condition.setImageResource(R.drawable.lluvia);
        else if(holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("snow"))
            holder.image_condition.setImageResource(R.drawable.nieve);
        else if(holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("clear"))
            holder.image_condition.setImageResource(R.drawable.sol);
        else if(holder.mItem.getCurrent().getWeather().get(0).getMain().toLowerCase().contains("clouds"))
            holder.image_condition.setImageResource(R.drawable.nube);
        else
            holder.image_condition.setImageResource(R.drawable.sol);

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap(List<Historical> dataset){
        Log.i("Historical","HistoricalAdapter Swapped");
        mDataset = dataset;
        notifyDataSetChanged();
    }
}