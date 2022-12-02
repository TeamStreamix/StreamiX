package com.supun.streamix;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class videoUploadForm extends AppCompatActivity {

    private ExoPlayer player;
    private Button btnUpload;
    private Button btnCancel;
    private MaterialAutoCompleteTextView formVideoTitle;
    private MaterialAutoCompleteTextView formVideoDescription;
    private String videoUri;

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
        videoUri = Uri.parse(getIntent().getStringExtra("VIDEO_URL")).toString();

        String videoRecordTime = getIntent().getStringExtra("VIDEO_RECORD_TIME");

        StyledPlayerView playerView = findViewById(R.id.video_player_view_upload_form);
        playerView.setControllerShowTimeoutMs(3000);

        player = new ExoPlayer.Builder(this).build();

        playerView.setPlayer(player);

        MediaItem mediaItem = new MediaItem.Builder().setUri(videoUri).build();

        player.setMediaItem(mediaItem);

        player.prepare();

        player.setPlayWhenReady(true);

        btnUpload.setOnClickListener(
                view -> {
                    verifyStoragePermissions(this);
                    Log.i("ASDASDASD","Headers are written 1");
                   UploadVideoFile uploadVideoFile = new UploadVideoFile();
                   uploadVideoFile.execute();

                    Intent intent = new Intent(this, MainActivity.class);

                    startActivity(intent);
                }
        );


    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private class UploadVideoFile extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            videoUri = getRealPathFromURI(Uri.parse(videoUri));

            Log.i("MY_VIDEO_PATH", videoUri);

            File video_source = new File(videoUri);
            String iFileName = videoUri;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String Tag="fSnd";

            try {

                FileInputStream fileInputStream = new FileInputStream(video_source);

                // upload url
                URL url = new URL(BuildConfig.SERVER_URL + "upload");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE",
                        "multipart/form-data");
                conn.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("fileToUpload", videoUri);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                Log.i(Tag,"Headers are written");

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"title\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(formVideoTitle.getText().toString());
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"description\""+ lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(formVideoDescription.getText().toString());
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName +"\"" + lineEnd);
                dos.writeBytes(lineEnd);

                Log.i(Tag,"Headers are written");

                // create a buffer of maximum size
                int bytesAvailable = fileInputStream.available();

                int maxBufferSize = 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[ ] buffer = new byte[bufferSize];

                // read file and write it into form...
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0)
                {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable,maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0,bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // close streams
                fileInputStream.close();

                dos.flush();

//                Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));

//                InputStream is = conn.getInputStream();
//
//                // retrieve the response from server
//                int ch;
//
//                StringBuffer b =new StringBuffer();
//                while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
//                String s=b.toString();
//                Log.i("RESPONSE",s);
//                dos.close();


            } catch(MalformedURLException e){
                Log.e(Tag, "URL error: " + e.getMessage(), e);
            } catch(IOException ioe){
                Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
            }

            return null;
        }
    }
}
