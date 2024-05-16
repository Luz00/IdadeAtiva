package com.tcc.idadeativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.widget.Toast;

public class AlarmeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String titulo = intent.getStringExtra("titulo");

        // Vamos usar o vibrador primeiro
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(4000);
        }

        Toast.makeText(context, "Alarme de " + titulo + " ! LEMBRETE !!", Toast.LENGTH_LONG).show();

        // Obter a URI do alarme padrão
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            // Se a URI do alarme for nula, obter a URI padrão de notificação
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }

        // Criar o objeto Ringtone com a URI obtida
        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        // Reproduzir o toque do alarme
        if (ringtone != null) {
            ringtone.play();
        }
    }
}
