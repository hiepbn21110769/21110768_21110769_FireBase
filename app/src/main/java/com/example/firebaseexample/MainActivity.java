package com.example.firebaseexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private Button uploadButton;
    private RecyclerView recyclerView;
    private ViewAdapter imageAdapter;
    private GridLayoutManager layoutManager;
    private List<String> imageURLs;
    private ImageFetchService imageFetchService;
    private boolean isLoading = false;


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("IMAGE_FETCH_COMPLETE")){
                ArrayList<String> newImageUrls  = intent.getStringArrayListExtra("imageUrls");
                imageURLs.addAll(newImageUrls);
                imageAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uploadButton = findViewById(R.id.uploadBtn);
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        imageURLs = new ArrayList<>();
        // Thêm các URL hình ảnh vào imageURLs ở đây

        imageAdapter = new ViewAdapter(imageURLs);
        recyclerView.setAdapter(imageAdapter);

        imageFetchService = new ImageFetchService();
        startService(new Intent(this, ImageFetchService.class));
        imageFetchService.fetchNextBatch();
        // Load danh sách hình ảnh từ Firebase Storage và hiển thị lên RecyclerView

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!isLoading && newState == RecyclerView.SCROLL_STATE_IDLE && layoutManager.findLastVisibleItemPosition() == imageAdapter.getItemCount() - 1){
                    isLoading = true;
                    imageFetchService.fetchNextBatch();
                }
            }
        });
        IntentFilter filter = new IntentFilter("IMAGE_FETCH_COMPLETE");
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang activity_upload_image khi nhấn nút uploadButton
                Intent intent = new Intent(MainActivity.this, Upload_Image_Activity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);


    }
//    private void loadImagesFromFirebase() {
//        // Lấy danh sách tất cả các file trong thư mục "images" trên Firebase Storage
//        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//                for (StorageReference item : listResult.getItems()) {
//                    // Lấy URL của hình ảnh và thêm vào danh sách imageURLs
//                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                        @Override
//                        public void onSuccess(Uri uri) {
//                            imageURLs.add(uri.toString());
//                            imageAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView khi có dữ liệu mới
//                        }
//                    });
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                // Xử lý khi có lỗi xảy ra trong quá trình lấy dữ liệu từ Firebase Storage
//                Toast.makeText(MainActivity.this, "Failed to load images from Firebase Storage.", Toast.LENGTH_SHORT).show();
//            }
//        });
    //}


}