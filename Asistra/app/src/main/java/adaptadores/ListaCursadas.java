package adaptadores;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.asistra.CursadaActivity;
import com.example.asistra.R;
import java.util.ArrayList;
import clases.Cursada;
import maes.tech.intentanim.CustomIntent;

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
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.txtAsignatura.setText(dataSet.get(position).getAsignatura().getNombre());
        holder.txtComision.setText(dataSet.get(position).getComision().getCodigo());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,CursadaActivity.class);

                // Le paso la info a la otra pantalla
                intent.putExtra("idCursada",String.valueOf(dataSet.get(position).getId()));
                intent.putExtra("asignatura",dataSet.get(position).getAsignatura().getNombre());
                intent.putExtra("idDocente",dataSet.get(position).getDocente().getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // Arranco la actividad
                mContext.startActivity(intent);
                CustomIntent.customType(mContext,"left-to-right");

                /* Tipos de animaciones
                 * *left-to-right
                 *right-to-left
                 *bottom-to-up
                 *up-to-bottom
                 *fadein-to-fadeout
                 *rotateout-to-rotatein*/

            }
        });

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
