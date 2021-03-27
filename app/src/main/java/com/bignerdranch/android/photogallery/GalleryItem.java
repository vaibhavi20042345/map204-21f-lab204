package com.bignerdranch.android.photogallery;

import com.google.gson.annotations.SerializedName;

public class GalleryItem {

    String title;
    String id;
    @SerializedName("url_s")
    String url;

    GalleryItem() {
        title = "";
        id = "";
        url = "";
    }
}
