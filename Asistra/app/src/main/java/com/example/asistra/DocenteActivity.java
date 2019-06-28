package com.example.asistra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import clases.Asignatura;
import clases.Comision;
import clases.Cursada;
import clases.Docente;

public class DocenteActivity extends AppCompatActivity {

    public Cursada cursada;
    public String idUsuario;
    public String idRol;

    public static ArrayList<Cursada> listaDeCursadas = new ArrayList<>();
    public static ListView listasDeAsistenciaView;
    public static ListaCursadas adaptadorMaterias;


    TextView aunSinLista;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente);

        //idUsuario = Objects.requireNonNull(getIntent().getExtras()).getString("idUsuario");
        //idRol = getIntent().getExtras().getString("idRol");


        //Creo unas cursadas de prueba. Primero tengo que crear estas clases
        Asignatura asignatura = new Asignatura();
        asignatura.setId("1");
        asignatura.setNombre("Aplicaciones Moviles");

        Comision comision = new Comision();
        comision.setId("2");
        comision.setCodigo("S53");

        Docente docente = new Docente();
        docente.setNombre("Jorge");
        docente.setApellido("Podjarny");
        docente.setLegajo("12345");

        //Ahora creo la cursada
        Cursada cursada = new Cursada();
        cursada.setId("1");
        cursada.setAnio("2019");
        cursada.setAsignatura(asignatura);
        cursada.setComision(comision);
        cursada.setDocente(docente);



        //Inicializo el visualizador de la lista de asistencias en pantalla
        listasDeAsistenciaView = findViewById(R.id.listaDeAsistencia);

        listaDeCursadas.clear();
        listaDeCursadas.add(cursada);

        //Inicializo al adaptador
        adaptadorMaterias = new ListaCursadas(listaDeCursadas, getApplicationContext());
        //Setteo el adapter en la listView
        listasDeAsistenciaView.setAdapter(adaptadorMaterias);



        //Toast.makeText(this, cursada.getMaximoFaltas(),Toast.LENGTH_SHORT).show();

       listasDeAsistenciaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(getApplicationContext(), "A generar token",Toast.LENGTH_SHORT).show();

                /*Intent intent = new Intent(DocenteActivity.this, MostrarLista.class);
                intent.putExtra("id",String.valueOf(listasDeAsistencia.get(i).getId()));
                intent.putExtra("asignatura",listasDeAsistencia.get(i).getAsignatura().getNombre());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/

            }
        });


        //Este método es el que recibe los datos de la actividad AgregarLista, para poder añadir una nueva lista de asistencia
   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {

                //Si vuelve exitosamente de la actividad Agregar Lista, la instancio
                listaDeMateriaAtrapada = new ListaAsistencia(
                        data.getStringExtra("especialidad"),
                        data.getStringExtra("asignatura"),
                        data.getStringExtra("com"),
                        data.getStringExtra("aula"),
                        Calendar.getInstance().get(Calendar.YEAR),
                        Integer.parseInt(data.getStringExtra("ausentes")));


                //La agrego a las demás
                listasDeAsistencia.add(listaDeMateriaAtrapada);

                //Notifico al adpatador de que se modificó la lista
                adaptadorMaterias.notifyDataSetChanged();

                Toast.makeText(getApplicationContext(), "Lista de asistencia creada ",Toast.LENGTH_SHORT).show();

                //Si creo una lista de asistencia nueva, entonces saco el mensaje para que se añadan
                if (listasDeAsistencia.size() > 0 ){
                    aunSinLista.setVisibility(View.GONE);

                }
            }
        }*/
    }
}
