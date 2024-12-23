package com.example.bakelink.customers;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.bakelink.bakers.models.Cake;
import com.example.bakelink.bakers.models.OrderItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CakeAddToCartBottomSheet extends BottomSheetDialogFragment {
    private static final String ARG_CAKE_ID = "cakeId";
    private static final String ARG_CAKE_NAME = "cakeName";
    private static final String ARG_CAKE_DESC = "cakeDescription";
    private static final String ARG_CAKE_PRICE = "cakePrice";
    private static final String ARG_CAKE_IMAGE = "cakeImageUrl";

    private static final String ARG_CAKE_WEIGHT = "cakeWeight";
    private static final String ARG_CAKE_FLAVOR = "cakeFlavor";
    private static final String ARG_CAKE_FILLING = "cakeFilling";
    private static final String ARG_CAKE_SIZE = "cakeSize";



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

    public static CakeAddToCartBottomSheet newInstance(String id,String name, String description, double price, String imageUrl, String weight) {
        CakeAddToCartBottomSheet fragment = new CakeAddToCartBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CAKE_ID, id);
        args.putString(ARG_CAKE_NAME, name);
        args.putString(ARG_CAKE_DESC, description);
        args.putDouble(ARG_CAKE_PRICE, price);
        args.putString(ARG_CAKE_IMAGE, imageUrl);
        args.putString(ARG_CAKE_WEIGHT, weight);
        fragment.setArguments(args);
        return fragment;
    }

    public static CakeAddToCartBottomSheet newInstance(String id,String name, String description, double price, String imageUrl, String weight, String flavor, String filling) {
        CakeAddToCartBottomSheet fragment = new CakeAddToCartBottomSheet();
        Bundle args = new Bundle();
        args.putString(ARG_CAKE_ID, id);
        args.putString(ARG_CAKE_NAME, name);
        args.putString(ARG_CAKE_DESC, description);
        args.putDouble(ARG_CAKE_PRICE, price);
        args.putString(ARG_CAKE_IMAGE, imageUrl);
        args.putString(ARG_CAKE_WEIGHT, weight);
        args.putString(ARG_CAKE_FILLING, flavor);
        args.putString(ARG_CAKE_FLAVOR, filling);
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
        //EditText dateOfDeliveryEditText = view.findViewById(R.id.edDateOfDelivery);
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


            addToCartButton.setOnClickListener(v -> {

                getBakerId(getArguments().getString(ARG_CAKE_ID));

                dismiss();

                Intent intent = new Intent(getContext(), C_CartActivity.class);
                startActivity(intent);
            });
        }

        return view;
    }

    public void getBakerId(String cakeId) {
        Log.d("addtocart","cakeid:"+cakeId);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers");
        String targetCakeId = cakeId;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot bakerSnapshot : snapshot.getChildren()) {
                    String bakerId = bakerSnapshot.getKey();
                    DataSnapshot cakesSnapshot = bakerSnapshot.child("cakes");

                    if (cakesSnapshot.hasChild(targetCakeId)) {
                        Log.d("Baker ID", "Baker ID found: " + bakerId);

                        found = true;
                        addCakeToCart(bakerId, cakeId);
                        break;
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

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userCarts").child(currentUserId).child("temporaryCartItems");

        Map<String, Object> orderData = new HashMap<>();

        orderData.put("cakeId", cakeId);
        orderData.put("bakerId", bakerId);
        orderData.put("deliveryDate", formattedDate);
        orderData.put("orderType", "Regular");
        orderData.put("status", "Pending");

        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(databaseReference.push().getKey());
        orderItem.setCakeId(cakeId);
        orderItem.setOrderType("Regular");
        orderItem.setStatus("Pending");
        orderItem.setItemTitle(getArguments().getString(ARG_CAKE_NAME));
        orderItem.setFlavor(getArguments().getString(ARG_CAKE_FLAVOR));
        orderItem.setWeight(getArguments().getString(ARG_CAKE_WEIGHT));
        orderItem.setQuantity(1);
        orderItem.setPrice(getArguments().getDouble(ARG_CAKE_PRICE));
        orderItem.setImageUrl(getArguments().getString(ARG_CAKE_IMAGE));
        orderItem.setCakeFilling(getArguments().getString(ARG_CAKE_FILLING));
        orderItem.setCakeLayers("N/A");
        orderItem.setCakeSize(getArguments().getString(ARG_CAKE_SIZE));
        orderItem.setAdditionalNotes("N/A");
        orderItem.setBakerId(bakerId);


        databaseReference.push().setValue(orderItem);

    }
}
