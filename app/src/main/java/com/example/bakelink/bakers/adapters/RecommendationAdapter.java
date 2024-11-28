package com.example.bakelink.bakers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.models.RecommendationCake;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {

    List<RecommendationCake> recommendationCakes;

    public RecommendationAdapter(List<RecommendationCake> recommendationCakes) {
        this.recommendationCakes = recommendationCakes;
    }

    public RecommendationAdapter() {
    }

    public List<RecommendationCake> getRecommendationCakes() {
        return recommendationCakes;
    }

    public void setRecommendationCakes(List<RecommendationCake> recommendationCakes) {
        this.recommendationCakes = recommendationCakes;
    }

    @NonNull
    @Override
    public RecommendationAdapter.RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.RecommendationViewHolder holder, int position) {
          RecommendationCake recommendationCake = recommendationCakes.get(position);
          String imageUrl = recommendationCake.getImageUrl();
          String bakerName = "Test Baker";

          Glide.with(holder.itemView.getContext())
                  .load(imageUrl) // Load from URL
                  .into(holder.rImage);

          holder.rBakerName.setText(bakerName);
    }

    @Override
    public int getItemCount() {
        return recommendationCakes.size();
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {

        ImageView rImage;
        TextView rBakerName;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);

            rImage = itemView.findViewById(R.id.rImage);
            rBakerName = itemView.findViewById(R.id.rBakerName);
        }
    }
}
