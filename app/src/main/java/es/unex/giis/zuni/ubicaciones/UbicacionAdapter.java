package es.unex.giis.zuni.ubicaciones;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.giis.zuni.R;



public class UbicacionAdapter extends RecyclerView.Adapter<UbicacionAdapter.ViewHolder> {
    private List<Ubicacion> mItems = new ArrayList<Ubicacion>();
    Context mContext;

    public interface OnItemClickListener {
        void onItemClick(Ubicacion item);      // Tipo del elemento que se va a devolver
    }

    private final UbicacionAdapter.OnItemClickListener listener;


    /* CONSTRUCTOR DEL ADAPTER*/
    public UbicacionAdapter(Context context, UbicacionAdapter.OnItemClickListener listener){
        this.mContext = context;
        this.listener = listener;
    }


    /* CREA NUEVAS VISTAS (INVOCADO POR EL LAYOUT MANAGER) */
    @Override
    public UbicacionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        // Se infla la vista para cada elemento
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ubicacion, parent, false);

        return new UbicacionAdapter.ViewHolder(mContext, v);
    }


    /* REEMPLAZA LOS CONTENIDOS DE UNA VISTA (INVOCADO POR EL LAYOUT MANAGER) */
    @Override
    public void onBindViewHolder(UbicacionAdapter.ViewHolder holder, int position){
        holder.bind(mItems.get(position), listener);
    }


    /* DEVUELVE EL NUMERO DE ELEMENTOS DE LA COLECCION */
    @Override
    public int getItemCount(){
        return mItems.size();
    }


    public void add(Ubicacion item){
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void remove(Ubicacion item){
        mItems.remove(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void load(List<Ubicacion> items){
        mItems.clear();
        mItems = items;
        notifyDataSetChanged();
    }



    public Object getItem(int pos) { return mItems.get(pos); }


    /* VIEWHOLDER ------------------------------------------------------------------------------- */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        /* Elementos del layout de cada item */
        private TextView TVubicacion;

        public ViewHolder(Context context, View itemView){
            super(itemView);

            mContext = context;

            // Obtener las referencias de cada widget de la vista del elemento
            TVubicacion = itemView.findViewById(R.id.ubicacionUbicacion);
        }

        public void bind(final Ubicacion ubicacion, final UbicacionAdapter.OnItemClickListener listener) {

            /* Configurar los widgets del elemento */

            if(ubicacion.getBanderaUbiFav()) {
                TVubicacion.setText(ubicacion.getUbicacion().concat(" (Ubicación predeterminada)"));
            }else{
                TVubicacion.setText(ubicacion.getUbicacion());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listener.onItemClick(ubicacion);
                }
            });
        }
    }
}
