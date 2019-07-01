package com.example.asistra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import clases.Alumno;
import clases.Asistencia;
import clases.Inscripcion;

public class ClaseActivity extends AppCompatActivity {

    private String http = "http://192.168.0.104/proyectoAsistencia/crearClase.php";

    Button generarToken;
    TextView tema;
    String docenteID;
    String cursadaID;
    String token;
    String idDiaClase;

    ProgressBar progressBar;

    Alumno alumno;
    Inscripcion inscripcion;
    Asistencia asistencia;
    ArrayList<Asistencia> listaDeAsistencias = new ArrayList<>();
    ArrayList<Inscripcion> listaDeInscripciones = new ArrayList<>();
    ArrayList<Alumno> listaDeAlumnos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase);

        docenteID = getIntent().getExtras().getString("idDocente");
        cursadaID = getIntent().getExtras().getString("idCursada");

        progressBar = findViewById(R.id.progresoClase);
        progressBar.setVisibility(View.GONE);
        generarToken = findViewById(R.id.generarTokenBtn);
        tema = findViewById(R.id.tema);

        generarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tema.getText().length() > 0 ) {
                    listaDeAsistencias.clear();
                    crearDiaClase(tema.getText().toString(),cursadaID, docenteID);
                } else {
                    Toast.makeText(getApplicationContext(), "Ingrese un tema", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    public void crearDiaClase(final String tema, final String idCursada, final String idDocente){

        String urlConsulta = http;

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                recuperarDatos(response);
                progressBar.setVisibility(View.GONE);

                generarToken();
                siguienteActividad();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sin conexion", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("idDocente", idDocente);
                parametros.put("idCursada", idCursada);
                parametros.put("tema", tema);

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
                JSONObject clases = datos.getJSONObject("diaclase");

                //guardo el dia de la clase
                iterator = clases.keys();
                while (iterator.hasNext()) {

                    String key = (String) iterator.next();
                    JSONObject claseObjeto = clases.getJSONObject(key);

                    idDiaClase = claseObjeto.optString("id");
                }

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

                    listaDeAsistencias.add(asistencia);

                }


            } else {
                Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_LONG).show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

        }

    }


    public void generarToken () {
        //Acá se generará aleatoriamente un token
        token = "A567";
    }

    public void siguienteActividad () {

        if (listaDeAsistencias.size()>0) {
            Intent intent = new Intent(ClaseActivity.this, MostrarTokenActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("idDiaClase", idDiaClase);
            intent.putExtra("token", token);
            intent.putExtra("cantidadAlumnos", Integer.toString(listaDeAsistencias.size()));
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "No hay alumnos", Toast.LENGTH_LONG).show();
        }

    }

}
