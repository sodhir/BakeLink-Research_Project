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

        public ViewHolder(View view) {
            super(view);
            bakerImage = view.findViewById(R.id.baker_image);
            bakerName = view.findViewById(R.id.baker_name);
            bakerRating = view.findViewById(R.id.baker_rating);
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
        Glide.with(context)
                .load(baker.getImageUrl())
                .into(holder.bakerImage);
    }

    @Override
    public int getItemCount() {
        return bakersList.size();
    }
}

