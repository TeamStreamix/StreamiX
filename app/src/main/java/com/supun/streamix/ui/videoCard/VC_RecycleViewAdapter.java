package com.supun.streamix.ui.videoCard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.supun.streamix.R;

import java.util.ArrayList;

public class VC_RecycleViewAdapter extends RecyclerView.Adapter<VC_RecycleViewAdapter.MyViewHolder> {

    private final IRecyclerView recyclerViewInterface;

    Context context;
    ArrayList<VideoCardModel> videoCardModels;

    public VC_RecycleViewAdapter(Context context, ArrayList<VideoCardModel> videoCardModels, IRecyclerView recyclerViewInterface){
        this.context = context;
        this.videoCardModels = videoCardModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public VC_RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*
         * This is where you inflate the layout(giving a look to our rows)
         */
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.video_card_row, parent, false);
        return new VC_RecycleViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull VC_RecycleViewAdapter.MyViewHolder holder, int position) {
        /*
         * Assigning values to the views we created in the recycler_view_row layout file
         * based on the position of the recycle view
         */

        holder.title.setText(videoCardModels.get(position).getTitle());
        holder.description.setText(videoCardModels.get(position).getDescription());
        holder.thumbnail.setImageResource(videoCardModels.get(position).getThumbnail());


    }

    @Override
    public int getItemCount() {
        /*
         * Number of items that need to be displayed using the recycler view
         */
        return videoCardModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        /*
        Grabbing the views from our recycler_view_row layout file
        Like in the onCreate method
         */

        private ImageView thumbnail;
        private TextView title, description;

        public MyViewHolder(@NonNull View itemView, IRecyclerView recyclerViewInterface) {

            super(itemView);

            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.video_title);
            description = itemView.findViewById(R.id.description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerViewInterface != null){
                      int pos = getAdapterPosition();

                      if (pos != RecyclerView.NO_POSITION){
                          recyclerViewInterface.onCardClick(pos);
                      }
                    }
                }
            });



        }
    }
}
