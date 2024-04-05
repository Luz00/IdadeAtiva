package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;

import java.util.Calendar;
import java.util.List;

public class activity_tela_alarme extends AppCompatActivity {

    private RadioButton rdBtnTemporario, rdBtnPermanente;
    private TextView tvPeriodo;
    private EditText lblNumeroDias;
    private static final String TAG = "activity_cadastro";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_alarme);

        dao = new DAO(this);
        List<Pessoa> pessoas = dao.buscaPessoa();

        /* CÓDIGO QUE FAZ A LÓGICA DE DESMARCAR OS RADIOSBUTOTN E SETAR OS ELEMENTOS VISIBLE */

        rdBtnTemporario = findViewById(R.id.rd_btn_temporario);
        rdBtnPermanente = findViewById(R.id.rd_btn_permanente);
        tvPeriodo = findViewById(R.id.tvPeriodo);
        lblNumeroDias = findViewById(R.id.lblNumeroDias);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        // Lógica para garantir que apenas um RadioButton pode ser selecionado

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rd_btn_temporario) {
                    rdBtnPermanente.setChecked(false);
                    tvPeriodo.setVisibility(View.VISIBLE);
                    lblNumeroDias.setVisibility(View.VISIBLE);
                } else if (checkedId == R.id.rd_btn_permanente) {
                    rdBtnTemporario.setChecked(false);
                    tvPeriodo.setVisibility(View.INVISIBLE);
                    lblNumeroDias.setVisibility(View.INVISIBLE);
                }
            }
        });
        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE CRIA JANELA PARA DATA DE NASCIMENTO E SALVA NA TEXTVIEW */

        mDisplayDate = (TextView) findViewById(R.id.tvDateInicio);

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        activity_tela_alarme.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        mDateSetListener,
                        dia,mes,ano);
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: date: " + dayOfMonth + "/" + month + "/" + year);
                String data = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(data);
            }
        };

        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE ABRE A TELA PRINCIPAL QUANDO CLICA NO BOTÃO DA CASINHA */
        AppCompatButton btnHome = findViewById(R.id.btnHome);
        AppCompatButton btnRelatorio = findViewById(R.id.btnRelatorio);

        View.OnClickListener onClickListener = new View.OnClickListener() {
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

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaRelatorio();
            }
        };

        btnHome.setOnClickListener(onClickListener);
        btnRelatorio.setOnClickListener(onClickListener2);
        /* ------------------------------------------------------------------------------------ */
    }



    private void abrirTelaHome(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void abrirTelaRelatorio() {
        Intent intent = new Intent(this, activity_relatorio.class);
        startActivity(intent);
    }
}