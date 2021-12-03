package com.dalfaro.mbuzonillo.ui.tabs;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;

import com.dalfaro.mbuzonillo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CamarasActivity extends Activity {

    VideoView videoView;
    VideoView videoView2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.camaras);

        videoView=(VideoView) findViewById(R.id.videoView2);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.cam1);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView2=(VideoView) findViewById(R.id.videoView3);
        Uri uri2 = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.cam2);
        videoView2.setVideoURI(uri2);
        videoView2.start();

        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView2.setMediaController(new MediaController(this));
        videoView2.requestFocus();
    }
}
