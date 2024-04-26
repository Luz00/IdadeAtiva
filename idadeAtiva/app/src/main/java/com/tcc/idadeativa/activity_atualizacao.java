package com.tcc.idadeativa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class activity_atualizacao extends AppCompatActivity {

    private static final String TAG = "activity_atualizacao";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView ivUser;
    private String fotoString = "";
    private DAO dao;
    private Pessoa pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizacao);

        EditText lblNome = findViewById(R.id.lblNome);
        EditText lblNomeSus = findViewById(R.id.lblNomeSus);
        Spinner singleSelectSpinner = findViewById(R.id.singleSelectSpinner);
        ListView multiSelectListView = findViewById(R.id.multiSelectListView);
        TextView mDisplayDate = findViewById(R.id.tvDate);
        EditText lblNumeroCartao = findViewById(R.id.lblNumeroCartao);
        AppCompatButton btnFoto = findViewById(R.id.btnFoto);
        AppCompatButton btnDrop = findViewById(R.id.btnDrop);
        AppCompatButton btnAtualizar = findViewById(R.id.btnAtualizar);
        AppCompatButton btnVoltar = findViewById(R.id.btnVoltar);
        ivUser = findViewById(R.id.iv_User);

        dao = new DAO(this);
        List<String> nomesDoencas = dao.buscarNomesDoencas();
        int idUsuario = getIdUsuarioLogado();
        List<String> doencasDoUsuario = dao.buscarDoencasDoUsuario(idUsuario);
        String sexoPessoa = dao.buscarSexoDaPessoa(idUsuario);

        lblNumeroCartao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Não é necessário implementar nada aqui
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (text.length() > 15) {
                    // Se o texto for maior que 15 caracteres, remove os caracteres extras
                    lblNumeroCartao.setText(text.substring(0, 15));
                    lblNumeroCartao.setSelection(15); // Move o cursor para o final do texto
                }
            }
        });

        /*CÓDIGO PARA SETAR OS VALORES DO USUÁRIO NOS CAMPOS*/

        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");

        if (pessoa != null) {
            // Preencher os campos com os dados da pessoa
            lblNome.setText(pessoa.getPessoa_nome());
            lblNomeSus.setText(pessoa.getPessoa_nomeSUS());
            mDisplayDate.setText(pessoa.getPessoa_dataNascimento());
            lblNumeroCartao.setText(pessoa.getPessoa_numSUS());

            byte[] decodedString = Base64.decode(pessoa.getPessoa_foto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivUser.setImageBitmap(decodedByte);
        }

        /*--------------------------------------------------------------------------------*/

        /* AÇÃO DO BOTÃO QUE ABRE A GALERIA PARA ESCOLHER FOTO */

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFonteDaFoto();
            }
        });

        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE CRIA O POPUP DE CONFIRMAÇÃO DE EXCLUSÃO DA CONTA DE USUÁRIO */

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity_atualizacao.this);
                builder.setTitle("Deseja realmente excluir sua conta?");
                builder.setMessage("Essa ação é irreversível!");
                // Adiciona o botão "Cancelar"
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Fecha o diálogo
                        dialog.dismiss();
                    }
                });
                // Adiciona o botão "Confirmar"
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean excluidoComSucesso = dao.excluirPessoa(pessoa.getPessoa_ID());

                        // Verificar se a exclusão foi bem-sucedida
                        if (excluidoComSucesso) {
                            Toast.makeText(activity_atualizacao.this, "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show();
                            abrirTelaInicial();
                        } else {
                            // Exibir mensagem de erro
                            Toast.makeText(activity_atualizacao.this, "Erro ao excluir conta!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE CRIA O SPINNER COM AS OPÇÕES E TAMBÉM O LISTVIEW */

        // Configure o Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_sexo,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        singleSelectSpinner.setAdapter(spinnerAdapter);

        // Configure o ListView para seleção múltipla
        ArrayAdapter<String> multiSelectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, nomesDoencas);
        multiSelectListView.setAdapter(multiSelectAdapter);
        multiSelectListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        for (int i = 0; i < nomesDoencas.size(); i++) {
            String doenca = nomesDoencas.get(i);
            if (doencasDoUsuario.contains(doenca)) {
                multiSelectListView.setItemChecked(i, true);
            }
        }
        multiSelectListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    SparseBooleanArray checkedItems = multiSelectListView.getCheckedItemPositions();
                    for (int i = 0; i < multiSelectListView.getCount(); i++) {
                        if (checkedItems.get(i)) {
                        }
                    }
                }
                return false;
            }
        });

        if (sexoPessoa != null) {
            int index = spinnerAdapter.getPosition(sexoPessoa);
            singleSelectSpinner.setSelection(index);
        }

        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE CRIA JANELA PARA DATA DE NASCIMENTO E SALVA NA TEXTVIEW */

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int ano = cal.get(Calendar.YEAR);
                int mes = cal.get(Calendar.MONTH);
                int dia = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        activity_atualizacao.this,
                        android.R.style.Theme_DeviceDefault_Dialog,
                        mDateSetListener,
                        dia, mes, ano);
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

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                abrirTelaPrincipal(pessoa);
            }
        });

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(lblNome.getText().toString().equals("") || singleSelectSpinner.getSelectedItem().equals("") || mDisplayDate.getText().toString().equals("") || lblNomeSus.getText().toString().equals("") || lblNumeroCartao.getText().toString().equals(""))){
                    // Capturar os dados dos campos da tela
                    String nome = lblNome.getText().toString();
                    String nomeSus = lblNomeSus.getText().toString();
                    String dataNascimento = mDisplayDate.getText().toString();
                    String numeroCartao = lblNumeroCartao.getText().toString();
                    String sexo = singleSelectSpinner.getSelectedItem().toString();

                    // Atualizar o objeto Pessoa com os novos dados
                    pessoa.setPessoa_nome(nome);
                    pessoa.setPessoa_nomeSUS(nomeSus);
                    pessoa.setPessoa_dataNascimento(dataNascimento);
                    pessoa.setPessoa_numSUS(numeroCartao);
                    pessoa.setPessoa_sexo(sexo);

                    // Atualizar os dados no banco de dados
                    boolean atualizadoComSucesso = dao.atualizarPessoa(pessoa);

                    // Obter ID do usuário logado
                    int idUsuario = getIdUsuarioLogado();

                    // Obter as doenças selecionadas no MultiSelectListView
                    SparseBooleanArray checkedItems = multiSelectListView.getCheckedItemPositions();
                    List<String> nomesDoencasSelecionadas = new ArrayList<>();
                    for (int i = 0; i < multiSelectListView.getCount(); i++) {
                        if (checkedItems.get(i)) {
                            nomesDoencasSelecionadas.add(multiSelectAdapter.getItem(i));
                        }
                    }
                    // Atualize a foto no objeto Pessoa
                    pessoa.setPessoa_foto(fotoString);

                    // Atualize a foto no banco de dados
                    boolean atualizadoComSucessoFOTO = dao.atualizarFotoPessoa(pessoa);

                    // Atualizar associações Pessoa-Doença
                    dao.atualizarPessoaDoenca(idUsuario, nomesDoencasSelecionadas);

                    if (atualizadoComSucesso) {
                        Toast.makeText(activity_atualizacao.this, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                        abrirTelaPrincipal(pessoa);
                    }
                }
                else {
                    Toast.makeText(activity_atualizacao.this, "Erro ao atualizar dados! Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void abrirTelaPrincipal(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
    }

    private void abrirTelaInicial() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void escolherFonteDaFoto() {
        Intent escolherFotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(escolherFotoIntent, REQUEST_IMAGE_PICK);
    }

    private int getIdUsuarioLogado() {
        // Implemente a lógica para obter o ID da pessoa logada
        // Por exemplo, você pode obtê-lo do objeto Pessoa recebido na intent ou de uma sessão de usuário
        Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
        return pessoa.getPessoa_ID();
    }

    // Dentro do método onActivityResult após selecionar a imagem
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, true);
                    ivUser.setImageBitmap(resizedBitmap);

                    // Converta a imagem para uma string Base64
                    ByteArrayOutputStream streamFoto = new ByteArrayOutputStream();
                    resizedBitmap.compress(Bitmap.CompressFormat.PNG, 70, streamFoto);
                    byte[] fotoemBytes = streamFoto.toByteArray();
                    fotoString = Base64.encodeToString(fotoemBytes, Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}