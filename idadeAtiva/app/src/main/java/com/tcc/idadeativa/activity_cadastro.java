package com.tcc.idadeativa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.Toast;
import android.net.Uri;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class activity_cadastro extends Activity {

    private static final String TAG = "activity_cadastro";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView ivUser;
    private String fotoString = "";
    private DAO dao;
    private Pessoa pessoa;

    private List<Integer> doencasSelecionadas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        EditText lblNome = findViewById(R.id.lblNome);
        EditText lblNomeSus = findViewById(R.id.lblNomeSus);
        Spinner singleSelectSpinner = findViewById(R.id.singleSelectSpinner);
        ListView multiSelectListView = findViewById(R.id.multiSelectListView);
        TextView mDisplayDate = findViewById(R.id.tvDate);
        EditText lblNumeroCartao = findViewById(R.id.lblNumeroCartao);
        AppCompatButton btnCancelar = findViewById(R.id.btnCancelar);
        AppCompatButton btnConfirmar = findViewById(R.id.btnConfirmar);
        AppCompatButton btnFoto = findViewById(R.id.btnFoto);

        dao = new DAO(this);
        List<String> nomesDoencas = dao.buscarNomesDoencas();

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



        /* CÓDIGO FOTO */

        ivUser = findViewById(R.id.iv_User);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escolherFonteDaFoto();
            }
        });

        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE VOLTA PARA A TELA INICIAL SE APERTAR O BOTAO CANCELAR */

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaInicial();
            }
        };
        btnCancelar.setOnClickListener(onClickListener);
        /* ------------------------------------------------------------------------------------ */

        /* CÓDIGO QUE CRIA O SPINNER COM AS OPÇÕES E TAMBÉM O LISTVIEW */


        // Configure o Spinner para o sexo
        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(
                this,
                R.array.array_sexo,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        singleSelectSpinner.setAdapter(spinnerAdapter1);

        // Configure o ListView para seleção múltipla
        ArrayAdapter<String> multiSelectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, nomesDoencas);
        multiSelectListView.setAdapter(multiSelectAdapter);
        multiSelectListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        multiSelectListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Verifique se o evento é um clique fora do ListView
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // Processar os itens selecionados
                    SparseBooleanArray checkedItems = multiSelectListView.getCheckedItemPositions();
                    for (int i = 0; i < multiSelectListView.getCount(); i++) {
                        if (checkedItems.get(i)) {
                            // O item na posição i está selecionado
                            // Faça algo com o item selecionado, por exemplo, adicione-o a uma lista
                        }
                    }
                }
                return false;
            }
        });



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
                        activity_cadastro.this,
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
        /* CÓDIGO QUE QUANDO APERTAR O BOTÃO CONFIRMAR ELE PEGA AS INFOS DOS CAMPOS E SALVA NO BANCO */

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(lblNome.getText().toString().equals("") || singleSelectSpinner.getSelectedItem().equals("") || mDisplayDate.getText().toString().equals("") || lblNomeSus.getText().toString().equals("") || lblNumeroCartao.getText().toString().equals(""))) {
                    DAO dao = new DAO(getApplicationContext());
                    Pessoa pessoa = new Pessoa();
                    pessoa.setPessoa_nome(lblNome.getText().toString());
                    String sexoSelecionado = singleSelectSpinner.getSelectedItem().toString();
                    pessoa.setPessoa_sexo(sexoSelecionado);
                    pessoa.setPessoa_dataNascimento(mDisplayDate.getText().toString());
                    pessoa.setPessoa_nomeSUS(lblNomeSus.getText().toString());
                    pessoa.setPessoa_numSUS(lblNumeroCartao.getText().toString());
                    pessoa.setPessoa_foto(fotoString);

                    long idUsuario = dao.inserePessoa(pessoa); // Insere a pessoa e obtém o ID do usuário inserido

                    // Insere as doenças associadas ao usuário
                    SparseBooleanArray checkedItems = multiSelectListView.getCheckedItemPositions();
                    for (int i = 0; i < multiSelectListView.getCount(); i++) {
                        if (checkedItems.get(i)) {
                            int idDoenca = getIdDoencaFromPosition(i); // Obtenha o ID da doença com base na posição no ListView
                            dao.inserePessoaDoenca((int) idUsuario, idDoenca); // Insere a associação entre usuário e doença
                        }
                    }

                    dao.close();

                    lblNome.setText("");
                    mDisplayDate.setText("");
                    lblNomeSus.setText("");
                    lblNumeroCartao.setText("");

                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                    abrirTelaInicial();
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* ------------------------------------------------------------------------------------ */
    }

    private int getIdDoencaFromPosition(int position) {
        // Retorna a posição como o ID da doença
        return position + 1; // Assumindo que os IDs começam de 1
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                try {
                    Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 400, 400, true);
                    Bitmap circularBitmap = getCircleBitmap(resizedBitmap);
                    ivUser.setImageBitmap(circularBitmap); // Exibe a imagem de forma circular no ImageView
                    ByteArrayOutputStream streamFoto = new ByteArrayOutputStream();
                    circularBitmap.compress(Bitmap.CompressFormat.PNG, 50, streamFoto);
                    byte[] fotoemBytes = streamFoto.toByteArray();
                    fotoString = Base64.encodeToString(fotoemBytes, Base64.DEFAULT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    // Método para exibir a imagem de forma circular no ImageView
    private void setCircularImage(ImageView imageView, Bitmap bitmap) {
        if (bitmap != null) {
            Bitmap circularBitmap = getCircleBitmap(bitmap);
            imageView.setImageBitmap(circularBitmap);
        }
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap circleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));

        canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint);

        return circleBitmap;
    }
}