package com.example.bakelink.customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.interfaces.BakeryTitleCallBack;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.example.bakelink.customers.adapters.CakeQuoteResponseAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class C_ViewQuotesPerCakeRequestActivity extends AppCompatActivity {

    RecyclerView quoteResponsesRecyclerView;
    String customcakeRequestId;
    ImageButton cartIcon;
    CakeQuoteResponseAdapter adapter;
    ArrayList<QuoteResponse> responseList;

    String bakeryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cview_quotes_per_cake_request);

        quoteResponsesRecyclerView = findViewById(R.id.allQuoteResponsesRecycler);
        customcakeRequestId = getIntent().getStringExtra("customcakeRequestId");
        responseList = new ArrayList<>();

        loadAllResponses();
        Log.d("CustomCakeFetch", "Custom Cake Response: " + responseList.size() + customcakeRequestId );
        adapter = new CakeQuoteResponseAdapter(responseList);
        quoteResponsesRecyclerView.setAdapter(adapter);
        quoteResponsesRecyclerView.setLayoutManager(new LinearLayoutManager(C_ViewQuotesPerCakeRequestActivity.this));

        cartIcon = findViewById(R.id.cart_icon);

        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(C_ViewQuotesPerCakeRequestActivity.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.none);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    startActivity(new Intent(getApplicationContext(), C_HomeActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.nav_bakers) {
                    startActivity(new Intent(getApplicationContext(), C_AllBakersActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.nav_cake_request) {
                    startActivity(new Intent(getApplicationContext(), C_CakeRequestsActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                } else if (item.getItemId() == R.id.nav_profile) {
                    startActivity(new Intent(getApplicationContext(), C_ProfileActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                return false;
            }
        });

    }

    private void loadAllResponses() {
        DatabaseReference responseRef = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customcakeRequestId).child("responses");
        responseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                responseList.clear();
                for (DataSnapshot responseSnapshot : snapshot.getChildren()) {
                    String responseId = responseSnapshot.getKey();
                    String userId = responseSnapshot.child("bakerId").getValue(String.class);
                    Double quotedPrice = responseSnapshot.child("quotedPrice").getValue(Double.class);
                    String responseMessage = responseSnapshot.child("responseMessage").getValue(String.class);
                    String status = responseSnapshot.child("status").getValue(String.class);

                    QuoteResponse cakeResponse = new QuoteResponse();
                    cakeResponse.setQuoteResponseId(responseId);
                    cakeResponse.setCustomCakeRequestId(customcakeRequestId);
                    cakeResponse.setBakerId(userId);
                    cakeResponse.setQuotedPrice(quotedPrice);
                    cakeResponse.setResponseMessage(responseMessage);
                    cakeResponse.setStatus(status);

                    getBakeryTitle(userId, bakeryTitle -> {
                        cakeResponse.setBakeryTitle(bakeryTitle);
                        responseList.add(cakeResponse);
                        adapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CustomCakeFetch", "Failed to fetch custom cake details: " + error.getMessage());
            }
        });
    }


    public void getBakeryTitle(String bakerId, BakeryTitleCallBack callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers");
        databaseReference.child(bakerId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String bakeryTitle = dataSnapshot.child("bakeryTitle").getValue(String.class);
                    Log.d("Firebase", "Bakery Title: " + bakeryTitle);
                    callback.onBakeryTitleFetched(bakeryTitle);
                } else {
                    Log.d("Firebase", "Baker not found!");
                    callback.onBakeryTitleFetched(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error: " + databaseError.getMessage());
                callback.onBakeryTitleFetched(null);
            }
        });
    }

}