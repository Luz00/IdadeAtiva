package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class activity_relatorio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        AppCompatButton btnClock = findViewById(R.id.btnClock);
        AppCompatButton btnHome = findViewById(R.id.btnHome);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlarme();
            }
        };

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaHome();
            }
        };

        btnClock.setOnClickListener(onClickListener);
        btnHome.setOnClickListener(onClickListener2);
    }

    private void abrirTelaAlarme() {
        Intent intent = new Intent(this, activity_tela_alarme.class);
        startActivity(intent);
    }

    private void abrirTelaHome() {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        startActivity(intent);
    }
}