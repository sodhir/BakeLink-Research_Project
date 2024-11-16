package com.example.bakelink.customers;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.models.QuoteResponse;
import com.example.bakelink.customers.adapters.CakeQuoteResponseAdapter;
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

    CakeQuoteResponseAdapter adapter;
    ArrayList<QuoteResponse> responseList;

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

    }

    private void loadAllResponses() {

        DatabaseReference responseRef = FirebaseDatabase.getInstance().getReference("customCakeRequests").child(customcakeRequestId).child("responses");
        responseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // responseList = new ArrayList<>();

                for (DataSnapshot responseSnapshot : snapshot.getChildren()) {
                    String responseId = responseSnapshot.getKey();
                    Log.d("CustomCakeFetch", "response ID: " + responseId );
                   // String customCakeRequestId = responseSnapshot.child("customCakeRequestId").getValue(String.class);
                    String userId = responseSnapshot.child("userId").getValue(String.class);
                    Double quotedPrice = responseSnapshot.child("quotedPrice").getValue(Double.class);
                    String responseMessage = responseSnapshot.child("responseMessage").getValue(String.class);
                    String status = responseSnapshot.child("status").getValue(String.class);

                    QuoteResponse cakeResponse = new QuoteResponse();
                    cakeResponse.setQuoteResponseId(responseId);
                    cakeResponse.setCustomCakeRequestId(customcakeRequestId);
                    cakeResponse.setUserID(userId);
                    cakeResponse.setQuotedPrice(quotedPrice);
                    cakeResponse.setResponseMessage(responseMessage);
                    cakeResponse.setStatus(status);

                    responseList.add(cakeResponse);
                    Log.d("CustomCakeFetch", "Custom Cake Response: " + responseList.size() );



                }
                //Log.d("CustomCakeFetch", "Custom Cake Response: " + responseList.size() );
                adapter.notifyDataSetChanged();

            }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                          //  Log.e("CustomCakeFetch", "Failed to fetch custom cake details: " + error.getMessage());
                        }
                    });

    }
}