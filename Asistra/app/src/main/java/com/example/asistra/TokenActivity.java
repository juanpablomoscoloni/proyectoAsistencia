package com.example.asistra;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TokenActivity extends AppCompatActivity {

    EditText campoToken;
    TextView meterToken;
    Button comprobarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token);

        campoToken = findViewById(R.id.editText);
        meterToken = findViewById(R.id.introducirToken);
        //campoToken.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        comprobarBtn = findViewById(R.id.comprobarBtn);

        TextWatcher mTextEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                if (s.length() == 4) {

                    comprobarBtn.setVisibility(View.VISIBLE);
                    meterToken.setText("Token válido");

                } else {

                    comprobarBtn.setVisibility(View.INVISIBLE);

                    if (s.length() == 0 ) {
                        meterToken.setText("Ingrese el token:");
                    } else {
                        meterToken.setText("Token inválido");
                    }

                }
            }

            public void afterTextChanged(Editable s) {
            }
        };

        campoToken.addTextChangedListener(mTextEditorWatcher);


    }
}
