package com.example.bakelink.customers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.customers.modal.Baker;

import java.util.List;
import com.bumptech.glide.Glide;


public class TrendingBakerAdapter extends RecyclerView.Adapter<TrendingBakerAdapter.ViewHolder> {
    private List<Baker> bakersList;

    public TrendingBakerAdapter(List<Baker> bakersList) {
        this.bakersList = bakersList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bakerImage;
        public TextView bakerName;
        public TextView bakerRating;
        public ImageView star1;
        public ImageView star2;
        public ImageView star3;
        public ImageView star4;
        public ImageView star5;

        public ViewHolder(View view) {
            super(view);
            bakerImage = view.findViewById(R.id.baker_image);
            bakerName = view.findViewById(R.id.baker_name);
            bakerRating = view.findViewById(R.id.baker_rating);
            star1 = view.findViewById(R.id.star_1);
            star2 = view.findViewById(R.id.star_2);
            star3 = view.findViewById(R.id.star_3);
            star4 = view.findViewById(R.id.star_4);
            star5 = view.findViewById(R.id.star_5);
        }
    }

    @Override
    public TrendingBakerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending_baker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Baker baker = bakersList.get(position);
        holder.bakerName.setText(baker.getName());
        holder.bakerRating.setText(String.valueOf(baker.getRating()));

        Context context = holder.itemView.getContext();

        String imageUrl = baker.getImageUrl();

        // Checking if the imageUrl starts with "drawable" or is a URL
        if (imageUrl != null && imageUrl.startsWith("drawable/")) {
            // Get the drawable resource name from imageUrl
            String drawableName = imageUrl.replace("drawable/", "");

            // Get the resource ID from the drawable name
            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

            // Use Glide to load from the drawable resource
            Glide.with(context)
                    .load(resourceId) // Load from drawable
                    .into(holder.bakerImage);
        } else {
            // Load from URL using Glide
            Glide.with(context)
                    .load(imageUrl) // Load from URL
                    .into(holder.bakerImage);
        }

        // Setting the number of filled stars based on the rating
        int rating = (int) Math.round(baker.getRating());
        holder.star1.setImageResource(rating >= 1 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        holder.star2.setImageResource(rating >= 2 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        holder.star3.setImageResource(rating >= 3 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        holder.star4.setImageResource(rating >= 4 ? R.drawable.ic_star_filled : R.drawable.ic_star);
        holder.star5.setImageResource(rating >= 5 ? R.drawable.ic_star_filled : R.drawable.ic_star);
    }

    @Override
    public int getItemCount() {
        return bakersList.size();
    }
}

