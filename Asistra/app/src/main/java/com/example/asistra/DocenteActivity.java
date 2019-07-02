package com.example.asistra;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import adaptadores.ListaCursadas;
import clases.Asignatura;
import clases.Comision;
import clases.Cursada;
import clases.Docente;


public class DocenteActivity extends AppCompatActivity {

    public String resultadoLogin;
    private String http = "http://192.168.43.218/proyectoAsistencia/recuperarDocente.php";

    public Cursada cursada;
    public Docente docente;
    public Asignatura asignatura;
    public Comision comision;

    //Todas las listas
    public static ArrayList<Cursada> listaDeCursadas = new ArrayList<>();
    public static ArrayList<Comision> listaDeComisiones = new ArrayList<>();
    public static ArrayList<Asignatura> listaDeAsignaturas = new ArrayList<>();

    //Lo necesario para mostrar las cursadas en pantalla
    public static ListView listasDeCursadaView;
    public static ListaCursadas adaptadorMaterias;

    ProgressBar progreso;
    TextView tituloDocente;
    TextView nombreDocente;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente);

        //Acá recupero el id que me pasa el login
        resultadoLogin = Objects.requireNonNull(getIntent().getExtras()).getString("id");

        //Encuentro los componentes
        progreso = findViewById(R.id.progresoDocente);
        tituloDocente = findViewById(R.id.tituloDocente);
        nombreDocente = findViewById(R.id.nombreDocente);


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
        cursada = new Cursada();
        cursada.setId("1");
        cursada.setAnio("2019");
        cursada.setAsignatura(asignatura);
        cursada.setComision(comision);
        cursada.setDocente(docente);


        //Inicializo el visualizador de la lista de asistencias en pantalla
        listasDeCursadaView = findViewById(R.id.listaDeAsistencia);

        listaDeCursadas.clear();
        //Inicializo al adaptador
        adaptadorMaterias = new ListaCursadas(listaDeCursadas, getApplicationContext());
        //Setteo el adapter en la listView
        listasDeCursadaView.setAdapter(adaptadorMaterias);


        listasDeCursadaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(DocenteActivity.this, CursadaActivity.class);
                intent.putExtra("idCursada",String.valueOf(listaDeCursadas.get(i).getId()));
                intent.putExtra("asignatura",listaDeCursadas.get(i).getAsignatura().getNombre());
                intent.putExtra("idDocente",listaDeCursadas.get(i).getDocente().getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        recuperarDocente();

    }

    public void recuperarDocente(){

        //Empieza a girar la ruedita
        progreso.setVisibility(View.VISIBLE);

        String urlConsulta = http;

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                //Aca viene el armado de los objetos a partir del JSON
                recuperarDatos(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sin conexion", Toast.LENGTH_LONG).show();
                progreso.setVisibility(View.GONE);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("id", resultadoLogin);

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

                JSONObject datos =  jsonArray.getJSONObject(0);
                JSONObject docentes = datos.getJSONObject("docente");
                JSONObject cursadas = datos.getJSONObject("cursada");
                JSONObject comisiones = datos.getJSONObject("comision");
                JSONObject asignaturas = datos.getJSONObject("asignatura");

                //Primero recupero el docente
                iterator = docentes.keys();
                while(iterator.hasNext()){

                    String key = (String)iterator.next();
                    JSONObject docenteObjeto = docentes.getJSONObject(key);

                    docente = new Docente();
                    docente.setId(docenteObjeto.optString("id"));
                    docente.setNombre(docenteObjeto.optString("nombre"));
                    docente.setApellido(docenteObjeto.optString("apellido"));
                    docente.setLegajo(docenteObjeto.optString("legajo"));

                    //Muestro el nombre en pantalla
                    nombreDocente.setText(docente.getNombre() + " " + docente.getApellido());
                    nombreDocente.setVisibility(View.VISIBLE);
                    tituloDocente.setVisibility(View.VISIBLE);

                }

                //Ahora recupero las comisiones
                iterator = comisiones.keys();
                while(iterator.hasNext()){

                    String key = (String)iterator.next();
                    JSONObject comisionObjeto = comisiones.getJSONObject(key);

                    comision = new Comision();
                    comision.setId(comisionObjeto.optString("id"));
                    comision.setCodigo(comisionObjeto.optString("codigo"));

                    listaDeComisiones.add(comision);

                }

                //Ahora recupero las asignaturas
                iterator = asignaturas.keys();
                while(iterator.hasNext()){

                    String key = (String)iterator.next();
                    JSONObject asignaturaObjeto = asignaturas.getJSONObject(key);

                    asignatura = new Asignatura();

                    asignatura.setId(asignaturaObjeto.optString("id"));
                    asignatura.setNombre(asignaturaObjeto.optString("nombre"));

                    listaDeAsignaturas.add(asignatura);

                }

                //Ahora recupero las cursadas
                iterator = cursadas.keys();
                while(iterator.hasNext()){

                    String key = (String)iterator.next();
                    JSONObject cursadaObjeto = cursadas.getJSONObject(key);

                    cursada = new Cursada();
                    cursada.setId(cursadaObjeto.optString("id"));
                    cursada.setFaltasMaximas(cursadaObjeto.optString("faltasMaximas"));
                    cursada.setAnio(cursadaObjeto.optString("anio"));
                    cursada.setDocente(docente);

                    //Acá me fijo qué comision pertenece a esta cursada de todas las que se recuperó
                    for (Comision c : listaDeComisiones) {

                        if (c.getId().equals(cursadaObjeto.optString("idComision"))) {
                            cursada.setComision(c);
                        }

                    }

                    //Acá me fijo qué asignatura pertenece a esta cursada de todas las que se recuperó
                    for (Asignatura a : listaDeAsignaturas) {

                        if (a.getId().equals(cursadaObjeto.optString("idAsignatura"))) {
                            cursada.setAsignatura(a);
                        }

                    }

                    listaDeCursadas.add(cursada);

                }

                //Acá se refresca la lista de las cursadas
                adaptadorMaterias.notifyDataSetChanged();
                listasDeCursadaView.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_LONG).show();
            }

            progreso.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
            progreso.setVisibility(View.GONE);
        }

    }


}
