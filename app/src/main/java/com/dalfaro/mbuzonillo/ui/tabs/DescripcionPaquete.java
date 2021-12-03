package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.databinding.DescripcionPaqueteBinding;
import com.dalfaro.mbuzonillo.models.Paquete;

public class DescripcionPaquete extends AppCompatActivity {

    DescripcionPaqueteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DescripcionPaqueteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //setContentView(R.layout.descripcion_paquete);

        Paquete paquete = (Paquete) getIntent().getSerializableExtra("paquete");
        System.out.println(paquete);

        binding.textViewNombrePaquete.setText(paquete.getNombre());

        Button botonCamara = findViewById(R.id.verGrabacion);
        botonCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verCamara(view);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }


    public void verCamara(View view) {
        Intent myIntent = new Intent(view.getContext(), CamarasActivity.class);
        startActivity(myIntent);
        //Toast.makeText(view.getContext(), "Abriendo Cámaras", Toast.LENGTH_SHORT).show();
    }

}