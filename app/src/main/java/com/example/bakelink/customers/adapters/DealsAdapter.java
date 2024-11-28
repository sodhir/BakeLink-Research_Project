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
import com.example.bakelink.customers.modal.Deal;

import java.util.List;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> {

    private List<Deal> dealsList;

    public DealsAdapter(List<Deal> dealsList) {
        this.dealsList = dealsList;
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_deal, parent, false);
        return new DealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        Deal deal = dealsList.get(position);
        holder.dealName.setText(deal.getName());
        holder.dealTag.setText(deal.getDealCode());
        holder.dealValidTill.setText(deal.getValidTill());
        holder.dealTopTag.setText(deal.getDealTag());

        Context context = holder.itemView.getContext();
        String imageUrl = deal.getImageUrl();
        // Checking if the imageUrl starts with "drawable" or is a URL
        if (imageUrl != null && imageUrl.startsWith("drawable/")) {
            // Get the drawable resource name from imageUrl
            String drawableName = imageUrl.replace("drawable/", "");

            // Get the resource ID from the drawable name
            int resourceId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());

            // Use Glide to load from the drawable resource
            Glide.with(context)
                    .load(resourceId) // Load from drawable
                    .into(holder.dealImage);
        } else {
            // Load from URL using Glide
            Glide.with(context)
                    .load(imageUrl) // Load from URL
                    .into(holder.dealImage);
        }
    }

    @Override
    public int getItemCount() {
        return dealsList.size();
    }

    static class DealViewHolder extends RecyclerView.ViewHolder {
        ImageView dealImage;
        TextView dealTag, dealName, dealValidTill, dealTopTag;

        DealViewHolder(@NonNull View itemView) {
            super(itemView);
            dealImage = itemView.findViewById(R.id.rImage);
            dealTag = itemView.findViewById(R.id.deal_tag);
            dealName = itemView.findViewById(R.id.deal_name);
            dealValidTill = itemView.findViewById(R.id.deal_valid_till);
            dealTopTag = itemView.findViewById(R.id.rBakerName);
        }
    }
}
