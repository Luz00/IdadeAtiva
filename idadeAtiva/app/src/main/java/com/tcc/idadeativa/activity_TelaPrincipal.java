package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

public class activity_TelaPrincipal extends AppCompatActivity {

    private AlertDialog alertDialog;
    private TextView ipt_NomeSUS;
    private TextView ipt_DataSUS;
    private TextView ipt_SexoSUS;
    private TextView ipt_NumeroSUS;
    private ImageView iv_User_home;
    private TextView ipt_NomeUser;
    private Spinner selectDoencasMain;

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
        ipt_NomeUser = findViewById(R.id.ipt_NomeUser);
        AppCompatButton btnMedir = findViewById(R.id.btnMedir);
        selectDoencasMain = findViewById(R.id.selectDoencasMain);

        // Obtenha o ID da pessoa logada
        int idUsuario = getIdUsuarioLogado(); // Implemente este método para obter o ID da pessoa logada

        // Consulte as doenças associadas à pessoa logada
        List<String> nomesDoencas = consultarDoencasDoUsuario(idUsuario);

        // Preencha o spinner com as doenças encontradas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesDoencas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoencasMain.setAdapter(adapter);

        /*CODIGO CHAMA O POP-UP DA DIABETES*/

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

        /*-----------------------------------------------------------------------------------------*/

        /*CÓDIGO QUE PEGA AS INFORMAÇÕES DO USUÁRIO E SETA NOS CAMPOS DO CARTÃO SUS*/

        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");

        ipt_NomeSUS.setText(pessoa.getPessoa_nomeSUS());
        ipt_DataSUS.setText(pessoa.getPessoa_dataNascimento());
        ipt_SexoSUS.setText(pessoa.getPessoa_sexo());
        ipt_NomeUser.setText(pessoa.getPessoa_nome());

        byte[] decodedString = Base64.decode(pessoa.getPessoa_foto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        iv_User_home.setImageBitmap(decodedByte);

        String numeroSUS = pessoa.getPessoa_numSUS();

        // Verifica se o número SUS é válido e possui 15 dígitos
        if (!TextUtils.isEmpty(numeroSUS) && numeroSUS.length() == 15) {
            // Formata o número SUS conforme especificação
            String formattedNumeroSUS = formatarNumeroSUS(numeroSUS);

            // Define o número SUS formatado no TextView ipt_NumeroSUS
            ipt_NumeroSUS.setText(formattedNumeroSUS);
        } else {
            // Se o número SUS não for válido, define o texto original
            ipt_NumeroSUS.setText(numeroSUS);
        }

        /*-----------------------------------------------------------------------------------------*/

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
        finish();
    }

    private void abrirTelaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void abrirTelaRelatorio(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_relatorio.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
    }

    private void abrirTelaAtualizar(Pessoa pessoa){
        Intent intent = new Intent(this, activity_atualizacao.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
    }

    private String formatarNumeroSUS(String numeroSUS) {
        // Formata o número SUS de acordo com a especificação
        return numeroSUS.substring(0, 3) + " " +
                numeroSUS.substring(3, 7) + " " +
                numeroSUS.substring(7, 11) + " " +
                numeroSUS.substring(11);
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