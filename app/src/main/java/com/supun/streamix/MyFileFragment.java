package com.supun.streamix;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.supun.streamix.ui.videoCard.IRecyclerView;
import com.supun.streamix.ui.videoCard.VC_RecycleViewAdapter;
import com.supun.streamix.ui.videoCard.VideoCardModel;

import java.util.ArrayList;


public class MyFileFragment extends Fragment implements IRecyclerView {

    private static final ArrayList<VideoCardModel> videoCardModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_files, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_my_files);

        setupVideoCardModels();
        VC_RecycleViewAdapter adapter = new VC_RecycleViewAdapter(getContext(), videoCardModels, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public static void setupVideoCardModels(){
        /*
        This method can be used to setup the video car models for the fragment
         */

        // This is to prevent duplicating items when orientation change
        videoCardModels.clear();

        // These are the dummy data used to display
        String[] titles = {"Video 5", "Video 6", "Video 7", "Video 8", "Video 9", "Video 10", "Video 15", "Video 16"};
        String[] descriptions = {
                "This is a description for video 5",
                "This is a description for video 6",
                "This is a description for video 7",
                "This is a description for video 8",
                "This is a description for video 9",
                "This is a description for video 10",
                "This is a description for video 15",
                "This is a description for video 16"
        };

        // These vectors should changed to images
        int[] vectorImages = {
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp};

        String[] associatedUrls = {
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4"
        };

        for(int i = 0; i < titles.length; i++){
            videoCardModels.add(new VideoCardModel(vectorImages[i], titles[i], descriptions[i], associatedUrls[i]));
        }
    }

    @Override
    public void onCardClick(int position) {

        Intent intent = new Intent(getActivity(), PlayerActivity.class);


        intent.putExtra("VIDEO_URL", videoCardModels.get(position).getVideoURL());
        intent.putExtra("VIDEO_TITLE", videoCardModels.get(position).getTitle());

        startActivity(intent);

    }
}
