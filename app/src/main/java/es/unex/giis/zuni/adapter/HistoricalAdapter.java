package es.unex.giis.zuni.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.historical.Historical;

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.MyViewHolder> {
    private List<Historical> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image_condition, image_sunrise, image_sunset;
        public TextView tv_day, tv_description, tv_sunrise, tv_sunset, tv_max,tv_min,tv_humidity,tv_ws;
        public View mView;

        public Historical mItem;

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

    public HistoricalAdapter(ArrayList<Historical> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public HistoricalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_historical, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);


        holder.tv_day.setText("TEST");

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