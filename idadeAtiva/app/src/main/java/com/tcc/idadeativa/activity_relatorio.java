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

    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        dao = new DAO(this);
        List<Pessoa> pessoas = dao.buscaPessoa();

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
                if (!pessoas.isEmpty()) {
                    if (pessoas.size() >= 1) {
                        abrirTelaHome(pessoas.get(0));
                    }
                    else if (pessoas.size() >= 2) {
                        abrirTelaHome(pessoas.get(1));
                    }
                    else if (pessoas.size() >= 3) {
                        abrirTelaHome(pessoas.get(2));
                    }
                }
            }
        };

        btnClock.setOnClickListener(onClickListener);
        btnHome.setOnClickListener(onClickListener2);
    }

    private void abrirTelaAlarme() {
        Intent intent = new Intent(this, activity_tela_alarme.class);
        startActivity(intent);
    }

    private void abrirTelaHome(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }
}