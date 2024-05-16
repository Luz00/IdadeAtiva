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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class activity_TelaPrincipal extends AppCompatActivity {

    private AlertDialog alertDialog;
    private TextView ipt_NomeSUS;
    private TextView ipt_DataSUS;
    private TextView ipt_SexoSUS;
    private TextView ipt_NumeroSUS;
    private ImageView iv_User_home;
    private TextView ipt_NomeUser;
    private Spinner selectDoencasMain;
    private DAO dao;

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
        dao = new DAO(this);
        // Consulte as doenças associadas à pessoa logada
        List<String> nomesDoencas = consultarDoencasDoUsuario(idUsuario);

        // Preencha o spinner com as doenças encontradas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nomesDoencas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDoencasMain.setAdapter(adapter);

        /*CODIGO CHAMA O POP-UP DAS DOENÇA SELECIONADA*/

        btnMedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String doencaSelecionada = selectDoencasMain.getSelectedItem().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_TelaPrincipal.this);
                View view;

                switch (doencaSelecionada) {
                    case "Diabetes":
                        view = getLayoutInflater().inflate(R.layout.popup_medicao_diabetes, null);
                        builder.setView(view);

                        // Referenciar os elementos do layout personalizado
                        EditText editTextMedicao_Diabetes = view.findViewById(R.id.editTextMedicao);
                        AppCompatButton btnCancelar_Diabetes = view.findViewById(R.id.btnCancelar);
                        AppCompatButton btnSalvar_Diabetes = view.findViewById(R.id.btnSalvar);

                        // Configurar a ação do botão Cancelar
                        btnCancelar_Diabetes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        // Configurar a ação do botão Salvar
                        btnSalvar_Diabetes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Obter o valor digitado pelo usuário
                                String medição = editTextMedicao_Diabetes.getText().toString();
                                String dataAtual = obterDataHoraAtualBrasileira();
                                int IdDoenca = getIdDoenca("Diabetes");

                                boolean inseridoComSucesso = dao.inserirMedicao(dataAtual, Double.parseDouble(medição), idUsuario, IdDoenca);

                                if (inseridoComSucesso) {
                                    Toast.makeText(activity_TelaPrincipal.this, "Medição salva com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity_TelaPrincipal.this, "Erro ao salvar medição!", Toast.LENGTH_SHORT).show();
                                }

                                alertDialog.dismiss();
                            }
                        });
                        // Criar o AlertDialog
                        alertDialog = builder.create();
                        // Mostrar o AlertDialog
                        alertDialog.show();
                        break;

                    case "Arritimia":
                        view = getLayoutInflater().inflate(R.layout.popup_medicao_arritimia, null);
                        builder.setView(view);

                        // Referenciar os elementos do layout personalizado
                        EditText editTextMedicao_Arritimia = view.findViewById(R.id.editTextMedicao);
                        AppCompatButton btnCancelar_Arritimia = view.findViewById(R.id.btnCancelar);
                        AppCompatButton btnSalvar_Arritimia = view.findViewById(R.id.btnSalvar);

                        // Configurar a ação do botão Cancelar
                        btnCancelar_Arritimia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        // Configurar a ação do botão Salvar
                        btnSalvar_Arritimia.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String medição = editTextMedicao_Arritimia.getText().toString();
                                String dataAtual = obterDataHoraAtualBrasileira();
                                int IdDoenca = getIdDoenca("Arritimia");

                                boolean inseridoComSucesso = dao.inserirMedicao(dataAtual, Double.parseDouble(medição), idUsuario, IdDoenca);

                                if (inseridoComSucesso) {
                                    Toast.makeText(activity_TelaPrincipal.this, "Medição salva com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity_TelaPrincipal.this, "Erro ao salvar medição!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        // Criar o AlertDialog
                        alertDialog = builder.create();
                        // Mostrar o AlertDialog
                        alertDialog.show();
                        break;

                    case "Hipertensão":
                        view = getLayoutInflater().inflate(R.layout.popup_medicao_hipertensao, null);
                        builder.setView(view);

                        // Referenciar os elementos do layout personalizado
                        EditText editTextMedicao_Hipertensao1 = view.findViewById(R.id.iptValor1);
                        EditText editTextMedicao_Hipertensao2 = view.findViewById(R.id.iptValor2);

                        AppCompatButton btnCancelar_Hipertensao = view.findViewById(R.id.btnCancelar);
                        AppCompatButton btnSalvar_Hipertensao = view.findViewById(R.id.btnSalvar);

                        // Configurar a ação do botão Cancelar
                        btnCancelar_Hipertensao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        // Configurar a ação do botão Salvar
                        btnSalvar_Hipertensao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Obter o valor digitado pelo usuário
                                String medição1 = editTextMedicao_Hipertensao1.getText().toString();
                                String medição2 = editTextMedicao_Hipertensao2.getText().toString();

                                String mediçãoPressao = medição1 + "." + medição2;

                                String dataAtual = obterDataHoraAtualBrasileira();
                                int IdDoenca = getIdDoenca("Hipertensão");

                                boolean inseridoComSucesso = dao.inserirMedicao(dataAtual, Double.parseDouble(mediçãoPressao), idUsuario, IdDoenca);

                                if (inseridoComSucesso) {
                                    Toast.makeText(activity_TelaPrincipal.this, "Medição salva com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity_TelaPrincipal.this, "Erro ao salvar medição!", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();
                            }
                        });
                        // Criar o AlertDialog
                        alertDialog = builder.create();
                        // Mostrar o AlertDialog
                        alertDialog.show();
                        break;
                    case "Hipotensão":
                        view = getLayoutInflater().inflate(R.layout.popup_medicao_hipotensao, null);
                        builder.setView(view);

                        // Referenciar os elementos do layout personalizado
                        EditText editTextMedicao_hipotensao1 = view.findViewById(R.id.iptValor1);
                        EditText editTextMedicao_hipotensao2 = view.findViewById(R.id.iptValor2);
                        AppCompatButton btnCancelar_hipotensao = view.findViewById(R.id.btnCancelar);
                        AppCompatButton btnSalvar_hipotensao = view.findViewById(R.id.btnSalvar);

                        // Configurar a ação do botão Cancelar
                        btnCancelar_hipotensao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        // Configurar a ação do botão Salvar
                        btnSalvar_hipotensao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Obter o valor digitado pelo usuário
                                String medição1 = editTextMedicao_hipotensao1.getText().toString();
                                String medição2 = editTextMedicao_hipotensao2.getText().toString();

                                String mediçãoPressao = medição1 + "." + medição2;

                                String dataAtual = obterDataHoraAtualBrasileira();
                                int IdDoenca = getIdDoenca("Hipotensão");

                                boolean inseridoComSucesso = dao.inserirMedicao(dataAtual, Double.parseDouble(mediçãoPressao), idUsuario, IdDoenca);

                                if (inseridoComSucesso) {
                                    Toast.makeText(activity_TelaPrincipal.this, "Medição salva com sucesso!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(activity_TelaPrincipal.this, "Erro ao salvar medição!", Toast.LENGTH_SHORT).show();
                                }
                                alertDialog.dismiss();
                            }
                        });
                        // Criar o AlertDialog
                        alertDialog = builder.create();
                        // Mostrar o AlertDialog
                        alertDialog.show();
                        break;

                    case "Depressão":
                        view = getLayoutInflater().inflate(R.layout.popup_medicao_depressao, null);
                        builder.setView(view);

                        // Referenciar os elementos do layout personalizado
                        EditText editTextMedicao_depressao = view.findViewById(R.id.editTextMedicao);
                        AppCompatButton btnCancelar_Depressao = view.findViewById(R.id.btnCancelar);
                        AppCompatButton btnSalvar_Depressao = view.findViewById(R.id.btnSalvar);

                        // Configurar a ação do botão Cancelar
                        btnCancelar_Depressao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        // Configurar a ação do botão Salvar
                        btnSalvar_Depressao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Obter o valor digitado pelo usuário
                                String medição = editTextMedicao_depressao.getText().toString();

                                if (medição.equals("0") || medição.equals("1") || medição.equals("2") || medição.equals("3")) {
                                    // O valor está dentro do intervalo desejado, continuar com a inserção no banco de dados
                                    String dataAtual = obterDataHoraAtualBrasileira();
                                    int IdDoenca = getIdDoenca("Depressão");

                                    boolean inseridoComSucesso = dao.inserirMedicao(dataAtual, Double.parseDouble(medição), idUsuario, IdDoenca);

                                    if (inseridoComSucesso) {
                                        Toast.makeText(activity_TelaPrincipal.this, "Medição salva com sucesso!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity_TelaPrincipal.this, "Erro ao salvar medição!", Toast.LENGTH_SHORT).show();
                                    }
                                    alertDialog.dismiss();
                                } else {
                                    // O valor não está dentro do intervalo desejado, exibir uma mensagem de erro para o usuário
                                    Toast.makeText(activity_TelaPrincipal.this, "Por favor, insira um valor válido!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        // Criar o AlertDialog
                        alertDialog = builder.create();
                        // Mostrar o AlertDialog
                        alertDialog.show();
                        break;
                }
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

    public static String obterDataHoraAtualBrasileira() {
        // Define o formato desejado para a data e hora
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        // Obtém a data e hora atual
        Date dataHoraAtual = new Date(System.currentTimeMillis());
        // Formata a data e hora atual para o formato brasileiro
        return sdf.format(dataHoraAtual);
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