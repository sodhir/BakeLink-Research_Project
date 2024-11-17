package com.example.bakelink.customers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CakeAddToCartBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_CAKE_ID = "cakeId";
    private static final String ARG_CAKE_NAME = "cakeName";
    private static final String ARG_CAKE_DESC = "cakeDescription";
    private static final String ARG_CAKE_PRICE = "cakePrice";
    private static final String ARG_CAKE_IMAGE = "cakeImageUrl";
    String formattedDate;

    public static CakeAddToCartBottomSheet newInstance(String id,String name, String description, double price, String imageUrl) {
        CakeAddToCartBottomSheet fragment = new CakeAddToCartBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CAKE_ID, id);
        args.putString(ARG_CAKE_NAME, name);
        args.putString(ARG_CAKE_DESC, description);
        args.putDouble(ARG_CAKE_PRICE, price);
        args.putString(ARG_CAKE_IMAGE, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add_to_cart, container, false);

        ImageView cakeImageView = view.findViewById(R.id.cakeImageView);
        TextView cakeNameTextView = view.findViewById(R.id.cakeNameTextView);
        TextView cakeDescriptionTextView = view.findViewById(R.id.cakeDescriptionTextView);
        TextView cakePriceTextView = view.findViewById(R.id.cakePriceTextView);
        EditText dateOfDeliveryEditText = view.findViewById(R.id.edDateOfDelivery);
        Button addToCartButton = view.findViewById(R.id.addToCartButton);

        if (getArguments() != null) {
            String name = getArguments().getString(ARG_CAKE_NAME);
            String description = getArguments().getString(ARG_CAKE_DESC);
            double price = getArguments().getDouble(ARG_CAKE_PRICE);
            String imageUrl = getArguments().getString(ARG_CAKE_IMAGE);

            cakeNameTextView.setText(name);
            cakeDescriptionTextView.setText(description);
            cakePriceTextView.setText(String.format("Price: $%.2f", price));

            Glide.with(requireContext()).load(imageUrl).into(cakeImageView);

            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String dateInput = dateOfDeliveryEditText.getText().toString().trim();


            addToCartButton.setOnClickListener(v -> {
                if (!dateInput.isEmpty()) {
                    formattedDate = df.format(dateInput);
                } else {
                    // Provide a fallback date if needed
                    formattedDate = df.format(new Date());
                }
                getBakerId(getArguments().getString(ARG_CAKE_ID));

                // Handle adding the item to the cart
                // You can pass back data to the activity using an interface if needed
                dismiss(); // Close the bottom sheet after adding
            });
        }

        return view;
    }

    public void getBakerId(String cakeId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers");
        String targetCakeId = cakeId; // Replace with the actual cake ID you're searching for

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot bakerSnapshot : snapshot.getChildren()) {
                    String bakerId = bakerSnapshot.getKey(); // Get baker ID
                    DataSnapshot cakesSnapshot = bakerSnapshot.child("cakes");

                    if (cakesSnapshot.hasChild(targetCakeId)) {
                        Log.d("Baker ID", "Baker ID found: " + bakerId);
                        // Do something with the bakerId (e.g., store it, pass it to a method, etc.)
                        found = true;
                        addCakeToCart(bakerId, cakeId);
                        break; // Stop searching once found
                    }
                }

                if (!found) {
                    Log.d("Baker ID", "Cake ID not found under any baker");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase Error", "Error fetching data: " + error.getMessage());
            }
        });
    }

    private void addCakeToCart(String bakerId, String cakeId) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers").child(bakerId).child("calendar").child(formattedDate).child("orders");

        Map<String, Object> orderData = new HashMap<>();
        orderData.put("orderId", databaseReference.push().getKey());
        orderData.put("cakeId", cakeId);

        databaseReference.push().setValue(orderData);
        //orderData.put("quantity", cakeItem.getQuantity());
        //orderData.put("price", cakeItem.getPrice());
    }
}