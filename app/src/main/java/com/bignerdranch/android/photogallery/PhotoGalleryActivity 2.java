package com.bignerdranch.android.photogallery;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PhotoGalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);

        boolean isFragmentContainerEmpty = savedInstanceState == null;
        if (isFragmentContainerEmpty) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, PhotoGalleryFragment.newInstance())
                    .commit();
        }
    }
}