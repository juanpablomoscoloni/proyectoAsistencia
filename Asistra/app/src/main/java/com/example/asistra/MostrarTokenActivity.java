package com.example.asistra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
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

public class MostrarTokenActivity extends AppCompatActivity {

    private String http;

    Button finalizar;
    TextView token;
    TextView restantes;
    TextView segundos;
    ProgressBar progressBar;
    CountDownTimer cTimer = null;

    String idDiaClase;

    Alumno alumno;
    Inscripcion inscripcion;
    Asistencia asistencia;
    ArrayList<Asistencia> listaDeAsistencias = new ArrayList<>();
    ArrayList<Inscripcion> listaDeInscripciones = new ArrayList<>();
    ArrayList<Alumno> listaDeAlumnos = new ArrayList<>();
    ArrayList<Asistencia> alumnosRestantes = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_token);

        http = getApplicationContext().getResources().getString(R.string.ipServer) + "/proyectoAsistencia/recuperarAsistencias.php";

        token = findViewById(R.id.tokenMuestraId);
        restantes = findViewById(R.id.restantesId);
        segundos = findViewById(R.id.segundosId);
        progressBar = findViewById(R.id.progresoRestantesId);
        finalizar = findViewById(R.id.finalizarBtn);

        token.setText(Objects.requireNonNull(getIntent().getExtras()).getString("token"));
        restantes.setText(getIntent().getExtras().getString("cantidadAlumnos") + " restantes");
        idDiaClase = getIntent().getExtras().getString("idDiaClase");
        startTimer();

        finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Se cancela el timer
                cancelTimer();
                Intent intent = new Intent(MostrarTokenActivity.this,UltimosPresentesActivity.class);
                intent.putExtra("idDiaClase",idDiaClase);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }

    public void actualizarRestantes(final String idClase){

        String urlConsulta = http;

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                recuperarDatos(response);
                restantes.setText(calcularRestantes() + " restantes");
                progressBar.setVisibility(View.GONE);
                startTimer();

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

    public int calcularRestantes () {

        int resultado = 0;
        alumnosRestantes.clear();

        for (Asistencia a: listaDeAsistencias) {

            if (a.getEstado().equals("0")) {
                alumnosRestantes.add(a);
            }

        }

        listaDeAsistencias.clear();
        resultado = alumnosRestantes.size();
        return resultado;

    }

    //arranca el temporizador
    void startTimer() {
        cTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seg = (int) (millisUntilFinished / 1000);
                segundos.setText(Integer.toString(seg));
            }
            public void onFinish() {
                actualizarRestantes(idDiaClase);
            }
        };
        cTimer.start();
    }

    //cancela el temporizador
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}
