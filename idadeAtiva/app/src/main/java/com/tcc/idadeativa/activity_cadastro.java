package com.tcc.idadeativa;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.net.Uri;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class activity_cadastro extends Activity {

    private static final String TAG = "activity_cadastro";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView ivUser;
    private String fotoString = "";
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

        // Configure o Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_sexo,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        singleSelectSpinner.setAdapter(spinnerAdapter);

        // Configure o ListView para seleção múltipla
        ArrayAdapter<CharSequence> listViewAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_doencas,
                android.R.layout.simple_list_item_multiple_choice
        );
        multiSelectListView.setAdapter(listViewAdapter);
        multiSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Lógica para manipular seleções no ListView
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
                    dao.inserePessoa(pessoa);
                    dao.close();

                    lblNome.setText("");
                    mDisplayDate.setText("");
                    lblNomeSus.setText("");
                    lblNumeroCartao.setText("");

                    Toast.makeText(getApplicationContext(), "Usuário cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        /* ------------------------------------------------------------------------------------ */
    }

    private void abrirTelaInicial() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    ivUser.setImageBitmap(bitmap);

                    byte[] fotoemBytes;
                    ByteArrayOutputStream streamFoto = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, streamFoto);
                    fotoemBytes = streamFoto.toByteArray();
                    fotoString = Base64.encodeToString(fotoemBytes, Base64.DEFAULT);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}