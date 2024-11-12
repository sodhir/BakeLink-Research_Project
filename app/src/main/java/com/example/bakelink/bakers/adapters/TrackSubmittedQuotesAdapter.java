package com.example.bakelink.bakers.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.Quote;
import com.example.bakelink.bakers.models.QuoteRequest;

import java.util.List;

public class TrackSubmittedQuotesAdapter extends RecyclerView.Adapter<TrackSubmittedQuotesAdapter.ViewHolder> {

    private List<Quote> quotesSent;
    private Context context;

    public TrackSubmittedQuotesAdapter(Context context, List<Quote> quotesSent) {
        this.context = context;
        this.quotesSent = quotesSent;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_track_submitted_quote, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote quote = quotesSent.get(position);
        holder.quoteImage.setImageResource(quote.getImageResource());
        holder.customerName.setText(quote.getCustomerName());
        holder.quoteAmount.setText("$" + quote.getAmount());

        // Set status and background color based on quote status
        holder.quoteStatus.setText(quote.getStatus());

        if ("Awaiting Approval".equals(quote.getStatus())) {
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#FFD700"));
            holder.quoteStatus.setTextColor(Color.parseColor("#B77400"));
        } else if ("Approved".equals(quote.getStatus())) {
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#88A84F"));
            holder.quoteStatus.setTextColor(Color.parseColor("#FFFFFF"));
        } else if ("Rejected".equals(quote.getStatus())) {
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#8B0000")); // Example color
            holder.quoteStatus.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            // Default case for any other statuses
            holder.quoteStatus.setBackgroundColor(Color.parseColor("#D3D3D3"));
            holder.quoteStatus.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return quotesSent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView quoteImage;
        TextView customerName, quoteAmount, quoteStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            quoteImage = itemView.findViewById(R.id.img_quote);
            customerName = itemView.findViewById(R.id.tv_customer_name);
            quoteAmount = itemView.findViewById(R.id.tv_quote_amount);
            quoteStatus = itemView.findViewById(R.id.tv_quote_status);
        }
    }
}

