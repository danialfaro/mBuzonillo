package com.dalfaro.mbuzonillo.ui.ajustes;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.dalfaro.mbuzonillo.AuthActivity;
import com.dalfaro.mbuzonillo.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Ajustes extends Fragment {

    String selectedidioma = "Español";
    String selectedtema = "Claro";
    int REQUEST_CODE = 200;
    int REQUEST_CODE2 = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ajustes, container, false);

        //Usuario Información
        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String nombre = usuario.getDisplayName();
        String correo = usuario.getEmail();
        String telefono = usuario.getPhoneNumber();
        Uri urlFoto = usuario.getPhotoUrl();
        String uid = usuario.getUid();
        String proveedores = usuario.getProviderData().toString();

        TextView textoNombre = (TextView) view.findViewById(R.id.nombreUsuario);
        textoNombre.setText(nombre.split(" ")[0]);

        TextView textoCorreo = (TextView) view.findViewById(R.id.correoUsuario);
        textoCorreo.setText(correo.split(" ")[0]);

        //Cerrar Sesión
        Button cerrarSesion = (Button) view.findViewById(R.id.btn_cerrar_sesion2);
        cerrarSesion.setOnClickListener(view2 -> AuthUI.getInstance().signOut(getContext())
                .addOnCompleteListener(task -> {

                    //Borrar datos login
                    SharedPreferences prefs = getActivity().getSharedPreferences("com.danialfaro.gti.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    Intent i = new Intent(cerrarSesion.getContext(), AuthActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                            | Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    getActivity().finish();
                }));

        cerrarSesion.setBackgroundColor(Color.parseColor("#547FA1"));

        //Click

        TextView clidioma = (TextView) view.findViewById(R.id.idioma);
        clidioma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarAvisoIdioma(clidioma);
            }
        });

        TextView cltema = (TextView) view.findViewById(R.id.apariencia);
        cltema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarAvisoTema(cltema);
            }
        });

        TextView clllamar = (TextView) view.findViewById(R.id.flechaLlamar);
        clllamar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                lanzarLlamar(clllamar);
            }
        });

        TextView clcorreo = (TextView) view.findViewById(R.id.flechaCorreo);
        clcorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarCorreo(clcorreo);
            }
        });

        TextView clacercade = (TextView) view.findViewById(R.id.flechaAcercade);
        clacercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lanzarAcercaDe(clacercade);
            }
        });

        TextView clubicacion = (TextView) view.findViewById(R.id.flechaUbicacion);
        clubicacion.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                lanzarUbicacion(clubicacion);
            }
        });


        ImageView image = view.findViewById(R.id.imageView2);
        image.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.anim2));


        return view;
    }

    public void lanzarAvisoIdioma(View view){
        TextView text = view.findViewById(R.id.idioma);
        //AlertDialog List
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.idioma));
        String[] idiomasEleccion = getResources().getStringArray(R.array.idiomas_select);
        int checkedItem = 0;

        builder.setSingleChoiceItems(idiomasEleccion, checkedItem, new DialogInterface.OnClickListener() {
            @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                selectedidioma = idiomasEleccion[which];
            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                text.setText(selectedidioma);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void lanzarAvisoTema(View view){
        TextView text2 = view.findViewById(R.id.apariencia);
        //AlertDialog List
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getResources().getString(R.string.tema));

        String[] temaEleccion = getResources().getStringArray(R.array.tema_select);

        int checkedItem = 0;

        builder.setSingleChoiceItems(temaEleccion, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                selectedtema = temaEleccion[which];

            }
        });
        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                text2.setText(selectedtema);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void lanzarLlamar(View view){
        int permiso_llamada = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE);
        if (permiso_llamada == PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "643364675"));
            startActivity(i);
        }else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},REQUEST_CODE);
        }

    }

    public void lanzarCorreo(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Consultar con BUZöNillo");
        intent.putExtra(Intent.EXTRA_TEXT, "Dudas sobre el buzón.");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"jorgelarrosaquesada@gmail.com"});
        startActivity(intent);
    }

    public void lanzarAcercaDe(View view){
        Intent i = new Intent(getContext(), AcercaDeActivity.class);
        startActivity(i);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void lanzarUbicacion(View view){
        int permiso = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if(permiso==PackageManager.PERMISSION_GRANTED){
            Intent i = new Intent(getContext(), MapaActivity.class);
            startActivity(i);
        }else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE2);
        }
    }


}
