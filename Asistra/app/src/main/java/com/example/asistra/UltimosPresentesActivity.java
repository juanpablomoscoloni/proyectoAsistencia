package com.example.asistra;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import adaptadores.ListaAsistencia;
import clases.Alumno;
import clases.Asistencia;
import clases.Inscripcion;

public class UltimosPresentesActivity extends AppCompatActivity {

    public static ListView listView;
    public static ListaAsistencia adapter;
    public static ArrayList<Asistencia> asistenciasDeAlumnos = new ArrayList<>();
    public Asistencia asistencia;
    public String idCursada;
    public String idClase;
    public String idUsuario;
    public String tema;
    public String fecha;
    SwipeRefreshLayout refresh;

    int presentes=0;
    int ausentes=0;

    ArrayList<Asistencia> asistencias = new ArrayList<>();

    Gson gson;
    String asistenciasString;
    TextView hecho;

    Alumno alumno;
    Inscripcion inscripcion;
    ArrayList<Inscripcion> listaDeInscripciones = new ArrayList<>();
    ArrayList<Alumno> listaDeAlumnos = new ArrayList<>();


    private String http;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ultimos_presentes);

        idClase = getIntent().getExtras().getString("idDiaClase");

        http = getApplicationContext().getResources().getString(R.string.ipServer) + "/proyectoAsistencia/recuperarAsistencias.php";

        //Atrapo el listview y el texto de hecho
        hecho = findViewById(R.id.validarBtn);
        listView=findViewById(R.id.listaAlumnos);
        refresh = findViewById(R.id.refrescarAsistencias);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        recuperarAsistencias(idClase);

        adapter = new ListaAsistencia(asistenciasDeAlumnos,getApplicationContext());

        listView.setAdapter(adapter);



       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                AsistenciaAlumno asistenciaDeAlumno = asistenciasDeAlumnos.get(position);
                                            }
                                        });*/


        hecho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }

        });



    }

    public void recuperarAsistencias(final String idClase){

        String urlConsulta = http;

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                recuperarDatos(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sin conexion", Toast.LENGTH_LONG).show();

            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("idDiaClase", idClase);

                return parametros;

            }

        };

        requestQueue.add(stringRequest);

    }

    public void recuperarDatos (String response) {

        try {

            Iterator iterator;
            JSONObject jsonobject = new JSONObject(response);
            JSONArray jsonArray = jsonobject.getJSONArray("Datos");

            if (jsonArray.length() > 0) {

                JSONObject datos = jsonArray.getJSONObject(0);
                JSONObject asistencias = datos.getJSONObject("asistencia");
                JSONObject inscripciones = datos.getJSONObject("inscripcion");
                JSONObject alumnos = datos.getJSONObject("alumno");

                //Descargo las inscripciones
                iterator = inscripciones.keys();
                while (iterator.hasNext()) {

                    String key = (String) iterator.next();
                    JSONObject inscripcionObjeto = inscripciones.getJSONObject(key);

                    inscripcion = new Inscripcion();
                    inscripcion.setId(inscripcionObjeto.optString("id"));
                    inscripcion.setIdAlumno(inscripcionObjeto.optString("idAlumno"));
                    inscripcion.setIdCursada(inscripcionObjeto.optString("idCursada"));
                    inscripcion.setInasistencias(inscripcionObjeto.optString("inasistencias"));

                    listaDeInscripciones.add(inscripcion);
                }

                //Descargo los alumnos
                iterator = alumnos.keys();
                while (iterator.hasNext()) {

                    String key = (String) iterator.next();
                    JSONObject alumnoObjeto = alumnos.getJSONObject(key);

                    alumno = new Alumno();
                    alumno.setId(alumnoObjeto.optString("id"));
                    alumno.setNombre(alumnoObjeto.optString("nombre"));
                    alumno.setApellido(alumnoObjeto.optString("apellido"));
                    alumno.setLegajo(alumnoObjeto.optString("legajo"));

                    listaDeAlumnos.add(alumno);
                }

                iterator = asistencias.keys();
                while (iterator.hasNext()) {

                    String key = (String) iterator.next();
                    JSONObject asistenciaObjeto = asistencias.getJSONObject(key);

                    asistencia = new Asistencia();
                    asistencia.setId(asistenciaObjeto.optString("id"));
                    asistencia.setIdDiaClase(asistenciaObjeto.optString("iddiaclase"));
                    asistencia.setIdInscripcion(asistenciaObjeto.optString("idinscripcion"));
                    asistencia.setEstado(asistenciaObjeto.optString("estado"));


                    //Acá me fijo qué comision pertenece a esta cursada de todas las que se recuperó
                    for (Inscripcion i : listaDeInscripciones) {

                        if (i.getId().equals(asistenciaObjeto.optString("idinscripcion"))) {
                            asistencia.setIdAlumno(i.getIdAlumno());
                        }

                    }

                    //Acá me fijo qué asignatura pertenece a esta cursada de todas las que se recuperó
                    for (Alumno a : listaDeAlumnos) {

                        if (a.getId().equals(asistencia.getIdAlumno())) {
                            asistencia.setNombreAlumno(a.getNombre());
                            asistencia.setApellidoAlumno(a.getApellido());
                        }

                    }

                    asistenciasDeAlumnos.add(asistencia);

                }

                adapter.notifyDataSetChanged();


            } else {
                Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }

    }


}
