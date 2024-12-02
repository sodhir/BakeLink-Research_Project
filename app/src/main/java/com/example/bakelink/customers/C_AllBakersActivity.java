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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.customers.adapters.TrendingBakerAdapter;
import com.example.bakelink.customers.modal.Baker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class C_AllBakersActivity extends AppCompatActivity {
    private RecyclerView recyclerViewTrendingBakers;
    private TrendingBakerAdapter trendingBakerAdapter;
    private List<Baker> trendingBakersList;
    private ImageButton cartIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_call_bakers);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });

        cartIcon = findViewById(R.id.cart_icon);


        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(C_AllBakersActivity.this, C_CartActivity.class);
                startActivity(intent);
            }
        });

        recyclerViewTrendingBakers = findViewById(R.id.recycler_view_trending_bakers);
        trendingBakersList = new ArrayList<>();

        // Setup the adapter
        trendingBakerAdapter = new TrendingBakerAdapter(trendingBakersList);
        recyclerViewTrendingBakers.setAdapter(trendingBakerAdapter);
        recyclerViewTrendingBakers.setLayoutManager(new GridLayoutManager(this, 2));

        fetchBakers();

        //Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_bakers);
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

    private void fetchBakers(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bakers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                trendingBakersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Baker baker = snapshot.getValue(Baker.class);
                    if (baker != null) {
                        baker.setId(snapshot.getKey());
                        baker.setName(snapshot.child("bakeryTitle").getValue(String.class));
                        baker.setImageUrl(snapshot.child("profilePictureUrl").getValue(String.class));
                        Float rating = snapshot.child("rating").getValue(Float.class);
                        if(rating != null){
                            baker.setRating(snapshot.child("rating").getValue(Float.class));
                        }else {
                            baker.setRating(0.0f);
                        }

                        trendingBakersList.add(baker);

                    }
                }
                trendingBakerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("db", "Failed to read data: " + databaseError.getMessage());
            }
        });

    }
}