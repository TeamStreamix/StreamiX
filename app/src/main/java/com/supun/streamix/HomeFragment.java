package com.supun.streamix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.supun.streamix.ui.videoCard.IRecyclerView;
import com.supun.streamix.ui.videoCard.VC_RecycleViewAdapter;
import com.supun.streamix.ui.videoCard.VideoCardModel;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements IRecyclerView {

    private static final ArrayList<VideoCardModel> videoCardModels = new ArrayList<>();
    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home);
        setupVideoCardModels();
        VC_RecycleViewAdapter adapter = new VC_RecycleViewAdapter(getContext(), videoCardModels, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        final int[] state = new int[1];

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state[0] = newState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0 && (state[0] == 0 || state[0] == 2)){
//                    hideToolbar();
                }
                else if(dy < -10){
//                    showToolbar();
                }

            }
        });

        return view;

    }

    private void setupVideoCardModels(){

        // This is to prevent duplicating items when orientation change
        videoCardModels.clear();

        // These are the dummy data used to display
        String[] titles = {"Video 1", "Video 2", "Video 3", "Video 4", "Video 5", "Video 1", "Video 2", "Video 3", "Video 4", "Video 5"};
        String[] descriptions = {
                "This is a description for video 1",
                "This is a description for video 2",
                "This is a description for video 3",
                "This is a description for video 4",
                "This is a description for video 5",
                "This is a description for video 1",
                "This is a description for video 2",
                "This is a description for video 3",
                "This is a description for video 4",
                "This is a description for video 5"
        };

        int[] vectorImages = {
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp,
                R.drawable.ic_android_black_24dp
        };

        String[] associatedUrls= {
                "https://samplelib.com/lib/preview/mp4/sample-5s.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://samplelib.com/lib/preview/mp4/sample-30s.mp4",
                "https://samplelib.com/lib/preview/mp4/sample-30s.mp4",
                "https://samplelib.com/lib/preview/mp4/sample-5s.mp4",
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4",
                "https://media.geeksforgeeks.org/wp-content/uploads/20201217163353/Screenrecorder-2020-12-17-16-32-03-350.mp4",
                "https://samplelib.com/lib/preview/mp4/sample-30s.mp4",
                "https://samplelib.com/lib/preview/mp4/sample-30s.mp4"
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

    private void hideToolbar(){

        View view = mainActivity.mainToolbarLayout;


        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), -100);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = val;
            view.setLayoutParams(layoutParams);
        });
        anim.setDuration(1000);
        anim.start();



    }

    private void showToolbar(){

        View view = mainActivity.mainToolbarLayout;


        ValueAnimator anim = ValueAnimator.ofInt(0, view.getMeasuredHeight());
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.height = val;
            view.setLayoutParams(layoutParams);
        });
        anim.setDuration(1000);
        anim.start();
    }
}
