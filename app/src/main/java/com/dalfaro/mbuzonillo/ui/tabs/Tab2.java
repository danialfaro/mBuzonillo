package com.dalfaro.mbuzonillo.ui.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dalfaro.mbuzonillo.MainActivity;
import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.models.Paquete;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class Tab2 extends Fragment {

    RecyclerView recyclerView;
    ArrayList<Paquete> paqueteArrayList;
    AdaptadorPaquetes adaptador;
    FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        paqueteArrayList = new ArrayList<Paquete>();
        adaptador = new AdaptadorPaquetes(getActivity(), paqueteArrayList);

        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Por lo menos te saca el nombre: " + paqueteArrayList.get(recyclerView.getChildAdapterPosition(v)).getNombre(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), DescripcionPaquete.class);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adaptador);

        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
        db.collection("buzones").document("QSKKczGZg5vyyWem1gre").collection("paquetes").orderBy("fecha", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                paqueteArrayList.add(dc.getDocument().toObject(Paquete.class));
                            }
                            adaptador.notifyDataSetChanged();
                        }
                    }
                });
    }

}



