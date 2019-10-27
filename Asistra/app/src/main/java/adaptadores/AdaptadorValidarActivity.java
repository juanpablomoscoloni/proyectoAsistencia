package adaptadores;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.asistra.R;

import java.util.ArrayList;
import java.util.Objects;

import clases.Asistencia;

/**
 Created by Starnova on 27/04/2018.
 */

public class AdaptadorValidarActivity extends ArrayAdapter<Asistencia> implements View.OnClickListener{

    private ArrayList<Asistencia> dataSet;
    Context mContext;

    @Override
    public void onClick(View view) {

    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtNombre;
        TextView txtApellido;
        TextView txtLegajo;
        TextView masOpciones;
        TextView recordar;
        ToggleButton preau;
    }

    public AdaptadorValidarActivity(ArrayList<Asistencia> asistenciasAlumnos, Context context) {
        super(context, R.layout.fila_asistencia, asistenciasAlumnos);
        this.dataSet = asistenciasAlumnos;
        this.mContext=context;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Asistencia asistenciaDealumno = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fila_asistencia, parent, false);
            viewHolder.txtNombre = (TextView) convertView.findViewById(R.id.nombre);
            viewHolder.txtApellido = (TextView) convertView.findViewById(R.id.apellidoAlumnoDeLista);
            viewHolder.preau = (ToggleButton) convertView.findViewById(R.id.preau);
            viewHolder.recordar = convertView.findViewById(R.id.recordar);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        /*

         A partir de acá se hacen las modificaciones sino se renuevan los botones

         */
        final GradientDrawable drawable = (GradientDrawable)viewHolder.preau.getBackground();

        if (Objects.requireNonNull(asistenciaDealumno).getEstado().equals("1")) {
            viewHolder.preau.setChecked(true);
            drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.verde));
        } else {
            viewHolder.preau.setChecked(false);
            drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.rojo)); //
        }


        viewHolder.txtNombre.setText(asistenciaDealumno.getNombreAlumno());
        viewHolder.txtApellido.setText(asistenciaDealumno.getApellidoAlumno());

        //Si se apreta el botón de la asistencia
        viewHolder.preau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (asistenciaDealumno.getEstado() =="1") {
                     viewHolder.preau.setText("Ausente");
                    asistenciaDealumno.setEstado("0");
                    drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.rojo));
                } else {
                    drawable.setStroke(3, ContextCompat.getColor(mContext, R.color.verde));
                    asistenciaDealumno.setEstado("1");
                    viewHolder.preau.setText("Presente");
                }

                 }


        });


        // Return the completed view to render on screen
        return convertView;
    }
}