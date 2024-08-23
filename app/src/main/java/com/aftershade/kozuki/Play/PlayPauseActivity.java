package com.aftershade.kozuki.Play;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aftershade.kozuki.HelperClasses.MediaPlayerService;
import com.aftershade.kozuki.HelperClasses.MusicFiles;
import com.aftershade.kozuki.HelperClasses.StorageUtil;
import com.aftershade.kozuki.MainScreen.MainActivity;
import com.aftershade.kozuki.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static com.aftershade.kozuki.HelperClasses.Adapters.RecentSongs.recentFiles;
import static com.aftershade.kozuki.HelperClasses.MediaPlayerService.audioIndex;
import static com.aftershade.kozuki.HelperClasses.MediaPlayerService.isMediaPlaying;

public class PlayPauseActivity extends AppCompatActivity {

    TextView songName, artistName;
    ImageView songArt, previousBtn, nextBtn, playpauseBtn;
    int position;
    static ArrayList<MusicFiles> listSongs;
    static MediaPlayer mediaPlayer;
    Uri uri;
    private MediaPlayerService playerService;
    boolean serviceBound = false;
    public static final String Broadcast_PLAY_NEW_AUDIO = "com.aftershade.kozuki.Play";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pause);

        position = getIntent().getIntExtra("position", -1);
        listSongs = recentFiles;

        //uri = Uri.parse(listSongs.get(position).getPath());

        songName = findViewById(R.id.song_name_playpause);
        artistName = findViewById(R.id.artist_name_play_pause);
        songArt = findViewById(R.id.song_art_play_pause);
        previousBtn = findViewById(R.id.previous_song_btn);
        nextBtn = findViewById(R.id.next_song_btn);
        playpauseBtn = findViewById(R.id.play_pause_btn);


        songName.setText(listSongs.get(position).getTitle());
        artistName.setText(listSongs.get(position).getArtist());
        setArt();


        if (isMediaPlaying) {


            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);

            /*playerService.stopMedia();
            playerService.resetMediaPlayer();
            playerService.initMediaPlayer();*/

        } else {
            playAudio(position);
        }

        previousBtn.setOnClickListener(v -> {
            playerService.skipToPrevious();
            updateActivity();
        });

        playpauseBtn.setOnClickListener(v -> {
            if (isMediaPlaying) {
                playerService.pauseMedia();
            } else {
                playerService.resumeMedia();
            }
        });

        nextBtn.setOnClickListener(v -> {
            playerService.skipToNext();
            updateActivity();
        });

    }

    private void updateActivity() {
        songName.setText(listSongs.get(audioIndex).getTitle());
        artistName.setText(listSongs.get(audioIndex).getArtist());
        setArt();
    }

    private void setArt() {

        byte[] image;

        if (audioIndex == -1){
             image = getAlbumArt(listSongs.get(position).getPath());
        } else {
            image = getAlbumArt(listSongs.get(audioIndex).getPath());
        }

        if (image != null) {
            Glide.with(getApplicationContext()).asBitmap().load(image).into(songArt);
        } else {
            Glide.with(getApplicationContext()).load(R.drawable.lens_flare).into(songArt);
        }

    }

    public byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();

        return art;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("ServiceState", serviceBound);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        serviceBound = savedInstanceState.getBoolean("ServiceState");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (serviceBound) {
            unbindService(serviceConnection);
            playerService.stopSelf();
        }
    }

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) iBinder;
            playerService = binder.getService();
            serviceBound = true;

            Toast.makeText(PlayPauseActivity.this, "Service Bound", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            serviceBound = false;
            Toast.makeText(getApplicationContext(), "Unbound", Toast.LENGTH_SHORT).show();
        }
    };

    private void playAudio(int audioIndex) {
        StorageUtil storageUtil = new StorageUtil(getApplicationContext());
        if (!serviceBound) {
            storageUtil.storeAudio(listSongs);
            storageUtil.storeAudioIndex(audioIndex);

            Intent playerIntent = new Intent(this, MediaPlayerService.class);
            startService(playerIntent);
            bindService(playerIntent, serviceConnection, BIND_AUTO_CREATE);
        } else {
            storageUtil.storeAudioIndex(audioIndex);

            Intent broadcastIntent = new Intent(Broadcast_PLAY_NEW_AUDIO);
            sendBroadcast(broadcastIntent);
        }
    }

}