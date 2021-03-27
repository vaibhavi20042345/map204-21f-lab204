package com.bignerdranch.android.photogallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.bignerdranch.android.photogallery.api.FlickrFetchr;

import java.util.List;

public class PhotoGalleryViewModel extends ViewModel {
    LiveData<List<GalleryItem>> galleryItemLiveData;
    public PhotoGalleryViewModel() {
        galleryItemLiveData = new FlickrFetchr().fetchPhotos();
    }
}
