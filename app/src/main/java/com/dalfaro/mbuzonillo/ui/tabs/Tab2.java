package com.dalfaro.mbuzonillo.ui.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dalfaro.mbuzonillo.databinding.Tab2Binding;
import com.dalfaro.mbuzonillo.models.Paquete;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class  Tab2 extends Fragment {

    private PaquetesFirebaseRecyclerAdapter adapter;
    private Tab2Binding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = Tab2Binding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SetupFirebaseRecyclerView();

        return root;
    }

    private void SetupFirebaseRecyclerView() {

        Query query = FirebaseFirestore.getInstance()
                .collection("buzones").document("QSKKczGZg5vyyWem1gre").collection("paquetes")
                .orderBy("fecha")
                .limit(50);
        SnapshotParser<Paquete> snapshotParser = snapshot -> {
            Paquete paquete = snapshot.toObject(Paquete.class);
            paquete.setUid(snapshot.getId());
            return paquete;
        };
        FirestoreRecyclerOptions<Paquete> opciones = new FirestoreRecyclerOptions
                .Builder<Paquete>().setQuery(query, snapshotParser).build();
        adapter = new PaquetesFirebaseRecyclerAdapter(opciones);

        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /*private void EventChangeListener() {
        db.collection("buzones").document("QSKKczGZg5vyyWem1gre").collection("paquetes").orderBy("fecha", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {

                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    paqueteArrayList.add(dc.getDocument().toObject(Paquete.class));
                                    break;
                                case MODIFIED:
                                    //paqueteArrayList.set(dc.getDocument().getId(), dc.getDocument().toObject(Paquete.class));
                                    break;
                                case REMOVED:
                                    paqueteArrayList.remove(dc.getDocument().toObject(Paquete.class));
                                    break;
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }*/


}



