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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import adaptadores.ListaCursadas;
import clases.Asignatura;
import clases.Comision;
import clases.Cursada;
import clases.Docente;


public class DocenteActivity extends AppCompatActivity {

    public String resultadoLogin;

    public Cursada cursada;

    public static ArrayList<Cursada> listaDeCursadas = new ArrayList<>();
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
        listaDeCursadas.add(cursada);

        //Inicializo al adaptador
        adaptadorMaterias = new ListaCursadas(listaDeCursadas, getApplicationContext());
        //Setteo el adapter en la listView
        listasDeCursadaView.setAdapter(adaptadorMaterias);


        listasDeCursadaView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(DocenteActivity.this, CursadaActivity.class);
                intent.putExtra("id",String.valueOf(listaDeCursadas.get(i).getId()));
                intent.putExtra("asignatura",listaDeCursadas.get(i).getAsignatura().getNombre());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        recuperarDocente();


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

    public void recuperarDocente(){

        //Empieza a girar la ruedita
        progreso.setVisibility(View.VISIBLE);

        String urlConsulta = "http://192.168.0.104/proyectoAsistencia/recuperarDocente.php";

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonArray = jsonobject.getJSONArray("Docente"); //Modificable

                    if (jsonArray.length() > 0) {

                       JSONObject docente = jsonArray.getJSONObject(0); //Modificable

                       //Acá recupero el docente
                       nombreDocente.setText(docente.getString("nombre") + " " + docente.getString("apellido"));
                       nombreDocente.setVisibility(View.VISIBLE);
                       tituloDocente.setVisibility(View.VISIBLE);
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


}
