package adaptadores;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.asistra.R;

import java.util.ArrayList;

import clases.Cursada;

public class ListaCursadas extends RecyclerView.Adapter<ListaCursadas.MyViewHolder> {

    private ArrayList<Cursada> dataSet;
    private Context mContext;

    public ListaCursadas (Context context, ArrayList<Cursada> listas ) {
        this.dataSet = listas;
        this.mContext=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_cursada,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txtAsignatura.setText(dataSet.get(position).getAsignatura().getNombre());
        holder.txtComision.setText(dataSet.get(position).getComision().getCodigo());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtAsignatura;
        TextView txtComision;
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtAsignatura = itemView.findViewById(R.id.asignaturaNombre) ;
            txtComision = itemView.findViewById(R.id.fechaClase) ;
            cardView = itemView.findViewById(R.id.cardview_id);

        }
    }

}
