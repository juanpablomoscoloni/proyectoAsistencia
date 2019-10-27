package com.example.asistra;

import android.annotation.SuppressLint;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import adaptadores.AdaptadorValidarActivity;
import clases.Alumno;
import clases.Asistencia;
import clases.AsistenciaJSON;
import clases.Inscripcion;

public class ValidarActivity extends AppCompatActivity {

    public static ListView listView;
    public static AdaptadorValidarActivity adapter;
    public Asistencia asistencia;
    public AsistenciaJSON asistenciaJSON;
    public static ArrayList<Asistencia> asistenciasDeAlumnosFinal = new ArrayList<>();
    public static ArrayList<Asistencia> asistenciasDeAlumnosInicial = new ArrayList<>();
    public static ArrayList<AsistenciaJSON> asistenciasJSON = new ArrayList<>();

    public String idClase;
    SwipeRefreshLayout refresh;

    Gson gson;
    String asistenciasConvertidas;
    Button validar;

    Alumno alumno;
    Inscripcion inscripcion;
    ArrayList<Inscripcion> listaDeInscripciones = new ArrayList<>();
    ArrayList<Alumno> listaDeAlumnos = new ArrayList<>();


    private String http;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar);

        idClase = getIntent().getExtras().getString("idDiaClase");

        http = getApplicationContext().getResources().getString(R.string.ipServer) + "/proyectoAsistencia/recuperarAsistencias.php";

        //Atrapo el listview y el texto de hecho
        validar = findViewById(R.id.validarBtn);
        listView=findViewById(R.id.listaAlumnos);
        refresh = findViewById(R.id.refrescarAsistencias);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                asistenciasDeAlumnosFinal.clear();
                asistenciasDeAlumnosInicial.clear();
                recuperarAsistencias(idClase);
            }
        });

        asistenciasDeAlumnosInicial.clear();
        asistenciasDeAlumnosFinal.clear();
        recuperarAsistencias(idClase);

        adapter = new AdaptadorValidarActivity(asistenciasDeAlumnosFinal,getApplicationContext());
        listView.setAdapter(adapter);

        validar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convertirAsistencias();
                gson = new Gson();
                asistenciasConvertidas = gson.toJson(asistenciasJSON);
                Toast.makeText(getApplicationContext(), asistenciasConvertidas, Toast.LENGTH_LONG).show();

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
                refresh.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sin conexion", Toast.LENGTH_LONG).show();
                refresh.setRefreshing(false);
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

                    asistenciasDeAlumnosFinal.add(asistencia);

                }

                //Acá ordeno de ausentes a presentes
                Collections.sort(asistenciasDeAlumnosFinal, new Comparator<Asistencia>() {
                    @Override
                    public int compare(Asistencia a, Asistencia a1) {
                        String s1 = a.getEstado();
                        String s2 = a1.getEstado();
                        return s1.compareToIgnoreCase(s2);
                    }

                });

                //Acá genero la copia
                for (Asistencia a: asistenciasDeAlumnosFinal) {
                    asistencia = new Asistencia();
                    asistencia.setId(a.getId());
                    asistencia.setEstado(a.getEstado());
                    asistenciasDeAlumnosInicial.add(asistencia);
                }

                //Acá notifico al adaptador de la lista que se cargaron todas las asistencias
                adapter.notifyDataSetChanged();


            } else {
                Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }

    }

    public void convertirAsistencias () {
    /*
     * Este método crea asistencias para enviar como JSON porque la clase asistencia comun
     * tiene campos de más que están al pedo para mandarlos como json.
     * Entonces creé esta clase asistencia que tiene los campos justos y necesarios.
    */
        asistenciasJSON.clear();

        for (int i = 0; i < asistenciasDeAlumnosFinal.size() ; i++) {

            asistenciaJSON = new AsistenciaJSON();

            //Acá voy comparando si cambió el estado con respecto a la lista inicial
            if (!asistenciasDeAlumnosInicial.get(i).getEstado().equals(asistenciasDeAlumnosFinal.get(i).getEstado())) {

                //Si cambió lo voy agregando a la lista de los JSON
                asistenciaJSON.setId(asistenciasDeAlumnosFinal.get(i).getId());

                asistenciasJSON.add(asistenciaJSON);


            }



        }

    }

}
