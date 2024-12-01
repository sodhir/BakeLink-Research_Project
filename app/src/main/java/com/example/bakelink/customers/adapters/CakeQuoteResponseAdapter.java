package com.example.bakelink.customers.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.B_ViewQuoteActivity;
import com.example.bakelink.bakers.adapters.TrackSubmittedQuotesAdapter;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.example.bakelink.customers.C_ViewQuoteDetails;

import java.util.List;

public class CakeQuoteResponseAdapter extends RecyclerView.Adapter<CakeQuoteResponseAdapter.ViewHolder> {

    private List<QuoteResponse> cakeQuoteResponses;

    public CakeQuoteResponseAdapter(List<QuoteResponse> cakeQuoteResponses) {
        this.cakeQuoteResponses = cakeQuoteResponses;
    }

    @NonNull
    @Override
    public CakeQuoteResponseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_quote_per_cake_request, parent, false);
        return new CakeQuoteResponseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakeQuoteResponseAdapter.ViewHolder holder, int position) {

        QuoteResponse quoteResponse = cakeQuoteResponses.get(position);
        holder.bakeryName.setText(quoteResponse.getBakeryTitle());
        holder.cakePrice.setText("Quoted price: $" + quoteResponse.getQuotedPrice());
        holder.cakeDetails.setText("Cake: " + quoteResponse.getCakeType());

        // Set status and background color based on quote status
        holder.quoteStatus.setText(quoteResponse.getStatus());
        if ("Awaiting Approval".equals(quoteResponse.getStatus())) {
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#FFD700"));
            holder.quoteStatus.setTextColor(Color.parseColor("#B77400"));
        } else if ("Accepted".equals(quoteResponse.getStatus())) {
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#88A84F"));
            holder.quoteStatus.setTextColor(Color.parseColor("#FFFFFF"));
        } else if ("Rejected".equals(quoteResponse.getStatus())) {
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#8B0000")); // Example color
            holder.quoteStatus.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            // Default case for any other statuses
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#D3D3D3"));
            holder.quoteStatus.setTextColor(Color.parseColor("#000000"));
        }


        holder.viewQuoteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext().getApplicationContext(), C_ViewQuoteDetails.class);
                intent.putExtra("responseId", quoteResponse.getQuoteResponseId());
                Log.d("quoteResponseId", quoteResponse.getQuoteResponseId());
                intent.putExtra("customCakeRequestId", quoteResponse.getCustomCakeRequestId());
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return cakeQuoteResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

       // ImageView quoteImage;
        TextView bakeryName, quoteStatus, cakeDetails, cakePrice, cakeFlavor;
        Button viewQuoteDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bakeryName = itemView.findViewById(R.id.bakery_name);
            quoteStatus = itemView.findViewById(R.id.quote_status);
            cakeDetails = itemView.findViewById(R.id.cake_details);
            cakePrice = itemView.findViewById(R.id.cake_price);
            cakeFlavor = itemView.findViewById(R.id.cake_flavor);
            viewQuoteDetails = itemView.findViewById(R.id.view_quote_details);
        }
    }
}
