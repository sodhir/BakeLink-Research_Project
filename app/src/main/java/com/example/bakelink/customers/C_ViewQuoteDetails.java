package com.example.bakelink.customers;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bakelink.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class C_ViewQuoteDetails extends AppCompatActivity {

    TextView txtrespondedId, txtrespondedbaker, txtrespondedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cview_quote_details);

        txtrespondedId = findViewById(R.id.txtrespondedId);
        txtrespondedbaker = findViewById(R.id.txtrespondedbaker);
        txtrespondedPrice = findViewById(R.id.txtrespondedPrice);

        String responseId = getIntent().getStringExtra("responseId");
        String customCakeRequestId = getIntent().getStringExtra("customCakeRequestId");

        loadQuoteDetails(responseId, customCakeRequestId);
    }

    private void loadQuoteDetails(String responseId, String customCakeRequestId) {

        // Fetch and display quote details based on quoteId
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customCakeRequestId).child("reponses").child(responseId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String responseId = snapshot.getKey();
                String bakerId = snapshot.child("userID").getValue(String.class);
                Double price = snapshot.child("quotedPrice").getValue(Double.class);

                txtrespondedId.setText(responseId);
                txtrespondedbaker.setText(bakerId);
                txtrespondedPrice.setText(price.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}