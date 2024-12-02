package com.example.bakelink.customers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.customers.modal.CakeCategory;

import java.util.List;

public class TopCakeCategoryAdapter extends RecyclerView.Adapter<TopCakeCategoryAdapter.ViewHolder> {
    private List<CakeCategory> categoryList;

    public TopCakeCategoryAdapter(List<CakeCategory> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_cake_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CakeCategory category = categoryList.get(position);
        holder.categoryName.setText(category.getName());

        Context context = holder.itemView.getContext();
        String imageUrl = category.getImageUrl();

        if (imageUrl != null && imageUrl.startsWith("drawable/")) {

            String drawableName = imageUrl.replace("drawable/", "");


            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());


            Glide.with(context)
                    .load(resourceId)
                    .into(holder.categoryImage);
        } else {

            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.categoryImage);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImage;
        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.category_image);
            categoryName = itemView.findViewById(R.id.category_name);
        }
    }
}

