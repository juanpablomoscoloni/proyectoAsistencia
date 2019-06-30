package com.example.asistra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ClaseActivity extends AppCompatActivity {

    Button generarToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clase);

        generarToken = findViewById(R.id.generarTokenBtn);

        generarToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Se genera token", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
