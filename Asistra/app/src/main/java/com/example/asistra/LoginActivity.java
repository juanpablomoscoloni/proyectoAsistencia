package com.example.asistra;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String http = "http://192.168.0.104/proyectoAsistencia/comprobarUsuario.php";

    public EditText campoUsuario;
    public EditText campoContra;
    public ProgressBar progreso;
    public Button iniciar;

   // public Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoContra = findViewById(R.id.pass);
        campoUsuario = findViewById(R.id.usuario);
        progreso = findViewById(R.id.inicioProgreso);
        iniciar = findViewById(R.id.iniciar);

        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (comprobarCampos()) {
                    iniciarSesion();
                }

            }
        });

    }

    public Boolean comprobarCampos () {

        Boolean resultado=true;

        if (campoUsuario.getText().toString().equals("") || campoContra.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Llenar los campos", Toast.LENGTH_LONG).show();
            resultado = false;
        }


        return resultado;
    }

    public void iniciarSesion () {

        //Bloquea el botón de Inicio de Sesión
        iniciar.setEnabled(false);

        //Empieza a girar la ruedita
        progreso.setVisibility(View.VISIBLE);

        String urlConsulta = http;

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlConsulta, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonArray = jsonobject.getJSONArray("Usuario"); //Modificable

                    if (jsonArray.length() > 0) {

                        JSONObject user = jsonArray.getJSONObject(0); //Modificable

                        //Compruebo si el usuario que se recuperó es de un docente
                        if (user.getString("rol").equals("alumno")) {
                            pasarActividadAlumno(user.getString("idAlumno"));
                        } else {
                            pasarActividadDocente(user.getString("idDocente"));
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "No se encontró", Toast.LENGTH_LONG).show();
                    }

                    progreso.setVisibility(View.GONE);
                    iniciar.setEnabled(true);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    progreso.setVisibility(View.GONE);
                    iniciar.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Sin conexion", Toast.LENGTH_LONG).show();
                progreso.setVisibility(View.GONE);
                iniciar.setEnabled(true);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();

                parametros.put("user", campoUsuario.getText().toString());
                parametros.put("password", campoContra.getText().toString());

                return parametros;

            }

        };

        requestQueue.add(stringRequest);

    }

   public void pasarActividadAlumno (String id) {

     Intent intent = new Intent(LoginActivity.this, TokenActivity.class);

     intent.putExtra("id", id);
     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

     startActivity(intent);

    }

    public void pasarActividadDocente (String id) {

        Intent intent = new Intent(LoginActivity.this, DocenteActivity.class);

        intent.putExtra("id", id);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }
}
