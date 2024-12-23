package com.example.bakelink.bakers.adapters;

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
import com.example.bakelink.bakers.B_OrderDetailActivity;
import com.example.bakelink.bakers.models.Order;
import com.example.bakelink.bakers.models.OrderItem;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        List<OrderItem> orderItems = order.getOrderItems();


        //holder.cakeImage.setImageResource(order.getImageResource());
       // holder.customerName.setText(order.getCustomerName());
        holder.cakeType.setText(order.getCakeType());
        holder.deliveryDate.setText(order.getOrderDate());
        holder.location.setText(order.getDeliveryAddress());


        if (order.getOrderType().equals("Regular")) {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.regular_order_bg));
        } else {
            holder.itemView.setBackgroundColor(holder.itemView.getContext().getResources().getColor(R.color.custom_order_bg));
        }

        String imageUrl = orderItems.get(0).getImageUrl();
        holder.customerName.setText(orderItems.get(0).getItemTitle());
        Glide.with(holder.cakeImage.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.cakesample2)
                .error(R.drawable.cakesample2)
                .into(holder.cakeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), B_OrderDetailActivity.class);
                intent.putExtra("order_id", order.getOrderId());
                intent.putExtra("order_type", order.getOrderType());
                intent.putExtra("order_date", order.getOrderDate());
                intent.putExtra("order_status", order.getOrderItems().get(0).getStatus());
                intent.putExtra("customer_name", order.getCustomerName());
                intent.putExtra("cake_type", order.getCakeType());
                intent.putExtra("delivery_address", order.getDeliveryAddress());
                intent.putExtra("image_resource", order.getOrderItems().get(0).getImageUrl());
                holder.itemView.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public ImageView cakeImage;
        public TextView customerName;
        public TextView cakeType;
        public TextView deliveryDate;
        public TextView location;
        public TextView details;

        public OrderViewHolder(View itemView) {
            super(itemView);
            cakeImage = itemView.findViewById(R.id.or_cake_image);
            customerName = itemView.findViewById(R.id.or_customer_name);
            cakeType = itemView.findViewById(R.id.or_cake_type);
            deliveryDate = itemView.findViewById(R.id.or_delivery_date);
            location = itemView.findViewById(R.id.or_location);
        }
    }
}

