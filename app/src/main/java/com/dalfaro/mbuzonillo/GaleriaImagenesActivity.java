package com.dalfaro.mbuzonillo;

import static com.dalfaro.comun.MQTT.TAG;
import static com.dalfaro.comun.MQTT.broker;
import static com.dalfaro.comun.MQTT.clientId;
import static com.dalfaro.comun.MQTT.qos;
import static com.dalfaro.comun.MQTT.topicRoot;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.dalfaro.mbuzonillo.ui.tabs.Tab1;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.HashMap;
import java.util.Map;

public class GaleriaImagenesActivity extends AppCompatActivity  implements MqttCallback {


    private static MqttClient client;
    private FloatingActionButton fab;

    private AdaptadorImagenes adaptador;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.galeria_imagen);

        actionBarSetup("Galeria Fotos");

        RecyclerView recyclerView = findViewById(R.id.recyclerID);
        Query query = FirebaseFirestore.getInstance() .collection("fotos").orderBy("tiempo", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Imagen> opciones = new FirestoreRecyclerOptions .Builder<Imagen>().setQuery(query, Imagen.class).build();
        adaptador = new AdaptadorImagenes(this, opciones); recyclerView.setAdapter(adaptador);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        conectarMqtt();
        suscribirMqtt("camara_lista", this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            //fab.setEnabled(false);
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(view.getContext());
            circularProgressDrawable.setStrokeWidth(8f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.setStrokeCap(Paint.Cap.ROUND);
            circularProgressDrawable.start();
            //fab.setImageDrawable(circularProgressDrawable);
            publicarMqtt("foto", "1");
            Toast.makeText(this, "Haciendo foto...", Toast.LENGTH_SHORT).show();



        });

    }

    @Override public void onStart() {
        super.onStart();
        adaptador.startListening();
    }
    @Override public void onStop() {
        super.onStop();
        adaptador.stopListening();

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup(String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar ab = getSupportActionBar();
            ab.setTitle(title);
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //MQTT
    public static void conectarMqtt() {
        try {
            Log.i(TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.connect();
        } catch (MqttException e) {
            Log.e(TAG, "Error al conectar.", e);
        }
    }

    public static void suscribirMqtt(String topic, MqttCallback listener) {
        try {
            Log.i(TAG, "Suscrito a " + topicRoot + topic);
            client.subscribe(topicRoot + topic, qos);
            client.setCallback(listener);
        } catch (MqttException e) {
            Log.e(TAG, "Error al suscribir.", e);
        }
    }

    public static void publicarMqtt(String topic, String mensageStr) {
        try {
            MqttMessage message = new MqttMessage(mensageStr.getBytes());
            message.setQos(qos);
            message.setRetained(false);
            client.publish(topicRoot + topic, message);
            Log.i(TAG, "Publicando mensaje: " + topic+ "->"+mensageStr);
        } catch (MqttException e) {
            Log.e(TAG, "Error al publicar." + e);
        }
    }


    public static void deconectarMqtt() {
        try {
            client.disconnect();
            Log.i(TAG, "Desconectado");
        } catch (MqttException e) {
            Log.e(TAG, "Error al desconectar.", e);
        }
    }
    @Override
    public void onDestroy() {
        deconectarMqtt();
        super.onDestroy();
    }

    //MQTT Callback

    @Override public void connectionLost(Throwable cause) {
        Log.d(TAG, "Conexión perdida");
    }
    @Override public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "Entrega completa");
    }
    @Override public void messageArrived(String topic, MqttMessage message)
            throws Exception {

        String payload = new String(message.getPayload());

        if(payload.equals("1")){
            fab.setEnabled(true);
            fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.camara1));
            //Toast.makeText(this, "Foto realizada", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(this, "Foto realizada", Toast.LENGTH_SHORT).show();

    }
}
