package com.example.bakelink.bakers.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.B_ViewQuoteActivity;
import com.example.bakelink.bakers.models.QuoteRequest;
import com.example.bakelink.customers.modal.CustomCakeRequest;

import java.util.List;

public class QuoteRequestAdapter extends RecyclerView.Adapter<QuoteRequestAdapter.ViewHolder> {

    private Context context;
    private List<CustomCakeRequest> quoteRequestList;

    public QuoteRequestAdapter(Context context, List<CustomCakeRequest> quoteRequestList) {
        this.context = context;
        this.quoteRequestList = quoteRequestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_new_quote_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomCakeRequest quoteRequest = quoteRequestList.get(position);

        holder.customerName.setText(quoteRequest.getUserEmail());
        holder.cakeType.setText(quoteRequest.getCakeType());
        holder.deliveryDate.setText(quoteRequest.getDeliveryDate());
       // holder.location.setText(quoteRequest.getLocation());
        Glide.with(holder.itemView.getContext())
                .load(quoteRequest.getImageUrl())
                .error(R.drawable.cakesample1) // Load from URL
                .into(holder.cakeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, B_ViewQuoteActivity.class);
                intent.putExtra("quoteId", quoteRequest.getCustomCakeRequestId()); // Pass the unique ID or necessary data
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return quoteRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cakeImage;
        TextView customerName, cakeType, deliveryDate, location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cakeImage = itemView.findViewById(R.id.cake_image);
            customerName = itemView.findViewById(R.id.customer_name);
            cakeType = itemView.findViewById(R.id.cake_type);
            deliveryDate = itemView.findViewById(R.id.delivery_date);
            location = itemView.findViewById(R.id.location);
        }
    }
}
