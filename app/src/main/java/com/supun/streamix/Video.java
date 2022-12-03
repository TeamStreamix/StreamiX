package com.supun.streamix;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("_id")
    private String _id;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("__v")
    private String __v;

}
