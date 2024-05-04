package com.tcc.idadeativa;

import static com.google.android.material.transition.MaterialSharedAxis.X;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.tcc.idadeativa.objetos.Medicao;
import com.tcc.idadeativa.objetos.Pessoa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class activity_relatorio extends AppCompatActivity {

    private Spinner selectDoencasMain;
    private DAO dao;
    private TextView nbrMedia, nbrMaxima, nbrMinimo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);

        AppCompatButton btnClock = findViewById(R.id.btnClock);
        AppCompatButton btnHome = findViewById(R.id.btnHome);
        selectDoencasMain = findViewById(R.id.selectDoencasMain);
        dao = new DAO(this);
        LineChart lineChart = findViewById(R.id.lineChart);
        nbrMedia = findViewById(R.id.nbrMedia);
        nbrMaxima = findViewById(R.id.nbrMaxima);
        nbrMinimo = findViewById(R.id.nbrMinimo);

//        String doencaSelecionada = selectDoencasMain.getSelectedItem().toString();
        // Consulte as doenças associadas à pessoa logada
        int idUsuario = getIdUsuarioLogado();
        List<String> nomesDoencas = consultarDoencasDoUsuario(idUsuario);

        // Preencha o spinner com as doenças encontradas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesDoencas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoencasMain.setAdapter(adapter);

        selectDoencasMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Obtenha o nome da doença selecionada
                String doencaSelecionada = selectDoencasMain.getSelectedItem().toString();

                // Obtenha o ID da doença com base no nome selecionado
                int idDoenca = getIdDoenca(doencaSelecionada);

                List<Medicao> medicoes = dao.obterMedicoesPorDoencaEUsuario(idDoenca, idUsuario);
                List<Entry> entries = new ArrayList<>();
                List<String> labels = new ArrayList<>();
                for (Medicao medicao : medicoes) {
                    double valor = medicao.getMedicao_valor();
                    Date data = medicao.getMedicao_data();
                    String dataFormatada = new SimpleDateFormat("dd/MM/yy — HH:mm").format(data);
                    entries.add(new Entry(entries.size(), (float) valor));
                    labels.add(dataFormatada);
                }
                LineDataSet dataSet = new LineDataSet(entries, "Valores de Medições");
                dataSet.setColor(Color.parseColor("#043fc3")); // Definindo a cor da linha
                dataSet.setLineWidth(2.5f);
                dataSet.setCircleColor(Color.parseColor("#c30407"));
                LineData lineData = new LineData(dataSet);
                lineChart.setDescription(null);

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelRotationAngle(-75);
                lineChart.setData(lineData);
                lineChart.invalidate();

              double valorMedia = dao.obterSomaUltimosSeteValores(idDoenca, idUsuario);
              double valorMediaDiv = valorMedia/7;
              String textoMedia = String.valueOf((int) valorMediaDiv);
              nbrMedia.setText(textoMedia);

              double valorMaximo = dao.obterMaiorValorUltimasSeteMedicoes(idDoenca, idUsuario);
              String textoMaximo = String.valueOf((int) valorMaximo);
              nbrMaxima.setText(textoMaximo);

              double valorMinimo = dao.obterMenorValorUltimasSeteMedicoes(idDoenca, idUsuario);
              String textoMinimo = String.valueOf((int) valorMinimo);
              nbrMinimo.setText(textoMinimo);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Não é necessário fazer nada se nada estiver selecionado
            }
        });

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