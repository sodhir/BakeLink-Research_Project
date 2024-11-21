package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class B_SubmittedQuotesDetailsActivity extends AppCompatActivity {
    private TextView cakeTypeText, cakeSizeText, cakeLayersText, cakeWeightText, cakeFlavorText, cakeFillingText, additionalNotesText, caketotalPrice;
    private String quoteId, responseId;
    private TextView cakeTypePrice, cakeSizePrice, cakeLayersPrice, cakeWeightPrice, cakeFlavorPrice, cakeFillingPrice, additionalNotesPrice, deliveryChargesPrice, discountsPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bsubmitted_quotes_details);
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

        // Get references to UI elements
        cakeTypeText = findViewById(R.id.qcakeType);
        cakeSizeText = findViewById(R.id.qcakeSize);
        cakeLayersText = findViewById(R.id.qcakeLayer);
        cakeWeightText = findViewById(R.id.qcakeWeight);
        cakeFlavorText = findViewById(R.id.qcakeFlavor);
        cakeFillingText = findViewById(R.id.qcakeFilling);
        additionalNotesText = findViewById(R.id.qcakenotes);

        cakeTypePrice = findViewById(R.id.qcakeTypePrice);
        cakeSizePrice = findViewById(R.id.qcakeSizePrice);
        cakeLayersPrice = findViewById(R.id.qcakeLayerPrice);
        cakeWeightPrice = findViewById(R.id.qcakeWeightPrice);
        cakeFlavorPrice = findViewById(R.id.qcakeFlavorPrice);
        cakeFillingPrice = findViewById(R.id.qcakeFillingPrice);
        additionalNotesPrice = findViewById(R.id.qcakenotesPrice);
        deliveryChargesPrice = findViewById(R.id.qcakedeliveryPrice);
        discountsPrice = findViewById(R.id.qcakediscountPrice);
        caketotalPrice = findViewById(R.id.qcakeTotalPrice);

        // Get the quoteId from the intent
        quoteId = getIntent().getStringExtra("quoteId");
        responseId = getIntent().getStringExtra("responseId");

        // Fetch data from Firebase
        fetchDataFromFirebase(quoteId,responseId);

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.none);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_SubmittedQuotesDetailsActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_SubmittedQuotesDetailsActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_SubmittedQuotesDetailsActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_SubmittedQuotesDetailsActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });
    }
    private void fetchDataFromFirebase(String quoteId, String responseId) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("customCakeRequests");

        database.child(quoteId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("FirebaseResponseData", dataSnapshot.toString());
                // Get data from snapshot
                String cakeType = dataSnapshot.child("cakeType").getValue(String.class);
                //Log.d("FirebaseResponseData", "caketype:" + cakeType);
                String cakeSize = dataSnapshot.child("cakeSize").getValue(String.class);
                String cakeLayers = dataSnapshot.child("noOfLayers").getValue(String.class);
                String cakeWeight = dataSnapshot.child("cakeWeight").getValue(String.class);
                String cakeFlavor = dataSnapshot.child("flavor").getValue(String.class);
                String cakeFilling = dataSnapshot.child("filling").getValue(String.class);
                String additionalNotes = dataSnapshot.child("notes").getValue(String.class);
                String deliveryAddress = dataSnapshot.child("deliveryAddress").getValue(String.class);
                String deliveryDate = dataSnapshot.child("deliveryDate").getValue(String.class);
                String deliveryTime = dataSnapshot.child("deliveryTime").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                //Log.d("FirebaseDebug", "quoteId: " + quoteId);
                //Log.d("FirebaseDebug", "responseId: " + responseId);
                // Now fetch price details from the responses node using responseId
                database.child(quoteId).child("responses").child(responseId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot responseSnapshot) {
                                //Log.d("FirebaseResponseData", responseSnapshot.toString());
//                                if (responseSnapshot.exists()) {
//                                    Log.d("FirebaseResponseData", "Data: " + responseSnapshot.getValue());
//                                } else {
//                                    Log.d("FirebaseResponseData", "Response snapshot does not exist.");
//                                }
                                // Retrieve price data from the responses node
                                double cakeTypePriceVal = responseSnapshot.child("cakeTypePrice").exists()? responseSnapshot.child("cakeTypePrice").getValue(Double.class) : 0.0;
                                double cakeSizePriceVal = responseSnapshot.child("cakeSizePrice").getValue(Double.class);
                                double cakeLayersPriceVal = responseSnapshot.child("cakeLayersPrice").getValue(Double.class);
                                double cakeWeightPriceVal = responseSnapshot.child("cakeWeightPrice").getValue(Double.class);
                                double cakeFlavorPriceVal = responseSnapshot.child("cakeFlavorPrice").getValue(Double.class);
                                double cakeFillingPriceVal = responseSnapshot.child("cakeFillingPrice").getValue(Double.class);
                                double cakeadditionalNotesPrice = responseSnapshot.child("additionalNotesPrice").getValue(Double.class);
                                double deliveryChargesPriceVal = responseSnapshot.child("deliveryChargesPrice").getValue(Double.class);
                                double discountsPriceVal = responseSnapshot.child("discountsPrice").getValue(Double.class);
                                double cakeTotal = responseSnapshot.child("quotedPrice").getValue(Double.class);

                                // Update UI with the fetched data from customcakeRequests node
                                cakeTypeText.setText("Cake type: " + cakeType);
                                cakeSizeText.setText("Cake size: " + cakeSize);
                                cakeLayersText.setText("No. of layers: " + cakeLayers);
                                cakeWeightText.setText("Cake weight: " + cakeWeight);
                                cakeFlavorText.setText("Cake flavor: " + cakeFlavor);
                                cakeFillingText.setText("Cake filling: " + cakeFilling);
                                additionalNotesText.setText("Additional notes: " + additionalNotes);

                                // Update price fields with the fetched prices from responses node
                                cakeTypePrice.setText(String.format("%.2f", cakeTypePriceVal));
                                cakeSizePrice.setText(String.format("%.2f", cakeSizePriceVal));
                                cakeLayersPrice.setText(String.format("%.2f", cakeLayersPriceVal));
                                cakeWeightPrice.setText(String.format("%.2f", cakeWeightPriceVal));
                                cakeFlavorPrice.setText(String.format("%.2f", cakeFlavorPriceVal));
                                cakeFillingPrice.setText(String.format("%.2f", cakeFillingPriceVal));
                                additionalNotesPrice.setText(String.format("%.2f", cakeadditionalNotesPrice));
                                deliveryChargesPrice.setText(String.format("%.2f", deliveryChargesPriceVal));
                                discountsPrice.setText(String.format("%.2f", discountsPriceVal));
                                caketotalPrice.setText(String.format("%.2f", cakeTotal));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                // Handle potential errors in the responses node fetch
                            }
                        });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle potential errors in the customcakeRequests fetch
            }
        });
    }

}