package com.example.asistra;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import adaptadores.AdaptadorDocenteActivity;
import clases.Asignatura;
import clases.Comision;
import clases.Cursada;
import clases.Docente;


public class DocenteActivity extends AppCompatActivity {

    public String resultadoLogin;
    private String http;

    public Cursada cursada;
    public Docente docente;
    public Asignatura asignatura;
    public Comision comision;

    //Todas las listas
    public static ArrayList<Cursada> listaDeCursadas = new ArrayList<>();
    public static ArrayList<Comision> listaDeComisiones = new ArrayList<>();
    public static ArrayList<Asignatura> listaDeAsignaturas = new ArrayList<>();

    //Lo necesario para mostrar las cursadas en pantalla
    public static RecyclerView listasDeCursadaView;
    public static AdaptadorDocenteActivity adaptadorMaterias;

    ProgressBar progreso;
    TextView tituloDocente;
    TextView nombreDocente;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docente);

        http = getApplicationContext().getResources().getString(R.string.ipServer) + "/proyectoAsistencia/recuperarDocente.php";

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


        listasDeCursadaView = findViewById(R.id.listaDeCursadas);
        adaptadorMaterias = new AdaptadorDocenteActivity(this,listaDeCursadas);
        listasDeCursadaView.setLayoutManager(new GridLayoutManager(this,1));
        listasDeCursadaView.setAdapter(adaptadorMaterias);

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
