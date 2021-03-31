package com.bignerdranch.android.photogallery.api;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bignerdranch.android.photogallery.GalleryItem;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FlickrFetchr {
    private static String TAG = "FlickrFetchr";
    private FlickrApi mFlickrApi;
    public FlickrFetchr() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.flickr.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mFlickrApi = retrofit.create(FlickrApi.class);
    }

    public LiveData<List<GalleryItem>> fetchPhotos() {
        MutableLiveData<List<GalleryItem>> responseLiveData = new MutableLiveData<>();
        Call<FlickrResponse> flickrRequest = mFlickrApi.fetchPhotos();
        flickrRequest.enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                Log.d(TAG, "Response received");
                FlickrResponse flickrResponse = response.body();
                PhotoResponse photoResponse = flickrResponse.photos;
                List<GalleryItem> galleryItems = photoResponse.galleryItems;
                responseLiveData.setValue(galleryItems);
            }
            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                Log.e(TAG, "Failed to fetch photos", t);
            }
        });
        return responseLiveData;
    }

    @WorkerThread
    public Bitmap fetchPhoto(String url) {
        try {
            Response<ResponseBody> response = mFlickrApi.fetchUrlBytes(url).execute();
            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
            Log.i(TAG, "Decoded bitmap from Response");
            return bitmap;
        } catch (Exception exception) {
            Bitmap.Config conf = Bitmap.Config.ARGB_8888; return Bitmap.createBitmap(150, 150, conf);
        }
    }

}
