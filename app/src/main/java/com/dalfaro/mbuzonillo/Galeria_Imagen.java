package com.dalfaro.mbuzonillo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Galeria_Imagen extends AppCompatActivity {
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
}
