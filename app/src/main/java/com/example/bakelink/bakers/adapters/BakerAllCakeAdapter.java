package com.example.bakelink.bakers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.EditCakeBottomSheet;
import com.example.bakelink.bakers.models.Cake;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BakerAllCakeAdapter extends RecyclerView.Adapter<BakerAllCakeAdapter.BakerAllCakeViewHolder>{

    List<Cake> allCakeList;

    public BakerAllCakeAdapter(List<Cake> allCakeList) {
        this.allCakeList = allCakeList;
    }

    @NonNull
    @Override
    public BakerAllCakeAdapter.BakerAllCakeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_cake, parent, false);
        return new BakerAllCakeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BakerAllCakeAdapter.BakerAllCakeViewHolder holder, int position) {

        Cake cake = allCakeList.get(position);
        String imageUrl = cake.getCakeImgUrl();

        Glide.with(holder.itemView.getContext())
                .load(imageUrl) // Load from URL
                .into(holder.cakeImg);

        holder.editImage.setImageResource(R.drawable.edit);
        holder.deleteImage.setImageResource(R.drawable.delete);

        holder.editImage.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager();
            EditCakeBottomSheet bottomSheet = new EditCakeBottomSheet(cake, updatedCake -> {
                notifyDataSetChanged(); // Refresh the adapter if needed
            });
            bottomSheet.show(fragmentManager, bottomSheet.getTag());
        });

        holder.deleteImage.setOnClickListener(v -> {
            // Get reference to the cake in Firebase
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            String bakerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference cakeRef = databaseReference.child("bakers").child(bakerId).child("cakes").child(cake.getCakeId());

            // Delete the cake from Firebase
            cakeRef.removeValue().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Remove the cake from the list locally and notify the adapter
                    allCakeList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(holder.itemView.getContext(), "Cake deleted successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Failed to delete cake", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }



    @Override
    public int getItemCount() {
        return allCakeList.size();
    }

    public class BakerAllCakeViewHolder extends RecyclerView.ViewHolder {

        ImageView cakeImg;
        ImageView editImage;

        ImageView deleteImage;

        public BakerAllCakeViewHolder(@NonNull View itemView) {
            super(itemView);

            cakeImg = itemView.findViewById(R.id.allCakeImg);
            editImage = itemView.findViewById(R.id.allCakeEdit);
            deleteImage = itemView.findViewById(R.id.allCakeDelete);
        }
    }
}
