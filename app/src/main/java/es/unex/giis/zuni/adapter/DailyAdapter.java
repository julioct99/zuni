package es.unex.giis.zuni.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.daily.Daily;

public class DailyAdapter extends RecyclerView.Adapter<DailyAdapter.MyViewHolder> {
    private List<Daily> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       public ImageView image_condition, image_sunrise, image_sunset;
       public TextView tv_day, tv_description, tv_sunrise, tv_sunset;
        public View mView;

        public Daily mItem;

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

        }
    }

    public DailyAdapter(ArrayList<Daily> myDataset) {
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


        switch (holder.mItem.getWeather().get(0).getMain()){
            case "Thunderstorm":
                holder.image_condition.setImageResource(R.drawable.tormenta);
                break;
            case "Drizzle":
            case "Rain":
                holder.image_condition.setImageResource(R.drawable.lluvia);
                break;
            case "Snow":
                holder.image_condition.setImageResource(R.drawable.nieve);
                break;
            case "Clear":
                holder.image_condition.setImageResource(R.drawable.sol);
                break;
            case "Clouds":
                holder.image_condition.setImageResource(R.drawable.nube);
                break;
        }

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');

        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(holder.mItem.getSunrise()*1000);
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
        String dateSunrise = sdf1.format(c1.getTime());
        holder.tv_sunrise.setText(dateSunrise);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(holder.mItem.getSunset()*1000);
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        String dateSunset = sdf2.format(c2.getTime());
        holder.tv_sunset.setText(dateSunset);

        holder.tv_day.setText(new SimpleDateFormat("yyyy-MM-dd")
                .format(new Date(holder.mItem.getDt() * 1000L)));

        holder.tv_description.setText(holder.mItem.getWeather().get(0).getDescription());

        holder.image_sunset.setImageResource(R.drawable.sol);
        holder.image_sunrise.setImageResource(R.drawable.sol);


    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap(List<Daily> dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }
}