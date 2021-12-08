package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.ui.ajustes.Ajustes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tab1 extends Fragment{

    private TextView puertadb;
    private TextView pesodb;
    private TextView iluminaciondb;

    boolean estado_puerta;
    boolean estado_iluminacion;
    ImageView image;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);

        puertadb = view.findViewById(R.id.puerta_abierta_cerrada);
        pesodb = view.findViewById(R.id.peso_kg);
        iluminaciondb = view.findViewById(R.id.iluminacion_encendido_apagado);
        obtenerdatos();

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
                                boolean dato_puerta = (boolean) value.getData().get("puerta");;
                                boolean dato_iluminacion = (boolean) value.getData().get("iluminacion");;

                                pesodb.setText(dato_peso);
                                if (dato_puerta == true){
                                    puertadb.setText("Abierta");
                                    estado_puerta = true;
                                    puertadb.setTextColor(0xFF335571);
                                    getActivity().startForegroundService(new Intent(getContext(), ServicioPuertaAbierta.class));
                                }else{
                                    puertadb.setText("Cerrada");
                                    estado_puerta = false;
                                    puertadb.setTextColor(0xFFD74646);
                                    getActivity().stopService(new Intent(getContext(), ServicioPuertaAbierta.class));

                                }

                                if (dato_iluminacion == true){
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
        image = view.findViewById(R.id.image_puerta);
        if (estado_puerta == true) {
            estado_puerta = false;
            image.setImageResource(R.drawable.puerta_cerrada);
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("puerta", false);
            ref.update(hopperUpdates);
            getActivity().stopService(new Intent(getContext(), ServicioPuertaAbierta.class));
        } else {

            estado_puerta = true;
            image.setImageResource(R.drawable.puerta_abierta);
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("puerta", true);
            ref.update(hopperUpdates);
            getActivity().startForegroundService(new Intent(getContext(), ServicioPuertaAbierta.class));

        }
    }

    public void verCamara(View view) {
        Intent myIntent = new Intent(view.getContext(), CamarasActivity.class);
        startActivity(myIntent);
    }

    public void cambiarIluminacion(View view){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("buzones").document("iuuL6GzS8k8uz042XkBy");
        image = view.findViewById(R.id.image_iluminacion);
        if (estado_iluminacion == true){
            estado_iluminacion = false;
            image.setImageResource(R.drawable.sensor_luz);
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("iluminacion", true);
            ref.update(hopperUpdates);
        }else{
            estado_iluminacion = true;
            image.setImageResource(R.drawable.sensor_luz_apagado);
            Map<String, Object> hopperUpdates = new HashMap<>();
            hopperUpdates.put("iluminacion", false);
            ref.update(hopperUpdates);
        }
    }


}