package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.models.Paquete;

public class DescripcionPaquete extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.descripcion_paquete);

        Paquete paquete = (Paquete) getIntent().getSerializableExtra("paquete");
        System.out.println(paquete);

        Button botonCamara = findViewById(R.id.verGrabacion);
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verCamara(view);
            }
        });

    }


    public void verCamara(View view) {
        Intent myIntent = new Intent(view.getContext(), CamarasActivity.class);
        startActivity(myIntent);
        //Toast.makeText(view.getContext(), "Abriendo Cámaras", Toast.LENGTH_SHORT).show();
    }

}