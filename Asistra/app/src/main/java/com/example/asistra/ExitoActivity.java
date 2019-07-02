package com.example.asistra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ExitoActivity extends AppCompatActivity {

    private String http;

    private TextView asignatura;
    private String idAsistencia;
    private String nombreAsignatura;
    private TextView titulo;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exito);

        http = getApplicationContext().getResources().getString(R.string.ipServer) + "/proyectoAsistencia/cambiarAsistencia.php";

        idAsistencia = getIntent().getExtras().getString("id");
        nombreAsignatura = getIntent().getExtras().getString("asignatura");

        asignatura = findViewById(R.id.materiaPresente);
        progressBar = findViewById(R.id.progresoExito);
        titulo = findViewById(R.id.tituloMateriaPresente);

        ponerPresente();


    }

    public void ponerPresente(){

        String urlConsulta = http;

        progressBar.setVisibility(View.VISIBLE);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonArray = jsonobject.getJSONArray("Dato");

                    if (jsonArray.length() > 0) {

                        asignatura.setText(nombreAsignatura);
                        asignatura.setVisibility(View.VISIBLE);
                        titulo.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);


                    } else {
                        Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);

                }

            }}, new Response.ErrorListener() {
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

                parametros.put("id", idAsistencia);

                return parametros;

            }

        };

        requestQueue.add(stringRequest);

    }
}
