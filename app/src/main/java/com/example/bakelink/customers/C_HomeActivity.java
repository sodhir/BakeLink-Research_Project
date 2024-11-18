package com.example.bakelink.customers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.customers.adapters.DealsAdapter;
import com.example.bakelink.customers.adapters.TopCakeCategoryAdapter;
import com.example.bakelink.customers.adapters.TrendingBakerAdapter;
import com.example.bakelink.customers.modal.Baker;
import com.example.bakelink.customers.modal.CakeCategory;
import com.example.bakelink.customers.modal.Deal;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class C_HomeActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;

    private RecyclerView recyclerViewTrendingBakers;
    private TrendingBakerAdapter trendingBakerAdapter;
    private List<Baker> trendingBakersList;

    private RecyclerView recyclerViewCategories;
    private List<CakeCategory> cakeCategoriesList;
    private TopCakeCategoryAdapter topCakeCategoryAdapter;
    private ImageButton cartIcon;
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String currentUserEmail = getIntent().getStringExtra("email");

        welcomeText = findViewById(R.id.tv_welcome_back);
        welcomeText.setText("Welcome Back, " + currentUserEmail+"!");

        // Set up the top toolbar
        Toolbar toolbar = findViewById(R.id.top_toolbar);
        setSupportActionBar(toolbar);

        //Trending baker section
        // Initialize RecyclerView
        recyclerViewTrendingBakers = findViewById(R.id.recycler_view_trending_bakers);
        trendingBakersList = new ArrayList<>(); // Populate this with your data
       // trendingBakersList.add(new Baker("Sweet Crumbs Bakery", "drawable/baker_a_image", 4.8f));
        //trendingBakersList.add(new Baker("Frosted Perfection", "drawable/baker_b_image", 4.7f));
        //trendingBakersList.add(new Baker("Bake Me Happy", "drawable/baker_c_image", 4.7f));

        // Setup the adapter
        trendingBakerAdapter = new TrendingBakerAdapter(trendingBakersList);
        recyclerViewTrendingBakers.setAdapter(trendingBakerAdapter);
        recyclerViewTrendingBakers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        fetchBakers();

        //Top cake categories section
        //recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        //recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new GridLayoutManager(this, 3));

        // Sample data for cake categories
        cakeCategoriesList = new ArrayList<>();
        cakeCategoriesList.add(new CakeCategory("Wedding Cakes", "drawable/wedding_cake_image"));
        cakeCategoriesList.add(new CakeCategory("Seasonal Cakes", "drawable/seasonal_cake_image"));
        cakeCategoriesList.add(new CakeCategory("Themed Cakes", "drawable/themed_cake_image"));

        topCakeCategoryAdapter = new TopCakeCategoryAdapter(cakeCategoriesList);
        recyclerViewCategories.setAdapter(topCakeCategoryAdapter);

        //deals section
        RecyclerView dealsRecyclerView = findViewById(R.id.deals_recycler_view);

        List<Deal> dealsList = new ArrayList<>();
        dealsList.add(new Deal("Get 20% Off All Vegan Cakes!", "Use Code: VEGAN20", "Hurry! Limited Offer", "drawable/deal_image_1","20% OFF"));
        dealsList.add(new Deal("$10 Off Custom Cake Orders", "For orders above $100", "Valid till Dec 31, 2024", "drawable/deal_image_2","$10 OFF"));
        dealsList.add(new Deal("Order a wedding cake\n" + "and enjoy free delivery", "", "Offer ends Nov 15, 2024", "drawable/deal_image_3","Free Delivery"));

        DealsAdapter dealsAdapter = new DealsAdapter(dealsList);
        dealsRecyclerView.setAdapter(dealsAdapter);
        dealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //fab
        ImageButton fab = findViewById(R.id.fab_request_quote);
        fab.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), C_RequestQuoteActivity.class));
        });

        cartIcon = findViewById(R.id.cart_icon);

        // Set OnClickListener for the cart icon
        cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to C_CartActivity
                Intent intent = new Intent(C_HomeActivity.this, C_CartActivity.class);
                startActivity(intent);
            }
        });



        //Bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    // Already in home, no action needed
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


    private void uploadBakerDetails(String name, String url, float rating){

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("bakers");
        String bakerId = databaseReference.push().getKey(); // Create a unique ID for each baker

        if (bakerId != null) {
            Baker baker = new Baker(name, url, rating);
            databaseReference.child(bakerId).setValue(baker)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("db", "Baker details uploaded successfully");
                    })
                    .addOnFailureListener(e -> {
                        Log.d("db", "Failed to upload baker details");
                    });
        }

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
                trendingBakerAdapter.notifyDataSetChanged(); // Notify adapter of data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("db", "Failed to read data: " + databaseError.getMessage());
            }
        });

    }
}