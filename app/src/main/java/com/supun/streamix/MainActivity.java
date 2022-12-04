package com.supun.streamix;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.supun.streamix.ui.main.SectionsPagerAdapter;
import com.supun.streamix.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    RelativeLayout mainToolbarLayout;

    private ActionMenuItemView btnToggleDark;
    private Menu menu;

    private int recordCode = 100;
    private Uri videoUri;



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("started_main", "Application re rendered"); // Every thing reruns when the orientation changes
        Log.i("BASE_URL", BuildConfig.SERVER_URL);

        super.onCreate(savedInstanceState);





        binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        mainToolbarLayout = findViewById(R.id.main_toolbar_layout);


        FloatingActionButton fab = binding.fab;

        fab.setOnClickListener(view -> {
            try{
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);

                startActivityForResult(intent, recordCode);


            } catch (Exception e){
                e.printStackTrace();
            }
        });




        // This is the code to change the theme ------------------------------------------

        btnToggleDark = findViewById(R.id.app_bar_dark_mode);

        // Saving state of our app
        // using SharedPreferences
        SharedPreferences sharedPreferences
                = getSharedPreferences(
                "sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor
                = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        // When user reopens the app
        // after applying dark/light mode
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnToggleDark.setIcon(ContextCompat.getDrawable(this, R.drawable.app_bar_light_mode));

        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            btnToggleDark.setIcon(ContextCompat.getDrawable(this, R.drawable.app_bar_dark_mode));

        }

        btnToggleDark.setOnClickListener(
                view -> {
                    // When user taps the enable/disable
                    // dark mode button
                    if (isDarkModeOn) {

                        // if dark mode is on it
                        // will turn it off
                        AppCompatDelegate
                                .setDefaultNightMode(
                                        AppCompatDelegate
                                                .MODE_NIGHT_NO);
                        // it will set isDarkModeOn
                        // boolean to false
                        editor.putBoolean(
                                "isDarkModeOn", false);
                        editor.apply();


                    }
                    else {

                        // if dark mode is off
                        // it will turn it on
                        AppCompatDelegate
                                .setDefaultNightMode(
                                        AppCompatDelegate
                                                .MODE_NIGHT_YES);

                        // it will set isDarkModeOn
                        // boolean to true
                        editor.putBoolean(
                                "isDarkModeOn", true);
                        editor.apply();

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == recordCode){
            if(resultCode == RESULT_OK){
                assert data != null;
                videoUri = data.getData();

                Log.i("MY_VIDEO_URI", videoUri.toString());

                Intent intent = new Intent(this, videoUploadForm.class);


                intent.putExtra("VIDEO_URL", videoUri.toString());

                startActivity(intent);

                finish();

                Log.i("RESULT_OK", "onActivityResult: Video captured");
            } else if(resultCode == RESULT_CANCELED){
                Log.i("CAMERA_CLOSE", "onActivityResult: Camera Closed");
            } else{
                Log.i("CAMERA_FAILED", "onActivityResult: Camera Failed");
            }
        }
    }
}