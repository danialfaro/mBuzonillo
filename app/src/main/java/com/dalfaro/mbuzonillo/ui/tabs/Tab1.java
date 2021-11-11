package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.ui.ajustes.Ajustes;


public class Tab1 extends Fragment{
    //FirebaseFirestore db;
    //ArrayList<InfoBuzon> infoBuzonArrayList;
    //TextView puerta;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FirebaseFirestore db = FirebaseFirestore.getInstance();
        //infoBuzonArrayList = new ArrayList<InfoBuzon>();
    }
    /**
     private void EventChangeListener() {
     db.collection("buzones")
     .addSnapshotListener(new EventListener<QuerySnapshot>() {
    @Override
    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

    if(error != null){
    Log.e("Firestore error", error.getMessage());
    return;
    }

    for(DocumentChange dc: value.getDocumentChanges()){
    if(dc.getType() == DocumentChange.Type.ADDED){
    infoBuzonArrayList.add(dc.getDocument().toObject(InfoBuzon.class));
    }

    }
    }
    });
     }
     **/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);

        ConstraintLayout clp = view.findViewById(R.id.constraintLayoutPeso);
        clp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarPeso(view);
            }
        });

        ConstraintLayout clp2 = view.findViewById(R.id.constraintLayoutPuerta);
        clp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarPuerta(view);
            }
        });

        ImageView clp3 = view.findViewById(R.id.image_cambiar_camara);
        clp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verCamara(view);
            }
        });

        ConstraintLayout clp4 = view.findViewById(R.id.constraintLayoutIluminacion);
        clp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarIluminacion(view);
            }
        });
        return view;
    }

    boolean estado;
    ImageView image;
    public void cambiarPuerta(View view) {

        TextView text = view.findViewById(R.id.puerta_abierta_cerrada);
        image = view.findViewById(R.id.image_puerta);
        if (estado) {
            text.setText("Cerrado");
            estado = false;
            text.setTextColor(0xFFD74646);
            image.setImageResource(R.drawable.puerta_cerrada);
        } else {
            text.setText("Abierta");
            estado = true;
            text.setTextColor(0xFF335571);
            image.setImageResource(R.drawable.puerta_abierta);
        }

    }

    public void cambiarPeso(View view){
        TextView text = view.findViewById(R.id.peso_kg);
        text.setText("10 KG");
    }

    public void verCamara(View view) {
        Intent myIntent = new Intent(view.getContext(), CamarasActivity.class);
        startActivity(myIntent);
        //Toast.makeText(view.getContext(), "Abriendo Cámaras", Toast.LENGTH_SHORT).show();
    }

    boolean estado2;
    public void cambiarIluminacion(View view){
        image = view.findViewById(R.id.image_iluminacion);
        TextView text = view.findViewById(R.id.iluminacion_encendido_apagado);
        if (estado2){
            text.setText("Encendido");
            estado2 = false;
            image.setImageResource(R.drawable.sensor_luz);
        }else{
            text.setText("Apagado");
            estado2 = true;
            image.setImageResource(R.drawable.sensor_luz_apagado);
        }
    }


}