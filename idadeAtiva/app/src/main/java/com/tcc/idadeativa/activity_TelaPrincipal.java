package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.tcc.idadeativa.objetos.Pessoa;

public class activity_TelaPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        AppCompatButton btnClock = findViewById(R.id.btnClock);
        AppCompatButton btnRelatorio = findViewById(R.id.btnRelatorio);
        AppCompatButton btnExit = findViewById(R.id.btnExit);
        AppCompatButton btnSettings = findViewById(R.id.btnSettings);
        EditText ipt_NomeSUS = findViewById(R.id.ipt_NomeSUS);
        EditText ipt_DataSUS = findViewById(R.id.ipt_DataSUS);
        EditText ipt_SexoSUS = findViewById(R.id.ipt_SexoSUS);
        EditText ipt_NumeroSUS = findViewById(R.id.ipt_NumeroSUS);

        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");

        ipt_NomeSUS.setText(pessoa.getPessoa_nomeSUS());
        ipt_DataSUS.setText(pessoa.getPessoa_dataNascimento());
        ipt_SexoSUS.setText(pessoa.getPessoa_sexo());
        ipt_NumeroSUS.setText(pessoa.getPessoa_numSUS());

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaAlarme();
            }
        };

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaRelatorio();
            }
        };

        View.OnClickListener onClickListener3 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaMain();
            }
        };

        View.OnClickListener onClickListener4 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_TelaPrincipal.this, activity_cadastro.class);
                intent.putExtra("visibility", View.VISIBLE);
                startActivity(intent);
            }
        };

        btnClock.setOnClickListener(onClickListener);
        btnRelatorio.setOnClickListener(onClickListener2);
        btnExit.setOnClickListener(onClickListener3);
        btnSettings.setOnClickListener(onClickListener4);
    }
    private void abrirTelaAlarme() {
        Intent intent = new Intent(this, activity_tela_alarme.class);
        startActivity(intent);
    }

    private void abrirTelaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void abrirTelaRelatorio() {
        Intent intent = new Intent(this, activity_relatorio.class);
        startActivity(intent);
    }

}