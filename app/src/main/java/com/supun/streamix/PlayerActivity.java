package com.supun.streamix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

public class PlayerActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // This is the URL for the video
        String videoURL = getIntent().getStringExtra("VIDEO_URL");
        String videoName = getIntent().getStringExtra("VIDEO_TITLE");

        StyledPlayerView playerView = findViewById(R.id.video_player_view);
        playerView.setControllerShowTimeoutMs(2000);

        ExoPlayer player = new ExoPlayer.Builder(this).build();

        playerView.setPlayer(player);

        MediaItem mediaItem = MediaItem.fromUri(videoURL);

        player.addMediaItem(mediaItem);

        player.prepare();

        player.setPlayWhenReady(true);


    }
}