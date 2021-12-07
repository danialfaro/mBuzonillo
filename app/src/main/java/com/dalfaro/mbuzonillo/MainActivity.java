package com.dalfaro.mbuzonillo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.dalfaro.mbuzonillo.databinding.ActivityMainBinding;
import com.dalfaro.mbuzonillo.models.Buzon;
import com.dalfaro.mbuzonillo.models.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        //Navigation Drawer (menu lateral)
        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_buzon, R.id.nav_ajustes)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Usuario Información
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("usuarios").document(usuario.getUid()).get().addOnSuccessListener(documentSnapshot -> {

            Usuario userData = documentSnapshot.toObject(Usuario.class);

            if(userData != null) {
                View header = navigationView.getHeaderView(0);
                ImageView imageMenu = header.findViewById(R.id.imageViewDrawableMenu);

                Glide.with(this)
                        .load(userData.getProfilePicUrl())
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageMenu);

                TextView nameMenu = header.findViewById(R.id.textViewNameMenu);
                nameMenu.setText(userData.getNombre());
                TextView emailMenu = header.findViewById(R.id.textViewNombrePaquete);
                emailMenu.setText(userData.getCorreo());
            }


        });



        //Setup
        Bundle bundle = getIntent().getExtras();
        //String email = bundle.getString("email");
        ProviderType provider = (ProviderType) bundle.get("provider");
        //setup(email, provider);

        //Guardar datos de la sesion
        SharedPreferences prefs = getSharedPreferences("com.danialfaro.gti.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", usuario.getEmail());
        editor.putString("provider", provider.toString());
        editor.apply();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
        }
    }

}