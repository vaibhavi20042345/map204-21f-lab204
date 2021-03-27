package com.bignerdranch.android.photogallery.api;

import com.bignerdranch.android.photogallery.GalleryItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoResponse {

    @SerializedName("photo")
    List<GalleryItem> galleryItems;
}
