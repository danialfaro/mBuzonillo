package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.dalfaro.mbuzonillo.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import static com.dalfaro.comun.MQTT.*;


public class Tab1 extends Fragment implements MqttCallback {

    private TextView puertadb;
    private TextView pesodb;
    private TextView iluminaciondb;

    boolean estado_iluminacion;
    ImageView image;
    ImageView imageCerradura;

    private static MqttClient client;
    private String estadoPuertaMQTT = "2";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);

        imageCerradura = view.findViewById(R.id.image_puerta);

        puertadb = view.findViewById(R.id.puerta_abierta_cerrada);
        pesodb = view.findViewById(R.id.peso_kg);
        iluminaciondb = view.findViewById(R.id.iluminacion_encendido_apagado);
        obtenerdatos();

        conectarMqtt();

        suscribirMqtt("motor_estado", this);

        ConstraintLayout clp2 = view.findViewById(R.id.constraintLayoutPuerta);
        clp2.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                cambiarPuerta(view);
            }
        });

        ConstraintLayout clp4 = view.findViewById(R.id.constraintLayoutIluminacion);
        clp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarIluminacion(view);
            }
        });

        ImageView clp3 = view.findViewById(R.id.image_cambiar_camara);
        clp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verCamara(view);
            }
        });
        return view;
    }

    public void obtenerdatos(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("buzones").document("iuuL6GzS8k8uz042XkBy")
                .addSnapshotListener(
                        new EventListener<DocumentSnapshot>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                Log.d("Data:" , "" + value.getData().get("peso"));

                                String dato_peso = (String) value.getData().get("peso");
                                boolean dato_puerta = (boolean) value.getData().get("puerta");
                                boolean dato_iluminacion = (boolean) value.getData().get("iluminacion");


                                String estadoPuertaFirestore = (String) value.getData().get("estadoPuerta");
                                estadoPuertaMQTT = estadoPuertaFirestore;

                                switch (estadoPuertaMQTT) {

                                    case "0": //abierta

                                        puertadb.setText("Abierta");
                                        imageCerradura.setImageResource(R.drawable.puerta_abierta);
                                        getActivity().startForegroundService(new Intent(getActivity(), ServicioPuertaAbierta.class));
                                        break;
                                    case "1": //moviendose
                                        puertadb.setText("Moviendose");
                                        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
                                        circularProgressDrawable.setStrokeWidth(8f);
                                        circularProgressDrawable.setCenterRadius(30f);
                                        circularProgressDrawable.setStrokeCap(Paint.Cap.ROUND);
                                        circularProgressDrawable.setBackgroundColor(Color.argb(1, 0, 255, 0));
                                        circularProgressDrawable.start();
                                        imageCerradura.setImageDrawable(circularProgressDrawable);
                                        break;
                                    case "2": //cerrada
                                        puertadb.setText("Cerrada");
                                        imageCerradura.setImageResource(R.drawable.puerta_cerrada);
                                        getActivity().stopService(new Intent(getActivity(), ServicioPuertaAbierta.class));
                                        break;
                                }

                                pesodb.setText(dato_peso);

                                if (dato_iluminacion){
                                    iluminaciondb.setText("Encendido");
                                    estado_iluminacion = true;
                                }else{
                                    iluminaciondb.setText("Apagado");
                                    estado_iluminacion = false;
                                }

                            }
                        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cambiarPuerta(View view) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("buzones").document("iuuL6GzS8k8uz042XkBy");

        if(estadoPuertaMQTT.equals("1")) {
            return;
        }

        System.out.println("Estado al pulsar: " + estadoPuertaMQTT);

        if(estadoPuertaMQTT.equals("2")) {
            publicarMqtt("motor", "1");
        } else if(estadoPuertaMQTT.equals("0")) {
            publicarMqtt("motor", "0");
        }

        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("estadoPuerta", "1");
        ref.update(hopperUpdates);

        estadoPuertaMQTT = "1";
    }

    public void verCamara(View view) {
        Intent myIntent = new Intent(view.getContext(), CamarasActivity.class);
        startActivity(myIntent);
    }

    public void cambiarIluminacion(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("buzones").document("iuuL6GzS8k8uz042XkBy");

        image = view.findViewById(R.id.image_iluminacion);
        if (estado_iluminacion){
            estado_iluminacion = false;
            publicarMqtt("led", "0");
            image.setImageResource(R.drawable.sensor_luz);
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("iluminacion", true);
            ref.update(hopperUpdates);
        }else{
            estado_iluminacion = true;
            publicarMqtt("led", "1");
            image.setImageResource(R.drawable.sensor_luz_apagado);
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("iluminacion", false);
            ref.update(hopperUpdates);
        }
    }

    //MQTT
    public static void conectarMqtt() {
        try {
            Log.i(TAG, "Conectando al broker " + broker);
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            /*MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(60);
            connOpts.setWill(topicRoot+"WillTopic","App desconectada".getBytes(),
                    qos, false);
            client.connect(connOpts);*/
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

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("buzones").document("iuuL6GzS8k8uz042XkBy");

        String payload = new String(message.getPayload());
        Log.d(TAG, "Recibiendo: " + topic + "->" + payload);

        System.out.println("Estado al recibir MQTT: " + message);

        estadoPuertaMQTT = message.toString();

        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("estadoPuerta", estadoPuertaMQTT);
        ref.update(hopperUpdates);

        /*switch (estadoPuertaMQTT) {

            case "0": //abierta
                puertadb.setText("Abierta");
                break;
            case "1": //moviendose
                puertadb.setText("Moviendose");
                break;
            case "2": //cerrada
                puertadb.setText("Cerrada");
                break;
        }*/

    }



}