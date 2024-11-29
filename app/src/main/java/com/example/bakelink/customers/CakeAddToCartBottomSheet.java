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

            // Open date picker on click of the date EditText
            dateOfDeliveryEditText.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                        (view1, selectedYear, selectedMonth, selectedDay) -> {
                            // Format the selected date and display it
                            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                            Calendar selectedDate = Calendar.getInstance();
                            selectedDate.set(selectedYear, selectedMonth, selectedDay);
                            formattedDate = df.format(selectedDate.getTime()); // Store formatted date
                            dateOfDeliveryEditText.setText(formattedDate); // Show the date in EditText
                        },
                        year, month, day);

                // Show the date picker dialog
                datePickerDialog.show();
            });



            addToCartButton.setOnClickListener(v -> {
                if (formattedDate.isEmpty()) {
                    // Set a fallback date if none is selected
                    formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
                }
                getBakerId(getArguments().getString(ARG_CAKE_ID));

                dismiss();
                // Redirect to the cart activity
                Intent intent = new Intent(getContext(), C_CartActivity.class); // Replace CartActivity with the actual name of your cart activity
                startActivity(intent);
            });
        }

        return view;
    }

    public void getBakerId(String cakeId) {
        Log.d("addtocart","cakeid:"+cakeId);
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

        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userCarts").child(currentUserId).child("temporaryCartItems");

        Map<String, Object> orderData = new HashMap<>();
       // orderData.put("orderId", databaseReference.push().getKey());
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
        orderItem.setFlavor(getArguments().getString(ARG_CAKE_DESC));
        orderItem.setWeight(getArguments().getString(ARG_CAKE_PRICE));
        orderItem.setQuantity(1);
        orderItem.setPrice(getArguments().getDouble(ARG_CAKE_PRICE));
        orderItem.setImageUrl(getArguments().getString(ARG_CAKE_IMAGE));
        orderItem.setBakerId(bakerId);


        databaseReference.push().setValue(orderItem);

    }
}
