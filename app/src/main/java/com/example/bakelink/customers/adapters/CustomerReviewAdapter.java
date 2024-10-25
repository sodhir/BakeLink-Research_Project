package com.example.bakelink.customers.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;

import java.util.List;

public class CustomerReviewAdapter extends RecyclerView.Adapter<CustomerReviewAdapter.CustomerReviewViewHolder> {

    private List<String> reviewList;

    public CustomerReviewAdapter(List<String> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public CustomerReviewAdapter.CustomerReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_customerreview, parent, false);
        return new CustomerReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerReviewAdapter.CustomerReviewViewHolder holder, int position) {
            holder.reviewTextView.setText(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public class CustomerReviewViewHolder extends RecyclerView.ViewHolder {

        TextView reviewTextView;
        public CustomerReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewTextView = itemView.findViewById(R.id.txtCustomerReview);
        }
    }
}
