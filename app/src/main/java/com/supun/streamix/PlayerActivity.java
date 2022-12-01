package com.supun.streamix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.util.MimeTypes;

public class PlayerActivity extends AppCompatActivity {


    ExoPlayer player;
    ImageButton btnVideoQuality;
    TextView videoTitle;

    Handler handler;
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_player);

        // This is the URL for the video
        String videoURL = getIntent().getStringExtra("VIDEO_URL");
        String videoName = getIntent().getStringExtra("VIDEO_TITLE");

        StyledPlayerView playerView = findViewById(R.id.video_player_view);
        playerView.setControllerShowTimeoutMs(3000);

        btnVideoQuality  =  findViewById(R.id.video_quality);
        videoTitle = findViewById(R.id.video_title);

        videoTitle.setText(videoName);



        TrackSelector trackSelector = new DefaultTrackSelector();

        player = new ExoPlayer.Builder(this)
                .setTrackSelector(trackSelector)
                .build();

        playerView.setPlayer(player);

        // TODO: TAG -> playlist manifest type .MPD or .M3U8
        MediaItem mediaItem = new MediaItem.Builder()
                .setUri(videoURL)
                .setMimeType(MimeTypes.APPLICATION_MPD).build();

        player.setMediaItem(mediaItem);

        player.prepare();

        player.setPlayWhenReady(true);

        handler = new Handler();
        runnable = () -> {
            btnVideoQuality.setVisibility(View.GONE);
            videoTitle.setVisibility(View.GONE);
        };

        startHandler();


    }

    private void startHandler(){
        handler.postDelayed(runnable, 3000);
    }

    private void stopHandler(){
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        videoTitle.setVisibility(View.VISIBLE);
        btnVideoQuality.setVisibility(View.VISIBLE);
        stopHandler();
        startHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        player.stop();
    }
}