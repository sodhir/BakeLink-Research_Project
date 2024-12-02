package com.example.bakelink.bakers.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.example.bakelink.bakers.interfaces.BakeryTitleCallBack;
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.bakers.models.RecommendationCake;
import com.example.bakelink.customers.CakeAddToCartBottomSheet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {

    List<RecommendationCake> recommendationCakes;

    String bakeryTitle = "";

    public RecommendationAdapter(List<RecommendationCake> recommendationCakes) {
        this.recommendationCakes = recommendationCakes;
    }

    public RecommendationAdapter() {
    }

    public List<RecommendationCake> getRecommendationCakes() {
        return recommendationCakes;
    }

    public void setRecommendationCakes(List<RecommendationCake> recommendationCakes) {
        this.recommendationCakes = recommendationCakes;
    }

    @NonNull
    @Override
    public RecommendationAdapter.RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recommendation, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationAdapter.RecommendationViewHolder holder, int position) {
        RecommendationCake recommendationCake = recommendationCakes.get(position);
        String imageUrl = recommendationCake.getImageUrl();
        String cakeName = recommendationCake.getCake().getCakeName();
        Cake cake = recommendationCake.getCake();
        //String bakerName = getBakeryTitle(recommendationCake.getBakerTitle());
//        String bakerId;
//        if(!recommendationCake.getCakeType().isEmpty()){
//            if(recommendationCake.getCakeType().equals("Regular")){
//
//            }else{
//                bakerId = recommendationCake.getBakerTitle();
//                getBakeryTitle(bakerId, bakeryTitle -> {
//                    holder.rBakerName.setText(bakeryTitle.isEmpty() ? "Test Baker" : bakeryTitle);
//                });
//            }
//
//        }else{
//            bakerId = recommendationCake.getBakerTitle();
//            getBakeryTitle(bakerId, bakeryTitle -> {
//                holder.rBakerName.setText(bakeryTitle.isEmpty() ? "Test Baker" : bakeryTitle);
//            });
//        }

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .into(holder.rImage);

        holder.rBakerName.setText(cakeName);

        holder.itemView.setOnClickListener(v -> {
            CakeAddToCartBottomSheet bottomSheet = CakeAddToCartBottomSheet.newInstance(
                    cake.getCakeId(),
                    cake.getCakeName(),
                    cake.getDescription(),
                    cake.getPrice(),
                    cake.getCakeImgUrl()
            );
            bottomSheet.show(((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager(), "CakeAddToCartBottomSheet");
        });

    }

    @Override
    public int getItemCount() {
        return recommendationCakes.size();
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {

        ImageView rImage;
        TextView rBakerName;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);

            rImage = itemView.findViewById(R.id.rImage);
            rBakerName = itemView.findViewById(R.id.rBakerName);
        }
    }

    private void getBakeryTitle(String bakerId, BakeryTitleCallBack callback) {
        if (bakerId == null || bakerId.isEmpty()) {
            callback.onBakeryTitleFetched(""); // Return empty if bakerId is invalid
            return;
        }

        DatabaseReference bakersRef = FirebaseDatabase.getInstance().getReference("bakers").child(bakerId);

        bakersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("bakeryTitle")) {
                    String bakeryTitle = snapshot.child("bakeryTitle").getValue(String.class);
                    callback.onBakeryTitleFetched(bakeryTitle);
                } else {
                    callback.onBakeryTitleFetched("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to fetch bakery title: " + error.getMessage());
                callback.onBakeryTitleFetched("");
            }
        });
    }

}
