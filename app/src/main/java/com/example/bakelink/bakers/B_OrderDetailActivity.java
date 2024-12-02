package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class B_OrderDetailActivity extends AppCompatActivity {
    Spinner orderStatusSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_border_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        String bakeryName = sharedPreferences.getString("bakery_name", null); // Get bakery name
        welcomeText.setText("Welcome back, " + bakeryName + "!");

        orderStatusSpinner = findViewById(R.id.order_status_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Pending", "Completed", "In Progress"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderStatusSpinner.setAdapter(adapter);

        String orderId = getIntent().getStringExtra("order_id");
        String orderDate = getIntent().getStringExtra("order_date");


        loadOrderDetails(orderDate, orderId);

        Button updateButton = findViewById(R.id.btnUpdateOrder);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOdersStatus(orderId, orderDate);
            }
        });


        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_OrderDetailActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }

    private void updateOdersStatus(String orderId, String orderDate) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference("bakers").child(currentUserId).child("calendar").child(orderDate).child("orders").child(orderId);

        orderref.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String orderStatus = dataSnapshot.child("orderStatus").getValue(String.class);
                String newStatus = orderStatusSpinner.getSelectedItem().toString();
                 for (DataSnapshot orderItemSnapshot : dataSnapshot.child("orderItems").getChildren()) {
                        orderItemSnapshot.getRef().child("status").setValue(newStatus);
                     }
                 }
        });

        Intent intent = new Intent(B_OrderDetailActivity.this, B_MyScheduleActivity.class);
        startActivity(intent);

    }

    private void loadOrderDetails(String orderDate, String orderId) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference orderref = FirebaseDatabase.getInstance().getReference("bakers").child(currentUserId).child("calendar").child(orderDate).child("orders").child(orderId);
        orderref.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                String orderType = dataSnapshot.child("orderType").getValue(String.class);
                String orderStatus = dataSnapshot.child("orderStatus").getValue(String.class);
                String imageUrl = "";


                for (DataSnapshot orderItemSnapshot : dataSnapshot.child("orderItems").getChildren()) {
                    Log.d("orderDetails", "Order Item Snapshot: " + orderItemSnapshot.toString());
                    imageUrl = orderItemSnapshot.child("imageUrl").getValue(String.class);
                    String cakeType = orderItemSnapshot.child("orderType").getValue(String.class);
                    String cakeSize = orderItemSnapshot.child("cakeSize").getValue(String.class);
                    String cakeLayers = orderItemSnapshot.child("cakeLayers").getValue(String.class);
                    String cakeWeight = orderItemSnapshot.child("cakeWeight").getValue(String.class);
                    String cakeFlavor = orderItemSnapshot.child("cakeFlavor").getValue(String.class);
                    String cakeFilling = orderItemSnapshot.child("cakeFilling").getValue(String.class);
                    String additionalNotes = orderItemSnapshot.child("additionalNotes").getValue(String.class);
                    Double price = orderItemSnapshot.child("price").getValue(Double.class);
                    String orderStat = orderItemSnapshot.child("status").exists()? orderItemSnapshot.child("status").getValue(String.class): "Pending";

                    orderStatusSpinner.setSelection(orderStat.equals("Pending") ? 0 : orderStat.equals("Completed") ? 1 : 2);

                    ImageView cakeImage = findViewById(R.id.cakeImage);
                    Glide.with(cakeImage.getContext()).load(imageUrl).into(cakeImage);
                    TextView orderTypeTextView = findViewById(R.id.vqCakeType);
                    orderTypeTextView.setText(cakeType);
                    TextView orderSizeTextView = findViewById(R.id.vqCakeSize);
                    if (cakeSize == null || cakeSize.trim().isEmpty()) {
                        orderSizeTextView.setText("N/A");
                    } else {
                        orderSizeTextView.setText(cakeSize);
                    }
                    TextView orderLayersTextView = findViewById(R.id.vqCakeLayers);
                    if (cakeLayers == null || cakeLayers.trim().isEmpty()) {
                        orderLayersTextView.setText("N/A");
                    } else {
                        orderLayersTextView.setText(cakeLayers);
                    }

                    TextView cakeFlaovorTextView = findViewById(R.id.vqCakeFlavor);
                    if (cakeFlavor == null || cakeFlavor.trim().isEmpty()) {
                        cakeFlaovorTextView.setText("N/A");
                    } else {
                        cakeFlaovorTextView.setText(cakeFlavor);
                    }
                    TextView cakeFillingTextView = findViewById(R.id.vqCakeFilling);
                    if (cakeFilling == null || cakeFilling.trim().isEmpty()) {
                        cakeFillingTextView.setText("N/A");
                    } else {
                        cakeFillingTextView.setText(cakeFilling);
                    }
                    TextView additionalNotesTextView = findViewById(R.id.vqAdditionalNotes);
                    if (additionalNotes == null || additionalNotes.trim().isEmpty()) {
                        additionalNotesTextView.setText("N/A");
                    } else {
                        additionalNotesTextView.setText(additionalNotes);
                    }
                    TextView priceTextView = findViewById(R.id.vqPrice);
                    priceTextView.setText(String.valueOf(price));



                }

                TextView orderDeliverDate = findViewById(R.id.vqDeliveryDate);
                orderDeliverDate.setText(orderDate);
                String deliveryAddress = dataSnapshot.child("deliveryAddress").getValue(String.class);
                TextView deliveryAddresstxt = findViewById(R.id.vqDeliveryAddress);
                deliveryAddresstxt.setText(deliveryAddress);

                     }else {
                Log.d("OrderDetailActivity", "Order data does not exist");
                    }
                }
            );
    }
}
