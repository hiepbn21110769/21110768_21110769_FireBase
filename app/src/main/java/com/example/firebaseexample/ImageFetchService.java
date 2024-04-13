package com.example.firebaseexample;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageFetchService extends Service {
    private static final int BATCH_SIZE = 20; // Number of images to fetch in each batch
    private StorageReference storageRef;
    private List<String> imageUrls;
    private int startIndex = 0;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fetchNextBatch();
        return super.onStartCommand(intent, flags, startId);
    }

    public void fetchNextBatch() {
        // Ensure the operation is executed on a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Simulate fetching image URLs from a remote server or database
                List<String> newImageUrls = fetchImageUrls();
            }
        }).start();
    }

    private List<String> fetchImageUrls() {

        List<String> imageUrls = new ArrayList<>();
        storageRef = FirebaseStorage.getInstance().getReference().child("images");
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                int endIndex = Math.min(startIndex + BATCH_SIZE, listResult.getItems().size());
                for (int i = startIndex; i < endIndex; i++) {
                    StorageReference item = listResult.getItems().get(i);
                    // Get download URL for each image
                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            // Add the download URL to the list of image URLs
                            imageUrls.add(uri.toString());
                            if (imageUrls.size() == BATCH_SIZE) {
                                // Once BATCH_SIZE number of URLs are fetched, send broadcast
                                sendBroadcast(imageUrls);
                            }
                        }
                    });
                }
                startIndex = endIndex;
            }
        });
        return imageUrls;
    }
    private void sendBroadcast(List<String> imageUrls) {
        Intent broadcastIntent = new Intent("IMAGE_FETCH_COMPLETE");
        broadcastIntent.putStringArrayListExtra("imageUrls", (ArrayList<String>) imageUrls);
        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

}
