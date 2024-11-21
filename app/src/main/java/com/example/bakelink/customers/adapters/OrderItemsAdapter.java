package com.example.bakelink.customers.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.bakers.models.OrderItem;
import com.example.bakelink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder> {
    private final List<OrderItem> orderItems;
    private String currentUserId;

    public OrderItemsAdapter(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_items, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        // Log.d("OrderItem", "OrderItem: " + orderItem.getOrderItemId());
        //holder.itemImage.setImageResource(0);
        holder.itemTitle.setText(orderItem.getItemTitle());
        holder.itemFlavor.setText("Flavor: " + orderItem.getFlavor());
        holder.itemWeight.setText("Weight: " + orderItem.getWeight());
        holder.itemPrice.setText("Price: $" + orderItem.getPrice());
        holder.itemQuantity.setText(String.valueOf(orderItem.getQuantity()));

        Glide.with(holder.itemImage.getContext())
                .load(orderItem.getImageUrl())
                .into(holder.itemImage);

        // Handle increase/decrease and delete actions
        holder.buttonIncrease.setOnClickListener(v -> {
            int newQuantity = orderItem.getQuantity() + 1;
            orderItem.setQuantity(newQuantity);
            notifyItemChanged(position);
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            if (orderItem.getQuantity() > 1) {
                int newQuantity = orderItem.getQuantity() - 1;
                orderItem.setQuantity(newQuantity);
                notifyItemChanged(position);
            }
        });

        holder.buttonDelete.setOnClickListener(v -> {
            orderItems.remove(position);
            notifyItemRemoved(position);
            deleteOrderItem(orderItem.getOrderItemId());
            //notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle, itemFlavor, itemWeight, itemPrice, itemQuantity;
        TextView buttonDecrease, buttonIncrease;
        ImageButton buttonDelete;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_image);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemFlavor = itemView.findViewById(R.id.item_flavor);
            itemWeight = itemView.findViewById(R.id.item_weight);
            itemPrice = itemView.findViewById(R.id.item_price);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            buttonDecrease = itemView.findViewById(R.id.button_decrease);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }

    public void deleteOrderItem(String orderItemId) {
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("userCarts").child(currentUserId).child("temporaryCartItems").child(orderItemId);
        dbRef.removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Firebase", "Record deleted successfully");
            } else {
                Log.e("Firebase", "Failed to delete record", task.getException());
            }
        });

    }
}
