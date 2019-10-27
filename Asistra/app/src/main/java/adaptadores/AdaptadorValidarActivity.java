package adaptadores;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.asistra.R;
import java.util.ArrayList;
import java.util.Objects;

import clases.Asistencia;


public class AdaptadorValidarActivity extends RecyclerView.Adapter<AdaptadorValidarActivity.MyViewHolder> {

    private ArrayList<Asistencia> dataSet;
    private Context mContext;

    public AdaptadorValidarActivity(Context context, ArrayList<Asistencia> asistencias ) {
        this.dataSet = asistencias;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_validar,parent,false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Asistencia asistenciaDealumno = dataSet.get(position);

        holder.txtNombre.setText(asistenciaDealumno.getNombreAlumno());
        holder.txtApellido.setText(asistenciaDealumno.getApellidoAlumno());

        final GradientDrawable drawable = (GradientDrawable)holder.preau.getBackground();

        if (Objects.requireNonNull(asistenciaDealumno).getEstado().equals("1")) {
            holder.preau.setChecked(true);
            drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.verde));
        } else {
            holder.preau.setChecked(false);
            drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.rojo)); //
        }

        //Si se apreta el botón de la asistencia
        holder.preau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (asistenciaDealumno.getEstado() =="1") {
                    holder.preau.setText("Ausente");
                    asistenciaDealumno.setEstado("0");
                    drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.rojo));
                } else {
                    drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.verde));
                    asistenciaDealumno.setEstado("1");
                    holder.preau.setText("Presente");
                }
            }


        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtNombre;
        TextView txtApellido;
        ToggleButton preau;
        CardView cardview_validar;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtNombre = itemView.findViewById(R.id.nombre);
            txtApellido = itemView.findViewById(R.id.apellidoAlumnoDeLista);
            preau = itemView.findViewById(R.id.preau);
            cardview_validar = itemView.findViewById(R.id.cardview_validar);

        }
    }


}
