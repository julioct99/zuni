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
import es.unex.giis.zuni.api.daily.Datum;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.MyViewHolder> {
    private List<Datum> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       public ImageView image_condition, image_sunrise, image_sunset;
       public TextView tv_day, tv_description, tv_sunrise, tv_sunset, tv_max,tv_min,tv_humidity,tv_ws;
        public View mView;

        public Datum mItem;

        public MyViewHolder(View v) {
            super(v);
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
        }
    }

    public DailyAdapter(ArrayList<Datum> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public DailyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_days, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);


        if(holder.mItem.getWeather().getDescription().contains("thunder") || holder.mItem.getWeather().getDescription().contains("storm") )
            holder.image_condition.setImageResource(R.drawable.tormenta);
        else if(holder.mItem.getWeather().getDescription().contains("drizzle") || holder.mItem.getWeather().getDescription().contains("rain") )
            holder.image_condition.setImageResource(R.drawable.lluvia);
        else if(holder.mItem.getWeather().getDescription().contains("Snow") || holder.mItem.getWeather().getDescription().contains("snow"))
                holder.image_condition.setImageResource(R.drawable.nieve);
        else if(holder.mItem.getWeather().getDescription().contains("Clear") || holder.mItem.getWeather().getDescription().contains("clear"))
                holder.image_condition.setImageResource(R.drawable.sol);
        else if(holder.mItem.getWeather().getDescription().contains("Clouds") || holder.mItem.getWeather().getDescription().contains("cloud"))
                holder.image_condition.setImageResource(R.drawable.nube);
        else if(holder.mItem.getWeather().getDescription().contains("Mist") || holder.mItem.getWeather().getDescription().contains("mist"))
            holder.image_condition.setImageResource(R.drawable.niebla);


        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');

        //Esto es para hacer el cambio de Timestamp a HH:MM:SS para que sea bonito
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(((holder.mItem.getSunriseTs())*1000l));
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        String dateSunrise = sdf1.format(c1.getTime());
        holder.tv_sunrise.setText(dateSunrise);

        //holder.tv_sunrise.setText(Long.toString(holder.mItem.getSunriseTs()));

        //Esto es para hacer el cambio de Timestamp a HH:MM:SS para que sea bonito
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(((holder.mItem.getSunsetTs())*1000l));
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String dateSunset = sdf2.format(c2.getTime());
        holder.tv_sunset.setText(dateSunset);

        holder.tv_day.setText(new SimpleDateFormat("E, dd MMM yyyy", Locale.ENGLISH)
                .format(new Date((holder.mItem.getTs()) * 1000l)));

        holder.tv_description.setText(holder.mItem.getWeather().getDescription());

        holder.image_sunset.setImageResource(R.drawable.moonrise);
        holder.image_sunrise.setImageResource(R.drawable.sunrise);

        holder.tv_max.setText(Double.toString(holder.mItem.getMaxTemp()).concat(" ºC"));
        holder.tv_min.setText(Double.toString(holder.mItem.getMinTemp()).concat(" ºC"));

        holder.tv_humidity.setText(Double.toString(holder.mItem.getPop()).concat("%"));
        holder.tv_ws.setText(new DecimalFormat("###.##").format(holder.mItem.getWindSpd()).concat(" m/s"));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public List<Datum> getDataset(){
        return this.mDataset;
    }

    public void clear(){
        int size = mDataset.size();
        mDataset.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void swap(List<Datum> dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }
}