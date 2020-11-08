package es.unex.giis.zuni.eventos;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import es.unex.giis.zuni.R;

public class EventoAdapter extends RecyclerView.Adapter<EventoAdapter.ViewHolder> {
    private List<Evento> mItems = new ArrayList<Evento>();
    Context mContext;

    public interface OnItemClickListener {
        void onItemClick(Evento item);      // Tipo del elemento que se va a devolver
    }

    private final OnItemClickListener listener;


    /* CONSTRUCTOR DEL ADAPTER*/
    public EventoAdapter(Context context, OnItemClickListener listener){
        this.mContext = context;
        this.listener = listener;
    }


    /* CREA NUEVAS VISTAS (INVOCADO POR EL LAYOUT MANAGER) */
    @Override
    public EventoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        // Se infla la vista para cada elemento
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.evento, parent, false);

        return new ViewHolder(mContext, v);
    }


    /* REEMPLAZA LOS CONTENIDOS DE UNA VISTA (INVOCADO POR EL LAYOUT MANAGER) */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        holder.bind(mItems.get(position), listener);
    }


    /* DEVUELVE EL NUMERO DE ELEMENTOS DE LA COLECCION */
    @Override
    public int getItemCount(){
        return mItems.size();
    }


    public void add(Evento item){
        mItems.add(item);
        notifyDataSetChanged();
    }

    public void remove(Evento item){
        mItems.remove(item);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    public void load(List<Evento> items){
        mItems.clear();
        mItems = items;
        notifyDataSetChanged();
    }



    public Object getItem(int pos) { return mItems.get(pos); }


    /* VIEWHOLDER ------------------------------------------------------------------------------- */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;

        /* Elementos del layout de cada item */
        private TextView titulo;
        private TextView fecha;
        private TextView ubicacion;

        public ViewHolder(Context context, View itemView){
            super(itemView);

            mContext = context;

            // Obtener las referencias de cada widget de la vista del elemento
            titulo = itemView.findViewById(R.id.tituloEvento);
            fecha = itemView.findViewById(R.id.fechaEvento);
            ubicacion = itemView.findViewById(R.id.ubicacionEvento);
        }

        public void bind(final Evento evento, final OnItemClickListener listener) {

            /* Configurar los widgets del elemento */
            titulo.setText(evento.getTitulo());
            fecha.setText(Evento.FORMAT.format(evento.getFecha()));
            ubicacion.setText(evento.getUbicacion());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    listener.onItemClick(evento);
                }
            });
        }
    }
}
