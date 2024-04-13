package com.example.firebaseexample;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullScreenImageActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;
    private String imageURL;

    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);

        ImageView imageView = findViewById(R.id.fullScreenImageView);
        Button downloadButton = findViewById(R.id.downloadButton);
        Button deleteButton = findViewById(R.id.deleteButton);

        imageURL = getIntent().getStringExtra("imageURL");
        storage = FirebaseStorage.getInstance(); // Khởi tạo FirebaseStorage

        // Load ảnh từ URL và hiển thị vào ImageView
        Glide.with(this)
                .load(imageURL)
                .into(imageView);

        // Xử lý sự kiện khi nhấn vào nút Download
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nếu đã có quyền WRITE_EXTERNAL_STORAGE, thực hiện tải và lưu hình ảnh
                downloadImage();
            }
        });

        // Xử lý sự kiện khi nhấn vào nút Delete
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức để xoá hình ảnh từ Firebase Storage
                deleteImage();
            }
        });


    }

    private void downloadImage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                // get url/ text from edit text
                String url = imageURL;

                // Thiết lập đường dẫn lưu trữ đúng cách, ví dụ: thư mục Downloads với tên tệp từ URL
                String fileName = URLUtil.guessFileName(url, null, null);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setTitle("Download"); //set title in download notification
                request.setDescription("Downloading file...."); //set description in download notification
                request.allowScanningByMediaScanner();
                request.setDestinationUri(Uri.fromFile(file)); // Thiết lập đường dẫn lưu trữ đúng cách
                request.setAllowedOverMetered(true);

                //get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                long reference = manager.enqueue(request);
                // Hiển thị thông báo thành công trên luồng giao diện người dùng
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(FullScreenImageActivity.this, "Hình ảnh đã được tải về thành công", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();


    }

    private void deleteImage() {
        // Lấy tham chiếu đến hình ảnh trong Firebase Storage
        StorageReference imageRef = storage.getReferenceFromUrl(imageURL);

        // Xoá hình ảnh
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Nếu thành công, hiển thị thông báo
                Toast.makeText(FullScreenImageActivity.this, "Hình ảnh đã được xoá thành công", Toast.LENGTH_SHORT).show();
                // Quay về MainActivity
                Intent intent = new Intent(FullScreenImageActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Nếu thất bại, hiển thị thông báo lỗi
                Toast.makeText(FullScreenImageActivity.this, "Lỗi khi xoá hình ảnh: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



}