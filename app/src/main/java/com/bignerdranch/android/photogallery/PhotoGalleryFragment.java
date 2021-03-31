package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PhotoGalleryFragment extends Fragment {
    private RecyclerView mPhotoRecyclerView;
    private ThumbnailDownloader<PhotoHolder> mThumbnailDownloader;

    private static String TAG = "PhotoGalleryFragment";
    private PhotoGalleryViewModel mPhotoGalleryViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mPhotoRecyclerView = view.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        return view;
    }
    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Retrofit retrofit = new Retrofit.Builder()
              // .baseUrl("https://www.flickr.com/")
              //  .addConverterFactory(ScalarsConverterFactory.create())
               // .build();
       // FlickrApi flickrApi = retrofit.create(FlickrApi.class);
      /*  Call<String> flickrHomePageRequest = flickrApi.fetchContents();

        flickrHomePageRequest.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "Response received: " + response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failed to fetch photos", t);
            }
        });  */

        /*LiveData<List<GalleryItem>> flickrLiveData = new FlickrFetchr().fetchPhotos();
        flickrLiveData.observe(this, new Observer<List<GalleryItem>>() {
            @Override
            public void onChanged(List<GalleryItem> response) {
                Log.d(TAG, String.valueOf(response));
            }
        }); */
        mPhotoGalleryViewModel = ViewModelProviders.of(this).get(PhotoGalleryViewModel.class);
        setRetainInstance(true);
        Handler responseHandler = new Handler();
        mThumbnailDownloader = new ThumbnailDownloader<>(responseHandler);
        mThumbnailDownloader.setThumbnailDownloadListener(
                new ThumbnailDownloader.ThumbnailDownloadListener<PhotoHolder>() {
                    @Override
                public void onThumbnailDownloaded(PhotoHolder photoHolder,
                                                  Bitmap bitmap) {
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    photoHolder.bindDrawable(drawable);
                } }
        );
        getLifecycle().addObserver(mThumbnailDownloader.mFragmentLifecycleObserver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getViewLifecycleOwner().getLifecycle().removeObserver(mThumbnailDownloader.mViewLifecycleObserver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getViewLifecycleOwner().getLifecycle().addObserver(mThumbnailDownloader.mViewLifecycleObserver);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPhotoGalleryViewModel.galleryItemLiveData.observe(
                getViewLifecycleOwner(),
                new Observer<List<GalleryItem>>() {
                    @Override
                    public void onChanged(List<GalleryItem> galleryItems) {
                     //   Log.d(TAG, "Have gallery items from ViewModel " + galleryItems);
// Eventually, update data backing the recycler view
                        mPhotoRecyclerView.setAdapter(new PhotoAdapter(galleryItems));
                    }
                });
    }

    private class PhotoHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;
       // TextView mTitleTextView;
        public PhotoHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_gallery, parent, false));
            mItemImageView = (ImageView) itemView.findViewById(R.id.item_image_view);
        }
        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }
    }

    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {
        private List<GalleryItem> mGalleryItems;
        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;;
        }
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new PhotoHolder(layoutInflater, parent);
        }
        @Override
        public void onBindViewHolder(PhotoHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            Drawable placeholder = ContextCompat.getDrawable(requireContext(), R.drawable.bill_up_close);
            holder.bindDrawable(placeholder);
            mThumbnailDownloader.queueThumbnail(holder, galleryItem.url);
        }
        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }
}
