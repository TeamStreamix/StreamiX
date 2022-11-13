package com.supun.streamix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<VideoCardModel> videoCardModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void setupVideoCardModels(){

        // These are the dummy data used to display
        String[] titles = {"Video 1", "Video 2", "Video 3", "Video 4"};
        String[] descriptions = {
                "This is a description for video 1",
                "This is a description for video 2",
                "This is a description for video 3",
                "This is a description for video 4"
        };

        int[] vectorImages = {
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp};

        for(int i = 0; i < titles.length; i++){
            videoCardModels.add(new VideoCardModel(vectorImages[i], titles[i], descriptions[i]));
        }
    }
}
