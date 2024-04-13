    package com.example.firebaseexample;

    import android.annotation.SuppressLint;
    import android.app.Activity;
    import android.content.Intent;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Color;
    import android.media.Image;
    import android.view.ContextMenu;
    import android.view.LayoutInflater;
    import android.view.MenuInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.core.content.ContextCompat;
    import androidx.recyclerview.widget.RecyclerView;
//    import com.bumptech.glide.Glide;

    import java.io.File;
    import java.io.IOException;
    import java.lang.ref.WeakReference;
    import java.net.URL;
    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Set;

    public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ImageViewHolder>{

        private List<String> imageURLs;
        private List<String> selectedItems = new ArrayList<>();

        boolean isSelectedMode = false;


        public ViewAdapter(List<String> imageURLs) {
            this.imageURLs = imageURLs;
        }

        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ImageViewHolder holder, @SuppressLint("RecyclerView") int position) {
            String imageURL = imageURLs.get(position);
            // Sử dụng ImageLoad để load hình ảnh từ imageURL và hiển thị vào imageView
            new ImageLoad(  holder.imageView, imageURL).execute();


            // Sự kiện click vào ImageView
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Chuyển đến màn hình mới và hiển thị ảnh ở kích thước lớn
                    Intent intent = new Intent(v.getContext(), FullScreenImageActivity.class);
                    intent.putExtra("imageURL",imageURL);
                    v.getContext().startActivity(intent);
                }
            });




        }

        @Override
        public int getItemCount() {
            return imageURLs.size();
        }

        public static class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView checkmark;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }

        public void toggleSelection(int position) {
            String item = imageURLs.get(position);
            if (selectedItems.contains(item)) {
                selectedItems.remove(item);

            } else {
                selectedItems.add(item);
            }
            notifyDataSetChanged();
        }




    }
