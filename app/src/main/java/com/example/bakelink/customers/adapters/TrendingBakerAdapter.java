package com.example.bakelink.customers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.BakerPage;
import com.example.bakelink.customers.modal.Baker;

import java.util.List;


public class TrendingBakerAdapter extends RecyclerView.Adapter<TrendingBakerAdapter.ViewHolder> {
    private List<Baker> bakersList;

    public TrendingBakerAdapter(List<Baker> bakersList) {
        this.bakersList = bakersList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
            bakerName = view.findViewById(R.id.cake_name);
            bakerRating = view.findViewById(R.id.txtBakerRating1);
            star1 = view.findViewById(R.id.star_1);
            star2 = view.findViewById(R.id.star_2);
            star3 = view.findViewById(R.id.star_3);
            star4 = view.findViewById(R.id.star_4);
            star5 = view.findViewById(R.id.star_5);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), BakerPage.class);
                    intent.putExtra("bakerId", bakersList.get(getAdapterPosition()).getId());
                    intent.putExtra("bakerName", bakersList.get(getAdapterPosition()).getName());
                    intent.putExtra("bakerImage", bakersList.get(getAdapterPosition()).getImageUrl());
                    intent.putExtra("bakerRating", bakersList.get(getAdapterPosition()).getRating());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending_baker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Baker baker = bakersList.get(position);
        holder.bakerName.setText(baker.getName());
        if(baker.getRating() == 0.0f){
            //do nothing
        }else{
            holder.bakerRating.setText(String.valueOf(baker.getRating()));
        }


        Context context = holder.itemView.getContext();

        String imageUrl = baker.getImageUrl();


        if (imageUrl != null && imageUrl.startsWith("drawable/")) {

            String drawableName = imageUrl.replace("drawable/", "");


            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());


            Glide.with(context)
                    .load(resourceId)
                    .into(holder.bakerImage);
        } else {

            Glide.with(context)
                    .load(imageUrl)
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

