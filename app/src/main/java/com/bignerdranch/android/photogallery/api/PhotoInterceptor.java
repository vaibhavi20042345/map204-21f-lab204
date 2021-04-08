package com.bignerdranch.android.photogallery.api;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class PhotoInterceptor implements Interceptor {
    private static final String API_KEY = "f6da0b392a271953abc45692a9097ae0";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        HttpUrl newUrl = originalRequest.url().newBuilder()
                .addQueryParameter("api_key", API_KEY)
                .addQueryParameter("format", "json")
                .addQueryParameter("nojsoncallback", "1")
                .addQueryParameter("extras", "url_s")
                .addQueryParameter("safesearch", "1")
                .build();
        Request newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build();
        return chain.proceed(newRequest);
    }
}
