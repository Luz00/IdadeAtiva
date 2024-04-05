package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;

import java.util.List;

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
                Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                abrirTelaAlarme(pessoa);
            }
        };

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                abrirTelaHome(pessoa);
            }
        };

        btnClock.setOnClickListener(onClickListener);
        btnHome.setOnClickListener(onClickListener2);
    }

    private void abrirTelaAlarme(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void abrirTelaHome(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }
}