    package com.example.firebaseexample;

    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;

    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
//    import com.bumptech.glide.Glide;

    import java.io.File;
    import java.io.IOException;
    import java.lang.ref.WeakReference;
    import java.net.URL;
    import java.util.List;

    public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.ImageViewHolder>{

        private List<String> imageURLs;

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
        public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    //        String imageURL = imageURLs.get(position);
    //        Glide.with(holder.imageView.getContext())
    //                .load(imageURL)
    //                .into(holder.imageView);
            String imageURL = imageURLs.get(position);
            // Sử dụng ImageLoad để load hình ảnh từ imageURL và hiển thị vào imageView
            new ImageLoad(  holder.imageView, imageURL).execute();

        }

        @Override
        public int getItemCount() {
            return imageURLs.size();
        }

        public static class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }

    }
