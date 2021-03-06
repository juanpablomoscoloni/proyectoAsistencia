package com.example.asistra;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Objects;

public class TokenActivity extends AppCompatActivity {

    private String http;
    private String token;
    private String idAlumno;

    private String idAsistencia;
    private String cantidadInasistencias;
    private String nombreAsignatura;

    EditText campoToken;
    TextView meterToken;
    Button comprobarBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        http = getApplicationContext().getResources().getString(R.string.ipServer) + "/proyectoAsistencia/comprobarToken.php";

        campoToken = findViewById(R.id.editText);
        meterToken = findViewById(R.id.introducirToken);

        comprobarBtn = findViewById(R.id.comprobarBtn);
        progressBar = findViewById(R.id.progresoComprobarTokenBtn);

        comprobarBtn.setVisibility(View.INVISIBLE);

        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                if (s.length() == 4) {

                    comprobarBtn.setVisibility(View.VISIBLE);
                    meterToken.setText("Formato válido");

                } else {

                    comprobarBtn.setVisibility(View.INVISIBLE);

                    if (s.length() == 0 ) {
                        meterToken.setText("Ingrese el token:");
                    } else {
                        meterToken.setText("Formato inválido");
                    }

                }
            }

            public void afterTextChanged(Editable s) {
            }
        };

        campoToken.addTextChangedListener(mTextEditorWatcher);

        comprobarBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                token = campoToken.getText().toString().toUpperCase();
                idAlumno = Objects.requireNonNull(getIntent().getExtras()).getString("id");
                comprobarToken(idAlumno,token);
            }
        });


    }

    public void comprobarToken(final String idAlumno, final String token){

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

                        Iterator iterator;
                        JSONObject datos = jsonArray.getJSONObject(0);
                        JSONObject asistencia = datos.getJSONObject("asistencia");
                        JSONObject asignatura = datos.getJSONObject("asignatura");
                        JSONObject inscripcion = datos.getJSONObject("inscripcion");

                        iterator = asistencia.keys();
                        while (iterator.hasNext()) {

                            String key = (String) iterator.next();
                            JSONObject asistenciaObjeto = asistencia.getJSONObject(key);
                            idAsistencia = asistenciaObjeto.optString("id");

                        }

                        iterator = inscripcion.keys();
                        while (iterator.hasNext()) {

                            String key = (String) iterator.next();
                            JSONObject inscripcionObjeto = inscripcion.getJSONObject(key);
                            cantidadInasistencias = inscripcionObjeto.optString("inasistencias");

                        }

                        iterator = asignatura.keys();
                        while (iterator.hasNext()) {

                            String key = (String) iterator.next();
                            JSONObject asignaturaObjeto = asignatura.getJSONObject(key);
                            nombreAsignatura = asignaturaObjeto.optString("nombre");

                        }

                        progressBar.setVisibility(View.GONE);
                        exito ();


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

                parametros.put("id", idAlumno);
                parametros.put("token", token);

                return parametros;

            }

        };

        requestQueue.add(stringRequest);

    }

    public void exito () {

        Intent intent = new Intent(TokenActivity.this, ExitoActivity.class);

        intent.putExtra("id", idAsistencia);
        intent.putExtra("asignatura", nombreAsignatura);
        intent.putExtra("faltas", cantidadInasistencias);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);

    }
}
