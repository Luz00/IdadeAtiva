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
import android.util.Base64;
import android.util.Log;
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
import java.util.Calendar;
import java.util.List;

public class activity_atualizacao extends AppCompatActivity {

    private static final String TAG = "activity_atualizacao";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private static final int REQUEST_IMAGE_PICK = 2;
    private ImageView ivUser;
    private String fotoString = "";
    private DAO dao;

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
        List<Pessoa> pessoas = dao.buscaPessoa();

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
                        // Excluir a conta do usuário

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
        ArrayAdapter<CharSequence> listViewAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_doencas,
                android.R.layout.simple_list_item_multiple_choice
        );
        multiSelectListView.setAdapter(listViewAdapter);
//        final Pessoa pessoa = new Pessoa();
//        multiSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Criar uma lista para armazenar as doenças selecionadas
//                List<String> selectedItems = new ArrayList<>();
//
//                // Iterar sobre todos os itens do ListView
//                for (int i = 0; i < parent.getCount(); i++) {
//                    // Verificar se o item na posição atual está marcado como selecionado
//                    if (multiSelectListView.isItemChecked(i)) {
//                        // Se estiver selecionado, adicionar o texto do item à lista de itens selecionados
//                        selectedItems.add(multiSelectListView.getItemAtPosition(i).toString());
//                    }
//                }
//                // Definir as doenças selecionadas na instância de Pessoa
//                pessoa.setPessoa_doenca(selectedItems);
//            }
//        });
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


    }
    private void abrirTelaPrincipal(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
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
                    Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 500, 500, true);
                    ivUser.setImageBitmap(resizedBitmap);
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