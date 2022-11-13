package com.supun.streamix;

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

public class HomeFragment extends Fragment implements IRecyclerView {

    private static final ArrayList<VideoCardModel> videoCardModels = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home);
        setupVideoCardModels();
        VC_RecycleViewAdapter adapter = new VC_RecycleViewAdapter(getContext(), videoCardModels, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;

    }

    private void setupVideoCardModels(){

        // This is to prevent duplicating items when orientation change
        videoCardModels.clear();

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

    @Override
    public void onCardClick(int position) {
        Log.i("CLICKED", videoCardModels.get(position).getTitle() + "Card clicked");
    }
}
