

// ---------------------------------------------------------------

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
import java.util.Calendar;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.api.current.Current;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.MyViewHolder> {
    private List<Current> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
       /* public TextView mTextView;
        public TextView mDateView; */

       public ImageView image;
        public TextView hora;
        public TextView condicion;
        public TextView descripcion;
        public TextView humedad;
        public TextView viento;
        public TextView presion;
        public TextView st;
        public TextView temp;
        public View mView;
        public Current mItem;

        public MyViewHolder(View v) {
            super(v);
            mView=v;

            image = v.findViewById(R.id.image);
            condicion = v.findViewById(R.id.condicion);
            descripcion = v.findViewById(R.id.descripcion);
            humedad = v.findViewById(R.id.humedad);
            viento = v.findViewById(R.id.viento);
            presion = v.findViewById(R.id.presion);
            st = v.findViewById(R.id.max);
            temp = v.findViewById(R.id.min);

            /*
            mTextView = v.findViewById(R.id.textView);
            mDateView = v.findViewById(R.id.dateView);
            */
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CurrentAdapter(List<Current> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public CurrentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        // Create new views (invoked by the layout manager)
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current, parent, false);

        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);
        /*holder.mTextView.setText(mDataset.get(position).getName());
        holder.mDateView.setText(mDataset.get(position).getCreatedAt());*/

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
            case "Mist":
                holder.image.setImageResource(R.drawable.niebla);
        }

        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');

        DecimalFormat df = new DecimalFormat("0",simbolos);
        DecimalFormat df1 = new DecimalFormat("0.0",simbolos);
        DecimalFormat df2 = new DecimalFormat("0.00",simbolos);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(holder.mItem.getDt()*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String dateString = sdf.format(c.getTime());


        holder.condicion.setText(holder.mItem.getWeather().get(0).getMain());
        holder.descripcion.setText(holder.mItem.getWeather().get(0).getDescription());

        holder.humedad.setText("H: ".concat(df.format(holder.mItem.getMain().getHumidity())).concat("%"));
        holder.presion.setText("P: ".concat(df1.format(holder.mItem.getMain().getPressure())).concat(" hPa"));
        holder.viento.setText("WS: ".concat(df2.format(holder.mItem.getWind().getSpeed())).concat(" m/s"));
        holder.st.setText("T: ".concat(df2.format(holder.mItem.getMain().getFeelsLike())).concat(" ºC"));
        holder.temp.setText("FL:".concat(df2.format(holder.mItem.getMain().getTemp())).concat(" ºC"));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    public void swap(List<Current> dataset){
        mDataset = dataset;
        notifyDataSetChanged();
    }
}