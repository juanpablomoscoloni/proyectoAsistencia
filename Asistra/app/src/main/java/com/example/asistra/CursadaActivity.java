package com.example.asistra;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import clases.Cursada;
import maes.tech.intentanim.CustomIntent;

public class CursadaActivity extends AppCompatActivity {

    Button pasarLista;
    TextView nombreAsignatura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cursada);

        pasarLista = findViewById(R.id.pasarListaBtn);
        nombreAsignatura = findViewById(R.id.asignatura);

        nombreAsignatura.setText(getIntent().getExtras().getString("asignatura"));

        pasarLista.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CursadaActivity.this,ClaseActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("idCursada", Objects.requireNonNull(getIntent().getExtras()).getString("idCursada"));
                intent.putExtra("idDocente",getIntent().getExtras().getString("idDocente"));
                startActivity(intent);
                CustomIntent.customType(CursadaActivity.this,"left-to-right");

                /* Tipos de animaciones
                 * *left-to-right
                 *right-to-left
                 *bottom-to-up
                 *up-to-bottom
                 *fadein-to-fadeout
                 *rotateout-to-rotatein*/

            }
        });

    }
}
