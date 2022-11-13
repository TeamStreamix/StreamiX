package com.supun.streamix.ui.videoCard;


public class VideoCardModel {
    /**
     * This class act as an model for the video card since we are going to use it in a recycler view
     */
    int thumbnail; // TODO: This should be converted into an image
    String title;
    String description; // we can add the recorded date here

    public VideoCardModel(int thumbnail, String title, String description) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.description = description;
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
}
