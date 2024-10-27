package com.example.bakelink.customers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.customers.modal.CustomCakeRequest;

import java.util.List;

public class CakeRequestAdapter extends RecyclerView.Adapter<CakeRequestAdapter.CakeRequestViewHolder>{

    private List<CustomCakeRequest> cakeRequests;

    public CakeRequestAdapter(List<CustomCakeRequest> cakeRequests) {
        this.cakeRequests = cakeRequests;
    }

    public CakeRequestAdapter() {
    }

    @NonNull
    @Override
    public CakeRequestAdapter.CakeRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cakerequest, parent, false);
        return new CakeRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeRequestAdapter.CakeRequestViewHolder holder, int position) {
        CustomCakeRequest request = cakeRequests.get(position);

        String cakeDetails = "Event date : " + request.getDeliveryTime() + "\n"
                            + "Requested date : 11/11/2023\n" + "Quotes : 2 received";

        holder.cakeType.setText(request.getCakeType());
        holder.cakeDetails.setText(cakeDetails);
        Glide.with(holder.cakeImage.getContext())
                .load(request.getImageUrl())
                .placeholder(R.drawable.cakesample1) // Optional placeholder while loading
                .error(R.drawable.cakesample1) // Optional error image if loading fails
                .into(holder.cakeImage);

    }

    @Override
    public int getItemCount() {
        return cakeRequests.size();
    }

    public class CakeRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView cakeImage;
        TextView cakeType;

        TextView cakeDetails;
        public CakeRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            cakeImage = itemView.findViewById(R.id.img_requested_cake);
            cakeType = itemView.findViewById(R.id.txtRequestedCakeType);
            cakeDetails = itemView.findViewById(R.id.txtRequestedCakeDetails);
        }
    }
}