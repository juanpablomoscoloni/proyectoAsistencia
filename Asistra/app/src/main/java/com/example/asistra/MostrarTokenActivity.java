package com.example.asistra;

import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class MostrarTokenActivity extends AppCompatActivity {

    TextView token;
    TextView restantes;
    TextView segundos;
    CountDownTimer cTimer = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_token);

        token = findViewById(R.id.tokenMuestraId);
        restantes = findViewById(R.id.restantesId);
        segundos = findViewById(R.id.segundosId);

        token.setText(Objects.requireNonNull(getIntent().getExtras()).getString("token"));
        startTimer();

    }

    //arranca el temporizador
    void startTimer() {
        cTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                int seg = (int) (millisUntilFinished / 1000);
                segundos.setText(Integer.toString(seg));
            }
            public void onFinish() {
                startTimer();
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
