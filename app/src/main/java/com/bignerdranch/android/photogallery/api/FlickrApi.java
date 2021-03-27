package com.bignerdranch.android.photogallery.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FlickrApi {
  //  @GET("/")
  //  Call<String> fetchContents();

    @GET("services/rest/?method=flickr.interestingness.getList" +
            "&api_key=d0774d5734a006015b7288e2b930abe7" +
            "&format=json" +
            "&nojsoncallback=1" +
            "&extras=url_s")
    Call<FlickrResponse> fetchPhotos();
}
