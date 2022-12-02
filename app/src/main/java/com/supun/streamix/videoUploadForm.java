package com.supun.streamix;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

public class videoUploadForm extends AppCompatActivity {

    private ExoPlayer player;
    private Button btnUpload;
    private Button btnCancel;
    private MaterialAutoCompleteTextView formVideoTitle;
    private MaterialAutoCompleteTextView formVideoDescription;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_upload_form);

        // Form id init
        btnUpload = findViewById(R.id.btnUploadVideo);
        btnCancel = findViewById(R.id.btnCancelVideo);
        formVideoTitle = findViewById(R.id.formVideoTitle);
        formVideoDescription = findViewById(R.id.formVideoDescription);

        // Get URL for the video
        String videoURL = getIntent().getStringExtra("VIDEO_URL");
        String videoRecordTime = getIntent().getStringExtra("VIDEO_RECORD_TIME");

        StyledPlayerView playerView = findViewById(R.id.video_player_view_upload_form);
        playerView.setControllerShowTimeoutMs(3000);

        player = new ExoPlayer.Builder(this).build();

        playerView.setPlayer(player);

        MediaItem mediaItem = new MediaItem.Builder().setUri(videoURL).build();

        player.setMediaItem(mediaItem);

        player.prepare();

        player.setPlayWhenReady(true);


    }
}
