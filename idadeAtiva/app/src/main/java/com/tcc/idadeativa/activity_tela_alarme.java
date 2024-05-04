package com.tcc.idadeativa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tcc.idadeativa.objetos.Pessoa;

import java.util.Calendar;

public class activity_tela_alarme extends AppCompatActivity {

    TimePicker alarmTimePicker;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    EditText lblTitulo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_alarme);

        /* CÓDIGO QUE ABRE A TELA PRINCIPAL QUANDO CLICA NO BOTÃO DA CASINHA */
        AppCompatButton btnHome = findViewById(R.id.btnHome);
        AppCompatButton btnRelatorio = findViewById(R.id.btnRelatorio);

        alarmTimePicker = (TimePicker) findViewById(R.id.timePicker);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        lblTitulo = findViewById(R.id.lblTitulo);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                abrirTelaHome(pessoa);
            }
        };

        View.OnClickListener onClickListener2 = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pessoa pessoa = (Pessoa) getIntent().getSerializableExtra("pessoa");
                abrirTelaRelatorio(pessoa);
            }
        };

        btnHome.setOnClickListener(onClickListener);
        btnRelatorio.setOnClickListener(onClickListener2);
        /* ------------------------------------------------------------------------------------ */
    }

    private void abrirTelaHome(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_TelaPrincipal.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
    }

    private void abrirTelaRelatorio(Pessoa pessoa) {
        Intent intent = new Intent(this, activity_relatorio.class);
        intent.putExtra("pessoa", pessoa);
        startActivity(intent);
        finish();
    }

    public void OnToggleClicked(View view) {
        long time;
        if (((ToggleButton) view).isChecked()) {
            Toast.makeText(activity_tela_alarme.this, "Alarme Definido!", Toast.LENGTH_SHORT).show();
            Calendar calendar = Calendar.getInstance();

            // calendar is called to get current time in hour and minute
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
            calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());

            // using intent i have class AlarmReceiver class which inherits
            // BroadcastReceiver
            Intent intent = new Intent(this, AlarmeReceiver.class);
            intent.putExtra("titulo", lblTitulo.getText().toString()); // lblTitulo é o EditText
            // we call broadcast using pendingIntent
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

            time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                // setting time as AM and PM
                if (Calendar.AM_PM == 0)
                    time = time + (1000 * 60 * 60 * 12);
                else
                    time = time + (1000 * 60 * 60 * 24);
            }
            // Alarm rings continuously until toggle button is turned off
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent);
            // alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 1000), pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(activity_tela_alarme.this, "Alarme Cancelado!", Toast.LENGTH_SHORT).show();
        }
    }
}