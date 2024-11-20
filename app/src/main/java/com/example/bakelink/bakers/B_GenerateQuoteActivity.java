package com.example.bakelink.bakers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.BasePrice;
import com.example.bakelink.bakers.models.CakeWeightAndPrice;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class B_GenerateQuoteActivity extends AppCompatActivity {

    private TextView cakeTypeText, cakeSizeText, cakeLayersText, cakeWeightText, cakeFlavorText, cakeFillingText, additionalNotesText, caketotalPrice;

    private EditText cakeTypePrice, cakeSizePrice, cakeLayersPrice, cakeWeightPrice, cakeFlavorPrice, cakeFillingPrice, additionalNotesPrice, deliveryChargesPrice, discountsPrice;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;
    private String currentCustomCakeRequestId;

    String customerId, imageUrl, cakeTypeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bgenerate_quote);

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


        // Retrieve data from Intent
        Intent intent = getIntent();

        String cakeType = intent.getStringExtra("cakeType");
        cakeTypeName = cakeType;
        String cakeSize = intent.getStringExtra("cakeSize");
        String cakeLayers = intent.getStringExtra("cakeLayers");
        String cakeWeight = intent.getStringExtra("cakeWeight");
        String cakeFlavor = intent.getStringExtra("cakeFlavor");
        String cakeFilling = intent.getStringExtra("cakeFilling");
        String additionalNotes = intent.getStringExtra("additionalNotes");
        customerId = intent.getStringExtra("customerId");
        imageUrl = intent.getStringExtra("imageUrl");

        // Populate the fields
        cakeTypeText.setText("Cake type: " + cakeType);
        cakeSizeText.setText("Cake size: " + cakeSize);
        cakeLayersText.setText("No. of layers: " + cakeLayers);
        cakeWeightText.setText("Cake weight: " + cakeWeight);
        cakeFlavorText.setText("Cake flavor: " + cakeFlavor);
        cakeFillingText.setText("Cake filling: " + cakeFilling);
        additionalNotesText.setText("Additional notes: " + additionalNotes);

        currentCustomCakeRequestId = getIntent().getStringExtra("customCakeRequestId");

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the current user's ID
        userId = mAuth.getCurrentUser().getUid();

        String bakerId = intent.getStringExtra("bakerId");
        //Log.d("BakerId", "Baker ID: " + bakerId);
        String selectedCakeType = intent.getStringExtra("cakeType");
        String selectedCakeWeight = intent.getStringExtra("cakeWeight");

        // Fetch quoteDefaults for the baker
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("bakers")
                .child(bakerId).child("quoteDefaults");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch and populate basePricePerCake
                    DataSnapshot basePriceSnapshot = snapshot.child("basePricePerCake");
                    for (DataSnapshot priceSnapshot : basePriceSnapshot.getChildren()) {
                        String cakeType = priceSnapshot.child("cakeType").getValue(String.class);
                        String price = priceSnapshot.child("price").getValue(String.class);

                        if (cakeType != null && cakeType.equals(selectedCakeType)) {
                            cakeTypePrice.setText(price); // Populate cakeTypePrice EditText
                            break;
                        }
                    }

                    // Fetch and populate cakeWeightAndPrice
                    DataSnapshot weightPriceSnapshot = snapshot.child("cakeWeightAndPrice");
                    for (DataSnapshot weightSnapshot : weightPriceSnapshot.getChildren()) {
                        String weight = weightSnapshot.child("weight").getValue(String.class);
                        String priceExtra = weightSnapshot.child("priceExtra").getValue(String.class);

                        if (weight != null && weight.equals(selectedCakeWeight)) {
                            cakeWeightPrice.setText(priceExtra); // Populate cakeWeightPrice EditText
                            break;
                        }
                    }
                } else {
                    // No quoteDefaults found
                    Toast.makeText(B_GenerateQuoteActivity.this, "No quote defaults found for this baker.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(B_GenerateQuoteActivity.this, "Failed to fetch quote defaults.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set TextWatchers to dynamically update total price
        cakeTypePrice.addTextChangedListener(new PriceTextWatcher());
        cakeSizePrice.addTextChangedListener(new PriceTextWatcher());
        cakeLayersPrice.addTextChangedListener(new PriceTextWatcher());
        cakeWeightPrice.addTextChangedListener(new PriceTextWatcher());
        cakeFlavorPrice.addTextChangedListener(new PriceTextWatcher());
        cakeFillingPrice.addTextChangedListener(new PriceTextWatcher());
        additionalNotesPrice.addTextChangedListener(new PriceTextWatcher());
        deliveryChargesPrice.addTextChangedListener(new PriceTextWatcher());
        discountsPrice.addTextChangedListener(new PriceTextWatcher());


        // Send Quote Button
        findViewById(R.id.btnSendQuote).setOnClickListener(view -> {
            sendQuote();
        });

        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(B_GenerateQuoteActivity.this, B_HomeActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_GenerateQuoteActivity.this, B_MyScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_GenerateQuoteActivity.this, B_MyAllCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_GenerateQuoteActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

    // This is the TextWatcher to listen for changes and recalculate total price
    private class PriceTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
            updateTotalPrice();
        }

        @Override
        public void afterTextChanged(Editable editable) {}
    }

    // Method to update total price based on the current values in the EditText fields
    private void updateTotalPrice() {
        double totalPrice = 0;

        // Helper method to get value from EditText (handling empty values and converting to double)
        totalPrice += getValueFromEditText(cakeTypePrice);
        totalPrice += getValueFromEditText(cakeSizePrice);
        totalPrice += getValueFromEditText(cakeLayersPrice);
        totalPrice += getValueFromEditText(cakeWeightPrice);
        totalPrice += getValueFromEditText(cakeFlavorPrice);
        totalPrice += getValueFromEditText(cakeFillingPrice);
        totalPrice += getValueFromEditText(additionalNotesPrice);
        totalPrice += getValueFromEditText(deliveryChargesPrice);
        totalPrice -= getValueFromEditText(discountsPrice);

        // Set the calculated total price to the TextView
        caketotalPrice.setText(String.format("%.2f", totalPrice));
    }

    // Helper method to get the value from EditText and return it as a double
    private double getValueFromEditText(EditText editText) {
        String value = editText.getText().toString();
        if (value.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(value);
    }


    private void sendQuote() {

        String quotedPriceString = caketotalPrice.getText().toString();
        //Log.d("generatequote",quotedPriceString);

        // Convert quoted price to double
        Double quotedPrice = Double.parseDouble(quotedPriceString);
        //Log.d("generatequote", Double.toString(quotedPrice) + "12");

        String responseID = mDatabase.child("customCakeRequests").child(currentCustomCakeRequestId).child("responses").push().getKey();
        Log.d("generatequote",responseID);
        // Create a new QuoteResponse object
        QuoteResponse quoteResponse = new QuoteResponse();
        //quoteResponse.setQuoteResponseId();
        quoteResponse.setBakerId(userId);  // Assuming `userId` is the baker's ID
        quoteResponse.setCustomCakeRequestId(currentCustomCakeRequestId);  // Custom cake request ID
        quoteResponse.setQuotedPrice(quotedPrice);
        quoteResponse.setResponseMessage("");  // Add the response message if needed
        quoteResponse.setStatus("Awaiting Approval");
        quoteResponse.setCustomerId(customerId);  // Set the customer's ID
        quoteResponse.setImageUrl(imageUrl);
        quoteResponse.setCakeType(cakeTypeName);
        //quoteResponse.setImageUrl("");  // Set image URL if available
        Log.d("generatequote",quoteResponse.toString());


         //Check if responseID is null (in case of any issue with generating key)
        if (responseID == null) {
            Toast.makeText(this, "Failed to generate a response ID", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the response under the quote's responses node
        mDatabase.child("customCakeRequests").child(currentCustomCakeRequestId).child("responses").child(responseID).setValue(quoteResponse)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Save the response ID under the baker's profile to track their responses
                        mDatabase.child("bakers").child(userId).child("quoteResponses").child(responseID).setValue(quoteResponse)
                                .addOnCompleteListener(bakerTask -> {
                                    if (bakerTask.isSuccessful()) {
                                        // Both response saved under request and baker's profile
                                        Toast.makeText(this, "Response submitted successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(B_GenerateQuoteActivity.this, B_HomeActivity.class));
                                        //finish(); // Close the activity or return to the previous screen
                                    } else {
                                        // Failed to save response under baker's profile
                                        Toast.makeText(this, "Failed to save your response under baker's profile", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Failed to save response under custom cake request
                        Toast.makeText(this, "Failed to submit response to the request", Toast.LENGTH_SHORT).show();
                    }
                });
    }





}