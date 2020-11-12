package es.unex.giis.zuni.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.giis.zuni.R;
import es.unex.giis.zuni.historical.Hourly;

public class HistoricalAdapter extends RecyclerView.Adapter<HistoricalAdapter.MyViewHolder> {
    private List<Hourly> mDataset;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
       public TextView text;
        public View mView;

        public Hourly mItem;

        public MyViewHolder(View v) {
            super(v);
            mView=v;

        }
    }

    public HistoricalAdapter(ArrayList<Hourly> myDataset) {
        mDataset = myDataset;
    }


    @Override
    public HistoricalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_historical, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.mItem = mDataset.get(position);
        holder.text.setText(holder.mItem.getWeather().get(0).getMain());

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void swap(List<Hourly> dataset){
        Log.e("HOLAAAAAAAAAAAA","6");
        mDataset = dataset;
        notifyDataSetChanged();

    }
}