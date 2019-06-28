package com.example.asistra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.asistra.R;

import java.util.ArrayList;

import clases.Cursada;

public class ListaCursadas extends ArrayAdapter implements View.OnClickListener {

    private ArrayList<Cursada> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtAsignatura;
        TextView txtComision;
    }

    public ListaCursadas(ArrayList<Cursada> listas, Context context) {
        super(context, R.layout.fila_cursada, listas);
        this.dataSet = listas;
        this.mContext=context;
    }

    @Override
    public void onClick(View view) {
        // Para atrapar algún botón, etc
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Cursada listaAsistencia = (Cursada) getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.fila_cursada, parent, false);
            viewHolder.txtAsignatura = (TextView) convertView.findViewById(R.id.asignaturaNombre);
            viewHolder.txtComision = (TextView) convertView.findViewById(R.id.fechaClase);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        viewHolder.txtAsignatura.setText(listaAsistencia.getAsignatura().getNombre().toUpperCase());
        viewHolder.txtComision.setText(listaAsistencia.getComision().getCodigo().toUpperCase());

        // Retorna la vista completa para renderizarla en pantalla
        return convertView;
    }

}
