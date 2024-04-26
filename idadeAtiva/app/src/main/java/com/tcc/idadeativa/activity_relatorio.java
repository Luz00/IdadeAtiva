package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;
import java.util.List;

public class activity_relatorio extends AppCompatActivity {

    private Spinner selectDoencasMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        AppCompatButton btnClock = findViewById(R.id.btnClock);
        AppCompatButton btnHome = findViewById(R.id.btnHome);
        selectDoencasMain = findViewById(R.id.selectDoencasMain);

        // Obtenha o ID da pessoa logada
        int idUsuario = getIdUsuarioLogado(); // Implemente este método para obter o ID da pessoa logada

        // Consulte as doenças associadas à pessoa logada
        List<String> nomesDoencas = consultarDoencasDoUsuario(idUsuario);

        // Preencha o spinner com as doenças encontradas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesDoencas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoencasMain.setAdapter(adapter);

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
        Intent intent = new Intent(this, activity_tela_alarme.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void abrirTelaHome(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    // Método para consultar as doenças associadas à pessoa logada
    private List<String> consultarDoencasDoUsuario(int idUsuario) {
        DAO dao = new DAO(getApplicationContext());
        List<String> nomesDoencas = dao.buscarDoencasDoUsuario(idUsuario);
        dao.close();
        return nomesDoencas;
    }

    // Método para obter o ID da pessoa logada
    private int getIdUsuarioLogado() {
        // Implemente a lógica para obter o ID da pessoa logada
        // Por exemplo, você pode obtê-lo do objeto Pessoa recebido na intent ou de uma sessão de usuário
        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
        return pessoa.getPessoa_ID();
    }
}