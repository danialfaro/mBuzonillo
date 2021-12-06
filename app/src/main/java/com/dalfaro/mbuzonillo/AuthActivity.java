package com.dalfaro.mbuzonillo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dalfaro.mbuzonillo.models.Buzon;
import com.dalfaro.mbuzonillo.models.Usuario;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;

import java.util.Objects;

enum ProviderType {
    BASIC,
    GOOGLE
}

public class AuthActivity extends AppCompatActivity {

    private final int GOOGLE_SIGN_IN = 100;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setTheme(R.style.Theme_MBuzonillo);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        Objects.requireNonNull(getSupportActionBar()).hide();

        firestore = FirebaseFirestore.getInstance();

        //Setup
        setup();
        session();


    }

    @Override
    protected void onStart() {
        super.onStart();
        this.setVisible(true);
    }

    private void session() {

        SharedPreferences prefs = getSharedPreferences("com.danialfaro.gti.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String provider = prefs.getString("provider", null);

        if (email != null && provider != null) {
            this.setVisible(false);
            showHome(ProviderType.valueOf(provider));
        }
    }

    private void setup() {

        String title = "Autentificacion";

        TextView textEmail = findViewById(R.id.editTextEmail);
        TextView textPassword = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(view -> {

            if (!textEmail.getText().toString().isEmpty() && !textPassword.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showHome(ProviderType.BASIC);
                    } else {
                        showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
            }

        });

        Button signUpButton = findViewById(R.id.buttonRegister);
        signUpButton.setOnClickListener(view -> {

            if (!textEmail.getText().toString().isEmpty() && !textPassword.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        createUserIfNotExist(user, ProviderType.BASIC);
                    } else {
                        showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
            }

        });

        Button googleButton = findViewById(R.id.buttonLoginGoogle);
        googleButton.setOnClickListener(view -> {
            //Configuracion
            GoogleSignInOptions googleConf = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this, googleConf);
            startActivityForResult(googleSignInClient.getSignInIntent(), GOOGLE_SIGN_IN);

        });

        //Background buttons
        googleButton.setBackgroundColor(Color.parseColor("#FFFFFF"));
        signUpButton.setBackgroundColor(Color.parseColor("#547FA1"));
        loginButton.setBackgroundColor(Color.parseColor("#547FA1"));

    }


    private void showHome(ProviderType provider) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("provider", provider);
        startActivity(homeIntent);
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario: \n" + message);
        builder.setPositiveButton("Aceptar", null);
        Dialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if (account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            FirebaseUser user = task1.getResult().getUser();
                            createUserIfNotExist(user, ProviderType.GOOGLE);
                        } else {
                            showAlert(Objects.requireNonNull(task1.getException()).getMessage());
                        }
                    });
                }

            } catch (ApiException e) {
                showAlert(e.getLocalizedMessage());
                e.printStackTrace();
            }

        }
    }

    public void createUserIfNotExist(FirebaseUser user, ProviderType provider) {
        firestore.collection("usuarios").document(user.getUid()).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (!document.exists()) {
                    Buzon buzon = new Buzon("Buzon de " + user.getUid());
                    firestore.collection("buzones").add(buzon).addOnSuccessListener(documentReference -> {

                        buzon.setUid(documentReference.getId());

                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        storage.getReference("Profile Pic/profile_pic.png").getDownloadUrl().addOnSuccessListener(uri -> {

                            String defaultProfilePicUrl = uri.toString();
                            Usuario usuario = new Usuario(user.getUid(), user.getEmail(), defaultProfilePicUrl, new String[]{documentReference.getId()});
                            firestore.collection("usuarios").document(user.getUid()).set(usuario);

                            showHome(provider);

                        }).addOnFailureListener(e -> {
                            showAlert(e.getMessage());
                        });


                    });
                } else {
                    firestore.collection("usuarios").document(user.getUid()).update("inicioSesion", System.currentTimeMillis());
                    showHome(provider);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

}