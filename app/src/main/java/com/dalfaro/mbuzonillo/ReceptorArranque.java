package com.dalfaro.mbuzonillo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.dalfaro.mbuzonillo.ui.tabs.ServicioPuertaAbierta;

public class ReceptorArranque extends BroadcastReceiver {
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startForegroundService(new Intent(context, ServicioPuertaAbierta.class));
    }
}