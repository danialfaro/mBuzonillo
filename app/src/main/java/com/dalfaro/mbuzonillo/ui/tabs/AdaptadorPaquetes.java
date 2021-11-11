package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.models.Paquete;

import java.lang.reflect.Array;
import java.util.ArrayList;



public class AdaptadorPaquetes extends RecyclerView.Adapter<AdaptadorPaquetes.ViewHolder> implements View.OnClickListener{
    Context context;
    ArrayList<Paquete> paquetesArrayList;
    private View.OnClickListener listener;

    //Constructor
    public AdaptadorPaquetes(Context context, ArrayList<Paquete> paquetesArrayList) {
        this.context = context;
        this.paquetesArrayList = paquetesArrayList;
    }

    //Métodos
    @NonNull
    @Override
    public AdaptadorPaquetes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_paquete, parent, false);
        v.setOnClickListener(this);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPaquetes.ViewHolder holder, int position) {
        Paquete paquete = paquetesArrayList.get(position);

        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition));
        holder.nombre.setText(paquete.getNombre());
        holder.fecha.setText(paquete.getFecha());
        //holder.proveedorDescripcion.setText(paquete.getProveedor());
    }

    @Override
    public int getItemCount() {
        return paquetesArrayList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre, fecha;
        LinearLayout cv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombre_paquete);
            fecha = itemView.findViewById(R.id.fecha_paquete);
            cv = itemView.findViewById(R.id.cv);

        }
    }
}
