package com.example.bakelink.bakers;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.BasePrice;
import com.example.bakelink.bakers.models.CakeWeightAndPrice;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class B_GenerateQuoteActivity extends AppCompatActivity {

    private TextView qTitle, basePriceTitle, weightPriceTitle;
    private LinearLayout basePriceList, weightPriceList;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String userId;
    private String currentCustomCakeRequestId;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bgenerate_quote);

        qTitle = findViewById(R.id.qTitle);
        basePriceTitle = findViewById(R.id.basePriceTitle);
        weightPriceTitle = findViewById(R.id.weightPriceTitle);
        basePriceList = findViewById(R.id.basePriceList);
        weightPriceList = findViewById(R.id.weightPriceList);
        currentCustomCakeRequestId = getIntent().getStringExtra("customCakeRequestId");

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Get the current user's ID
        userId = mAuth.getCurrentUser().getUid();

        // Fetch quote defaults from Firebase
        fetchQuoteDefaults();


        // Send Quote Button
        findViewById(R.id.btnSendQuote).setOnClickListener(view -> {
            sendQuote();
        });

    }

    private void fetchQuoteDefaults() {

        mDatabase.child("bakers").child(userId).child("quoteDefaults").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Check if data exists
                if (dataSnapshot.exists()) {
                    // Retrieve the base price per cake and weight and price
                    List<BasePrice> basePriceListData = new ArrayList<>();
                    List<CakeWeightAndPrice> weightPriceListData = new ArrayList<>();

                    // Loop through the basePricePerCake data
                    for (DataSnapshot priceSnapshot : dataSnapshot.child("basePricePerCake").getChildren()) {
                        String cakeType = priceSnapshot.child("cakeType").getValue(String.class);
                        int price = priceSnapshot.child("price").getValue(Integer.class);
                        basePriceListData.add(new BasePrice(cakeType, price));
                    }

                    // Loop through the cakeWeightAndPrice data
                    for (DataSnapshot weightSnapshot : dataSnapshot.child("cakeWeightAndPrice").getChildren()) {
                        String weight = weightSnapshot.child("weight").getValue(String.class);
                        int price = weightSnapshot.child("price").getValue(Integer.class);
                        weightPriceListData.add(new CakeWeightAndPrice(weight, price));
                    }

                    // Display the data on the UI
                    displayBasePriceList(basePriceListData);
                    displayWeightPriceList(weightPriceListData);
                } else {
                    // Handle case where no data exists
                    Toast.makeText(B_GenerateQuoteActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Toast.makeText(B_GenerateQuoteActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayBasePriceList(List<BasePrice> basePriceListData) {
        for (BasePrice basePrice : basePriceListData) {
            TextView textView = new TextView(this);
            textView.setText(basePrice.getCakeType() + ": $" + basePrice.getPrice());
            basePriceList.addView(textView);
        }
    }

    private void displayWeightPriceList(List<CakeWeightAndPrice> weightPriceListData) {
        for (CakeWeightAndPrice weightPrice : weightPriceListData) {
            TextView textView = new TextView(this);
            textView.setText(weightPrice.getWeight() + ": $" + weightPrice.getPrice());
            weightPriceList.addView(textView);
        }
    }

    private void sendQuote() {
        // Get the quoted price from the EditText
        EditText quotedPriceEditText = findViewById(R.id.edSubtotal);
        String quotedPriceString = quotedPriceEditText.getText().toString();

        if (quotedPriceString.isEmpty()) {
            Toast.makeText(this, "Please enter a quoted price", Toast.LENGTH_SHORT).show();
            return; // Exit if no price is provided
        }

        // Convert quoted price to integer
        double quotedPrice = Double.parseDouble(quotedPriceString);

        // Create a new QuoteResponse object
        QuoteResponse quoteResponse = new QuoteResponse(userId, quotedPrice, "", currentCustomCakeRequestId);


        // Generate a unique response ID using push
        String responseID = mDatabase.child("customCakeRequests").child(currentCustomCakeRequestId).child("responses").push().getKey();

        // Check if responseID is null (in case of any issue with generating key)
        if (responseID == null) {
            Toast.makeText(this, "Failed to generate a response ID", Toast.LENGTH_SHORT).show();
            return;
        }

        quoteResponse.setStatus("Responded");

        // Save the response under the quote's responses node
        mDatabase.child("customCakeRequests").child(currentCustomCakeRequestId).child("responses").child(responseID).setValue(quoteResponse)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Save the response ID under baker's profile to track their responses
                        mDatabase.child("bakers").child(userId).child("quoteResponses").child(responseID).setValue(quoteResponse)
                                .addOnCompleteListener(bakerTask -> {
                                    if (bakerTask.isSuccessful()) {
                                        // Both response saved under request and baker's profile
                                        Toast.makeText(this, "Response submitted successfully", Toast.LENGTH_SHORT).show();
                                        finish(); // Close the activity or return to the previous screen
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