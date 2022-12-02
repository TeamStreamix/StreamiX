package com.supun.streamix;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

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
        View viewToolbar = mainActivity.mainToolbarLayout;
        int defaultHeight = 394;
        viewToolbar.getLayoutParams().height = defaultHeight;
        viewToolbar.requestLayout();



        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state[0] = newState;
                Log.i("SCROLL STATE", "" + newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Log.i("SCROLLED_DY", "" + dy + ":::::" + state[0] + ":::::::::" + viewToolbar.getLayoutParams().height);

                if(dy > 0 && (state[0] == 1 || state[0] == 0)){

                    slideView( viewToolbar, viewToolbar.getLayoutParams().height, 0);

                }
                else if(dy < -10){
                    slideView( viewToolbar, viewToolbar.getLayoutParams().height, defaultHeight);

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
                R.drawable.thumbnail_1,
                R.drawable.thumbnail_2,
                R.drawable.thumbnail_3,
                R.drawable.no_thumbnail_available,
                R.drawable.no_thumbnail_available,
                R.drawable.no_thumbnail_available,
                R.drawable.no_thumbnail_available,
                R.drawable.no_thumbnail_available,
                R.drawable.no_thumbnail_available,
                R.drawable.no_thumbnail_available
        };

        String[] associatedUrls= {
                "http://192.168.1.15:8080/a/a_out.mpd",
                "http://192.168.1.15:8080/c/c_out.mpd",
                "http://192.168.1.15:8080/b/videofull1_out.mpd",
                "https://content.jwplatform.com/manifests/yp34SRmf.m3u8",
                "https://dash.akamaized.net/dash264/TestCasesIOP33/adapatationSetSwitching/5/manifest.mpd",
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



    public static void slideView(View view,
                                 int currentHeight,
                                 int newHeight) {

        ValueAnimator slideAnimator = ValueAnimator
                .ofInt(currentHeight, newHeight)
                .setDuration(500);

        /* We use an update listener which listens to each tick
         * and manually updates the height of the view  */


        slideAnimator.addUpdateListener(animation1 -> {
            view.getLayoutParams().height = (Integer) animation1.getAnimatedValue();
            view.requestLayout();
        });

        /*  We use an animationSet to play the animation  */

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animationSet.play(slideAnimator);
        animationSet.start();
    }

}
