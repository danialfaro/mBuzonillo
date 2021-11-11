package com.dalfaro.mbuzonillo;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

enum ProviderType {
    BASIC,
    GOOGLE
}

public class AuthActivity extends AppCompatActivity {

    private final int GOOGLE_SIGN_IN = 100;

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

        if(email != null && provider != null) {
            this.setVisible(false);
            showHome(email, ProviderType.valueOf(provider));
        }


    }

    private void setup() {

        String title = "Autentificacion";

        TextView textEmail = findViewById(R.id.editTextEmail);
        TextView textPassword = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(view -> {

            if(!textEmail.getText().toString().isEmpty() && !textPassword.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString()).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String email = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getEmail(); //No deberia ser null
                        showHome(email, ProviderType.BASIC);
                    } else {
                        showAlert(Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
            }

        });

        Button signUpButton = findViewById(R.id.buttonRegister);
        signUpButton.setOnClickListener(view -> {

            if(!textEmail.getText().toString().isEmpty() && !textPassword.getText().toString().isEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(textEmail.getText().toString(), textPassword.getText().toString()).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        String email = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getEmail(); //No deberia ser null
                        showHome(email, ProviderType.BASIC);
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


    private void showHome(String email, ProviderType provider) {
        Intent homeIntent = new Intent(this, MainActivity.class);
        homeIntent.putExtra("email", email);
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

        if(requestCode == GOOGLE_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                if(account != null) {
                    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()) {
                            String email = Objects.requireNonNull(account.getEmail()); //No deberia ser null
                            showHome(email, ProviderType.GOOGLE);
                        } else {
                            showAlert(Objects.requireNonNull(task1.getException()).getMessage());
                        }
                    });
                }

            } catch (ApiException e) {
                showAlert(e.getMessage());
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }
}