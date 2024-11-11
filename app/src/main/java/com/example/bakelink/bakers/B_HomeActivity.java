package com.example.bakelink.bakers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakelink.R;
import com.example.bakelink.bakers.adapters.QuoteRequestAdapter;
import com.example.bakelink.bakers.models.QuoteRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class B_HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private QuoteRequestAdapter adapter;
    private List<QuoteRequest> quoteRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bhome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set welcome text
        TextView welcomeText = findViewById(R.id.welcomeText);
        String username = "username";  // Retrieve actual username from intent or shared preferences
        welcomeText.setText("Welcome back, " + username + "!");

        recyclerView = findViewById(R.id.recyclerViewNewQuoteRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Initialize data
        quoteRequestList = new ArrayList<>();
        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.cakesample2));
        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.themed_cake_image));
        quoteRequestList.add(new QuoteRequest("[Customer name]", "[Cake type]", "[Delivery date]", "[Location]", R.drawable.cakesample3));
        // Add more items as needed

        // Set adapter
        adapter = new QuoteRequestAdapter(this, quoteRequestList);
        recyclerView.setAdapter(adapter);


        // Set up bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_baker);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                //no action
                return true;
            } else if (item.getItemId() == R.id.nav_schedule) {
                startActivity(new Intent(B_HomeActivity.this, B_ScheduleActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_my_cakes) {
                startActivity(new Intent(B_HomeActivity.this, B_MyCakesActivity.class));
                return true;
            } else if (item.getItemId() == R.id.nav_profile) {
                startActivity(new Intent(B_HomeActivity.this, B_ProfileActivity.class));
                return true;
            }
            return false;
        });

    }

}