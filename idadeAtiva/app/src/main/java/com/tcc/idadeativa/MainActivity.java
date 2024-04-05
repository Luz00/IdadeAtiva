package com.tcc.idadeativa;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnUser1 = findViewById(R.id.btnUser1);
        ImageButton btnUser2 = findViewById(R.id.btnUser2);
        ImageButton btnUser3 = findViewById(R.id.btnUser3);

        dao = new DAO(this);
        List<Pessoa> pessoas = dao.buscaPessoa();

        if (!pessoas.isEmpty()) {
            if (pessoas.size() >= 1) {
                byte[] decodedString = Base64.decode(pessoas.get(0).getPessoa_foto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                btnUser1.setImageBitmap(decodedByte);
                btnUser1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirActivityPrincipal(pessoas.get(0));
                        finish();
                    }
                });

            } else {
                btnUser1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirActivityCadastro();
                        finish();
                    }
                });
            }
            if (pessoas.size() >= 2) {
                byte[] decodedString = Base64.decode(pessoas.get(1).getPessoa_foto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                btnUser2.setImageBitmap(decodedByte);
                btnUser2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirActivityPrincipal(pessoas.get(1));
                        finish();
                    }
                });
            } else {
                btnUser2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirActivityCadastro();
                        finish();
                    }
                });
            }
            if (pessoas.size() >= 3) {
                byte[] decodedString = Base64.decode(pessoas.get(2).getPessoa_foto(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                btnUser3.setImageBitmap(decodedByte);
                btnUser3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirActivityPrincipal(pessoas.get(2));
                        finish();
                    }
                });
            } else {
                btnUser3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrirActivityCadastro();
                        finish();
                    }
                });
            }
        } else {
            // Nenhum usuário cadastrado, definir a ação de clique para abrir a activity_cadastro
            btnUser1.setImageResource(R.drawable.user); // Defina a imagem padrão aqui
            btnUser1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirActivityCadastro();
                    finish();
                }
            });
            btnUser2.setImageResource(R.drawable.user); // Defina a imagem padrão aqui
            btnUser2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirActivityCadastro();
                    finish();
                }
            });
            btnUser3.setImageResource(R.drawable.user); // Defina a imagem padrão aqui
            btnUser3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abrirActivityCadastro();
                    finish();
                }
            });
        }
    }

        private void abrirActivityCadastro () {
            Intent intent = new Intent(this, activity_cadastro.class);
            startActivity(intent);
        }

        private void abrirActivityPrincipal(Pessoa pessoa) {
            Intent intent = new Intent(this, activity_TelaPrincipal.class);
            intent.putExtra("pessoa", pessoa);
            startActivity(intent);
        }
}