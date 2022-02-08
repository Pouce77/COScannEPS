package fr.kunze.coscanneps;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class Aide extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> listVideo;
    int un=R.raw.enregistrer_une_nouvelle_course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aide);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       recyclerView=findViewById(R.id.recyclerview);
       layoutManager=new LinearLayoutManager(Aide.this,LinearLayoutManager.VERTICAL,false);

       listVideo=new ArrayList<>();

       listVideo.add("enregistrer_une_nouvelle_course.mp4");

        RecyclerViewAdapter recyclerViewAdapter=new RecyclerViewAdapter(listVideo,R.layout.cardview,Aide.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                final VideoView videoView=(VideoView)view.findViewById(R.id.videoViewCard);
                videoView.setBackgroundResource(0);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        videoView.start();
                        videoView.seekTo(200);
                        videoView.requestFocus();

                    }


                });

                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {

                        videoView.stopPlayback();
                        videoView.setBackgroundResource(android.R.drawable.ic_media_play);
                    }
                });

            }

            @Override
            public void onLongItemClick(View view, int position) {


            }
        }));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listbalise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        if (id == R.id.home) {

            Intent i = new Intent(Aide.this, MainActivity.class);
            startActivity(i);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void playVideo(final VideoView videoView) throws IOException {

        final MediaController mc = new MediaController(Aide.this);

        videoView.setMediaController(mc);

        final Uri uri= Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.enregistrer_une_nouvelle_course);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         * add media controller
                         */
                        videoView.setMediaController(mc);
                        /*
                         * and set its position on screen
                         */
                        mc.setAnchorView(videoView);
                    }
                });
                videoView.seekTo(200);
                videoView.requestFocus();

            }
        });
    }
}