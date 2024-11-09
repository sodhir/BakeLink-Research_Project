package com.example.bakelink.bakers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.QuoteRequest;

import java.util.List;

public class QuoteRequestAdapter extends RecyclerView.Adapter<QuoteRequestAdapter.ViewHolder> {

    private Context context;
    private List<QuoteRequest> quoteRequestList;

    public QuoteRequestAdapter(Context context, List<QuoteRequest> quoteRequestList) {
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
        QuoteRequest quoteRequest = quoteRequestList.get(position);

        holder.customerName.setText(quoteRequest.getCustomerName());
        holder.cakeType.setText(quoteRequest.getCakeType());
        holder.deliveryDate.setText(quoteRequest.getDeliveryDate());
        holder.location.setText(quoteRequest.getLocation());
        holder.cakeImage.setImageResource(quoteRequest.getImageResource());
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
