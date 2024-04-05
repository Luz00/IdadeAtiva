package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.tcc.idadeativa.objetos.Pessoa;

public class activity_TelaPrincipal extends AppCompatActivity {

    private AlertDialog alertDialog;
    private TextView ipt_NomeSUS;
    private TextView ipt_DataSUS;
    private TextView ipt_SexoSUS;
    private TextView ipt_NumeroSUS;
    private ImageView iv_User_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        AppCompatButton btnClock = findViewById(R.id.btnClock);
        AppCompatButton btnRelatorio = findViewById(R.id.btnRelatorio);
        AppCompatButton btnExit = findViewById(R.id.btnExit);
        AppCompatButton btnSettings = findViewById(R.id.btnSettings);
        ipt_NomeSUS = findViewById(R.id.ipt_NomeSUS);
        ipt_DataSUS = findViewById(R.id.ipt_DataSUS);
        ipt_SexoSUS = findViewById(R.id.ipt_SexoSUS);
        ipt_NumeroSUS = findViewById(R.id.ipt_NumeroSUS);
        iv_User_home = findViewById(R.id.iv_User_home);
        AppCompatButton btnMedir = findViewById(R.id.btnMedir);



        /*CODIGO TESTE POPUP*/

        btnMedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Criar um AlertDialog.Builder
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_TelaPrincipal.this);

                // Inflar o layout personalizado para o AlertDialog
                View view = getLayoutInflater().inflate(R.layout.popup_medicao_diabetes, null);
                builder.setView(view);

                // Referenciar os elementos do layout personalizado
                EditText editTextMedicao = view.findViewById(R.id.editTextMedicao);
                AppCompatButton btnCancelar = view.findViewById(R.id.btnCancelar);
                AppCompatButton btnSalvar = view.findViewById(R.id.btnSalvar);

                // Configurar a ação do botão Cancelar
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Fechar o AlertDialog
                        alertDialog.dismiss();
                    }
                });

                // Configurar a ação do botão Salvar
                btnSalvar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obter o valor digitado pelo usuário
                        String medição = editTextMedicao.getText().toString();

                        // Você pode fazer o que quiser com o valor digitado, como salvá-lo em um banco de dados ou exibi-lo em outro lugar na atividade
                        // Fechar o AlertDialog
                        alertDialog.dismiss();
                    }
                });

                // Criar o AlertDialog
                alertDialog = builder.create();

                // Mostrar o AlertDialog
                alertDialog.show();
            }
        });

        /*-------------------*/

        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");

        ipt_NomeSUS.setText(pessoa.getPessoa_nomeSUS());
        ipt_DataSUS.setText(pessoa.getPessoa_dataNascimento());
        ipt_SexoSUS.setText(pessoa.getPessoa_sexo());
        ipt_NumeroSUS.setText(pessoa.getPessoa_numSUS());

        byte[] decodedString = Base64.decode(pessoa.getPessoa_foto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv_User_home.setImageBitmap(decodedByte);

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
                abrirTelaRelatorio(pessoa);
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
                Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                abrirTelaAtualizar(pessoa);
                visualizarCampos();
            }
        };

        btnClock.setOnClickListener(onClickListener);
        btnRelatorio.setOnClickListener(onClickListener2);
        btnExit.setOnClickListener(onClickListener3);
        btnSettings.setOnClickListener(onClickListener4);
    }
    private void abrirTelaAlarme(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_tela_alarme.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void abrirTelaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void abrirTelaRelatorio(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_relatorio.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void abrirTelaAtualizar(Pessoa pessoa){
        Intent intent = new Intent(this, activity_cadastro.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
    }

    private void visualizarCampos(){
        Intent intent2 = new Intent(activity_TelaPrincipal.this, activity_cadastro.class);
        intent2.putExtra("visibility", View.VISIBLE);
        intent2.putExtra("invisible", View.INVISIBLE);
        startActivity(intent2);
    }
}