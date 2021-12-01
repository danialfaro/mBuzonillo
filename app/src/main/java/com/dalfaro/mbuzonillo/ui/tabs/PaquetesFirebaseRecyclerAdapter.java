package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.models.Paquete;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.io.Serializable;

public class PaquetesFirebaseRecyclerAdapter extends FirestoreRecyclerAdapter<Paquete, PaqueteHolder> {

    public PaquetesFirebaseRecyclerAdapter(FirestoreRecyclerOptions<Paquete> opciones) {
        super(opciones);
    }

    @Override
    protected void onBindViewHolder(@NonNull PaqueteHolder holder, int position, @NonNull Paquete model) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.getNombre().setText(model.getNombre());
        holder.getFecha().setText(model.getFecha());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), DescripcionPaquete.class);
            intent.putExtra("paquete", model);
            view.getContext().startActivity(intent);
        });
    }

    @NonNull
    @Override
    public PaqueteHolder onCreateViewHolder(ViewGroup group, int i) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(group.getContext())
                .inflate(R.layout.item_paquete, group, false);

        return new PaqueteHolder(view);
    }
}

class PaqueteHolder extends RecyclerView.ViewHolder {

    private final TextView nombre;
    private final TextView fecha;

    public PaqueteHolder(@NonNull View itemView) {
        super(itemView);
        nombre = itemView.findViewById(R.id.nombre_paquete);
        fecha = itemView.findViewById(R.id.fecha_paquete);
    }

    public TextView getNombre() {
        return nombre;
    }

    public TextView getFecha() {
        return fecha;
    }
}