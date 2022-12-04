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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements IRecyclerView {

    private static final ArrayList<VideoCardModel> videoCardModels = new ArrayList<>();
    private MainActivity mainActivity;
    VC_RecycleViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mainActivity = (MainActivity) getActivity();


        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_home);

        setupVideoCardModels();


        Log.i("zz", String.valueOf(videoCardModels.size()));
        adapter = new VC_RecycleViewAdapter(getContext(), videoCardModels, this);

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
        ArrayList<String> ids = new ArrayList<>();

        String Tag = "vhagar";
        Log.i(Tag, "Starting");
        Call<List<Video>> call = RetrofitClient.getInstance().getAPI().getVideos();
        Log.i(Tag, "call ");
        call.enqueue(new Callback<List<Video>>() {
            @Override
            public void onResponse(Call<List<Video>> call, Response<List<Video>> response) {
                Log.i(Tag, "Got data");
                List<Video> videoList = response.body();
                Log.i("size", String.valueOf(videoList.size()));
                Log.i(Tag, videoList.get(0).getTitle());
                Log.i(Tag, videoList.get(0).getDescription());
                for (int i = 0; i < videoList.size(); i++) {
                    Log.i(Tag, "xxxx");
                    if (!ids.contains(videoList.get(i).getID())){
                        ids.add(videoList.get(i).getID());
                        videoCardModels.add(new VideoCardModel(R.drawable.no_thumbnail_available, videoList.get(i).getTitle(),
                                videoList.get(i).getDescription(),
                                BuildConfig.FILE_SYSTEM_URL+videoList.get(i).getID()+"/"+videoList.get(i).getID()+"_out.mpd"));

                        adapter.notifyItemInserted(videoCardModels.size() - 1);
                    }

                }
                Log.i("qqq", String.valueOf(videoCardModels.size()));
            }

            @Override
            public void onFailure(Call<List<Video>> call, Throwable t) {

                Log.e(Tag, "Mission Faileddddd");
//                Toast.makeText(getApplicationContext(), "An error has occured", Toast.LENGTH_LONG).show();
            }

        });

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
