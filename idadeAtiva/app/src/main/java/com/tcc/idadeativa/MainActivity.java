package com.tcc.idadeativa;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton btnUser1 = findViewById(R.id.btnUser1);
        ImageButton btnUser2 = findViewById(R.id.btnUser2);
        ImageButton btnUser3 = findViewById(R.id.btnUser3);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirTelaCadastro();
            }
        };
        btnUser1.setOnClickListener(onClickListener);
        btnUser2.setOnClickListener(onClickListener);
        btnUser3.setOnClickListener(onClickListener);
    }
    private void abrirTelaCadastro() {
        Intent intent = new Intent(this, activity_cadastro.class);
        startActivity(intent);
    }
}