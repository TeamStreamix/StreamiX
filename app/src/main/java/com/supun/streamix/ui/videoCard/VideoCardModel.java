package com.supun.streamix.ui.videoCard;


public class VideoCardModel {
    /**
     * This class act as an model for the video card since we are going to use it in a recycler view
     */
    int thumbnail; // TODO: This should be converted into an image
    String title;
    String description; // we can add the recorded date here
    String videoURL;

    public VideoCardModel(int thumbnail, String title, String description, String videoURL) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
        this.videoURL = videoURL;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }
}
