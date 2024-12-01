package com.example.bakelink.bakers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.customers.CakeAddToCartBottomSheet;
import com.example.bakelink.customers.adapters.CakeServiceAdapter;

import java.util.List;

public class BakerCakeAdapter extends RecyclerView.Adapter<BakerCakeAdapter.BakerCakeViewHolder> {

    List<Cake> cakeList;

    public BakerCakeAdapter(List<Cake> cakeList) {
        this.cakeList = cakeList;
    }

    @NonNull
    @Override
    public BakerCakeAdapter.BakerCakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bakercake, parent, false);
        return new BakerCakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakerCakeAdapter.BakerCakeViewHolder holder, int position) {
        Cake cake = cakeList.get(position);
        holder.cakeName.setText(cake.getCakeName());

        Context context = holder.itemView.getContext();
        String imageUrl = cake.getCakeImgUrl();
        // Checking if the imageUrl starts with "drawable" or is a URL
        if (imageUrl != null && imageUrl.startsWith("drawable/")) {
            // Get the drawable resource name from imageUrl
            String drawableName = imageUrl.replace("drawable/", "");

            // Get the resource ID from the drawable name
            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

            // Use Glide to load from the drawable resource
            Glide.with(context)
                    .load(resourceId) // Load from drawable
                    .into(holder.cakeImg);
        } else {
            // Load from URL using Glide
            Glide.with(context)
                    .load(imageUrl) // Load from URL
                    .into(holder.cakeImg);
        }

        holder.itemView.setOnClickListener(v -> {
            CakeAddToCartBottomSheet bottomSheet = CakeAddToCartBottomSheet.newInstance(
                    cake.getCakeId(),
                    cake.getCakeName(),
                    cake.getDescription(),
                    cake.getPrice(),
                    cake.getCakeImgUrl(),
                    cake.getWeights().get(0)
            );
            bottomSheet.show(((AppCompatActivity) context).getSupportFragmentManager(), "CakeAddToCartBottomSheet");
        });

    }

    @Override
    public int getItemCount() {
        return cakeList.size();
    }

    public class BakerCakeViewHolder extends RecyclerView.ViewHolder {

        ImageView cakeImg;
        TextView cakeName;

        public BakerCakeViewHolder(@NonNull View itemView) {
            super(itemView);

            cakeImg = itemView.findViewById(R.id.bakercake_img);
            cakeName = itemView.findViewById(R.id.cake_name);
        }
    }
}
