package com.tcc.idadeativa;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageButton;
import android.view.View;
import com.tcc.idadeativa.DAO.DAO;
import com.tcc.idadeativa.objetos.Pessoa;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DAO dao;
    private ImageButton[] userButtons = new ImageButton[3]; // Array para armazenar os botões de usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userButtons[0] = findViewById(R.id.btnUser1);
        userButtons[1] = findViewById(R.id.btnUser2);
        userButtons[2] = findViewById(R.id.btnUser3);

        dao = new DAO(this);
        List<Pessoa> pessoas = dao.buscaPessoa();

        // Verificar se há usuários cadastrados
        for (int i = 0; i < pessoas.size(); i++) {
            if (i < userButtons.length) {
                setUserData(userButtons[i], pessoas.get(i));
            }
        }

        // Definir a ação de clique para cada botão de usuário
        for (int i = 0; i < userButtons.length; i++) {
            final int index = i;
            userButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pessoas.size() > index) {
                        abrirActivityPrincipal(pessoas.get(index));
                    } else {
                        abrirActivityCadastro();
                    }
                }
            });
        }
    }

    // Método para configurar a imagem e o nome do usuário em um botão de imagem
    private void setUserData(ImageButton button, Pessoa pessoa) {
        byte[] decodedString = Base64.decode(pessoa.getPessoa_foto(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        button.setImageBitmap(decodedByte);
    }

    // Método para abrir a activity de cadastro
    private void abrirActivityCadastro() {
        Intent intent = new Intent(this, activity_cadastro.class);
        startActivity(intent);
        finish();
    }

    // Método para abrir a activity principal com os detalhes do usuário
    private void abrirActivityPrincipal(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
    }
}