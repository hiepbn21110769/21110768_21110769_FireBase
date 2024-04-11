package com.example.firebaseexample;

import android.os.AsyncTask;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class ImageLoad extends AsyncTask<Void, Void, Void> {
    private WeakReference<ImageView> imageViewWeakReference;
    private String imageURL;

    public ImageLoad(ImageView imageView, String imageURL) {
        imageViewWeakReference = new WeakReference<>(imageView);
        this.imageURL = imageURL;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        // Không cần thực hiện bất kỳ công việc nào ở đây, vì việc load hình ảnh sẽ được thực hiện trong onPostExecute
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        ImageView imageView = imageViewWeakReference.get();
        if (imageView != null) {
            // Sử dụng Glide để load hình ảnh từ imageURL và hiển thị vào imageView
            Glide.with(imageView.getContext())
                    .load(imageURL)
                    .into(imageView);
        }
    }
}


