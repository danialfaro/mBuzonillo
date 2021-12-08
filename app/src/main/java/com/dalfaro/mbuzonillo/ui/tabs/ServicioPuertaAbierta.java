package com.dalfaro.mbuzonillo.ui.tabs;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.ui.tabs.Tab1;

public class ServicioPuertaAbierta extends Service {

    private NotificationManager notificationManager;
    static final String CANAL_ID = "mi_canal";
    static final int NOTIFICACION_ID = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int idArranque) {
        lanzarNotificacion();
        return START_STICKY;
    }


    @Override public void onDestroy() {
        notificationManager.cancel(NOTIFICACION_ID);
    }

    public void lanzarNotificacion(){

        Intent intent = new Intent(this, Tab1.class);
        PendingIntent intentpending = PendingIntent.getActivities(this, 1, new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CANAL_ID, "Mis Notificaciones",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Descripcion del canal");
            notificationManager.createNotificationChannel(notificationChannel);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
        }

        NotificationCompat.Builder notificacion =
                new NotificationCompat.Builder(this, CANAL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("BUZöNillo")
                        .setContentText("La puerta está Abierta, recuerda cerrarla")
                        //.setAutoCancel(true)
                        .setContentIntent(intentpending);
        notificationManager.notify(NOTIFICACION_ID, notificacion.build());

        startForeground(NOTIFICACION_ID, notificacion.build());
    }
}
