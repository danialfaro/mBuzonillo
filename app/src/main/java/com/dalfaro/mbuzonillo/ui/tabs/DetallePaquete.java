package com.dalfaro.mbuzonillo.ui.tabs;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dalfaro.mbuzonillo.R;
import com.dalfaro.mbuzonillo.databinding.DetallePaqueteBinding;
import com.dalfaro.mbuzonillo.models.Paquete;

public class DetallePaquete extends AppCompatActivity {

    DetallePaqueteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DetallePaqueteBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Paquete paquete = (Paquete) getIntent().getSerializableExtra("paquete");
        System.out.println(paquete);

        CharSequence prettyTime = DateUtils.getRelativeDateTimeString(
                this, paquete.getFecha(), DateUtils.SECOND_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS, 0);

        actionBarSetup(paquete.getNombre(), prettyTime.toString());

        Glide.with(this)
                .load(paquete.getImagenUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.imageView);

        binding.textViewProveedor.setText(paquete.getProveedor());
        binding.textViewPeso.setText(paquete.getPeso());

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup(String title, String subtitle) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar ab = getSupportActionBar();
            ab.setTitle(title);
            ab.setSubtitle(subtitle);
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