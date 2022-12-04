package com.supun.streamix;

import static com.supun.streamix.videoUploadForm.createFolder;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.util.ArrayList;


public class MyFileFragment extends Fragment implements IRecyclerView {

    private static final ArrayList<VideoCardModel> videoCardModels = new ArrayList<>();
    private MainActivity mainActivity;

    private String mediaFolder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_my_files, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_my_files);

        mediaFolder = videoUploadForm.createFolder(getContext());
        Log.i("FOLDER", mediaFolder);

        setupVideoCardModels(mediaFolder);
        VC_RecycleViewAdapter adapter = new VC_RecycleViewAdapter(getContext(), videoCardModels, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        final int[] state = new int[1];

        View viewToolbar = mainActivity.mainToolbarLayout;
        int defaultHeight = 394;
        viewToolbar.getLayoutParams().height = defaultHeight;



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

    private void setupVideoCardModels(String filePath){
        File directory = new File(filePath);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);

        videoCardModels.clear();

        for (File file : files) {

            videoCardModels.add(new VideoCardModel(
                    ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND),
                    file.getName(),
                    "This is a local offline video",
                    file.getAbsolutePath()

            ));

            Log.i("FileAbsPath", file.getAbsolutePath());

        }
    }

    @Override
    public void onCardClick(int position) {

        Intent intent = new Intent(getActivity(), PlayerActivity.class);


        intent.putExtra("VIDEO_URL", videoCardModels.get(position).getVideoURL());
        intent.putExtra("VIDEO_TITLE", videoCardModels.get(position).getTitle());
        intent.putExtra("IS_MP4", 1);

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
