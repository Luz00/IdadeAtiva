package com.tcc.idadeativa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;

import java.util.ArrayList;
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
        int idUsuario = getIdUsuarioLogado();
        String doencaSelecionada = selectDoencasMain.getSelectedItem().toString();
        // Consulte as doenças associadas à pessoa logada
        List<String> nomesDoencas = consultarDoencasDoUsuario(idUsuario);

        // Preencha o spinner com as doenças encontradas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesDoencas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoencasMain.setAdapter(adapter);

        LineChart lineChart = findViewById(R.id.lineChart);

// Criar uma lista de entradas para os dados do gráfico
        List<Entry> entries = new ArrayList<>();
// Adicionar dados de exemplo (substitua isso pelos seus dados reais)
        entries.add(new Entry(0, 10));
        entries.add(new Entry(1, 20));
        entries.add(new Entry(2, 15));
// ...

// Criar um conjunto de dados com os valores e uma etiqueta
        LineDataSet dataSet = new LineDataSet(entries, "Valores de medição");

// Criar uma lista de rótulos de data para o eixo X (substitua isso pelos seus rótulos de data reais)
        ArrayList<String> dates = new ArrayList<>();
        dates.add("01/01/2023");
        dates.add("02/01/2023");
        dates.add("03/01/2023");
// ...

// Criar um conjunto de dados de rótulos de data para o eixo X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

// Criar um conjunto de dados de valores de medição
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false); // Desativar o eixo direito

// Adicionar o conjunto de dados ao gráfico
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

// Exibir o gráfico
        lineChart.invalidate();






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
        finish();
    }

    private void abrirTelaHome(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
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

    private int getIdDoenca(String doenca) {
        int idDoenca = 0;
        if (doenca.equals("Arritimia")) {
            idDoenca = 1;
        }
        else if (doenca.equals("Depressão")) {
            idDoenca = 2;
        }
        else if (doenca.equals("Diabetes")) {
            idDoenca = 3;
        }
        else if (doenca.equals("Hipotensão")) {
            idDoenca = 5;
        }
        else if (doenca.equals("Hipertensão")) {
            idDoenca = 4;
        }
        return idDoenca;
    }
}